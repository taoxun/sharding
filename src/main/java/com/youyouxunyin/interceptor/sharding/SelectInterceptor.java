package com.youyouxunyin.interceptor.sharding;

import cn.hutool.core.collection.CollUtil;
import com.youyouxunyin.annotations.Sharding;
import com.youyouxunyin.interceptor.SelectSqlParser;
import com.youyouxunyin.util.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.*;

@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class SelectInterceptor implements Interceptor {

    @Autowired
    SelectSqlParser sqlParser;

    private Map<String,MappedStatement> msMap = new HashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement)args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds)args[2];
        ResultHandler resultHandler = (ResultHandler)args[3];
        Executor executor = (Executor)invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey)args[4];
            boundSql = (BoundSql)args[5];
        }
        Page page = PageContext.getPage();
        Sharding annotation = getAnnotation(parameter);
        if (annotation!=null){
            List<Object> list = querySharding(executor, rowBounds, resultHandler, ms, boundSql, cacheKey, annotation, parameter,page);
            if (list!=null &&list.size()>=0){
                return list;
            }
        }
        if (page!=null){
            return this.queryPage(page,boundSql.getSql(),executor,ms,boundSql,cacheKey,rowBounds,resultHandler,parameter);
        }
        return invocation.proceed();
    }

    private <E> List<E> querySharding(Executor executor, RowBounds rowBounds, ResultHandler resultHandler, MappedStatement ms, BoundSql boundSql, CacheKey cacheKey, Sharding sharding, Object parameter,Page page) throws JSQLParserException, IllegalAccessException, NoSuchFieldException, SQLException {

        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {

            Object value = ReflectionUtil.getFieldValue(parameter, sharding.field());
            Statement statement = CCJSqlParserUtil.parse(boundSql.getSql());
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", sharding.tableName());
            map.put("value", value);
            map.put("mode", sharding.mode());
            map.put("length", sharding.length());
            ShardingContext.set(map);
            List<E> result = new ArrayList<>();

            //分表键不为空
            if (!StringUtils.isEmpty(value)){
                sqlParser.processSelect(statement,-1);
                if (page!=null){
                    result = queryPage(page,statement.toString(),executor,ms,boundSql,cacheKey,rowBounds,resultHandler,parameter);
                }else {
                    result = ExecutorUtil.query(statement.toString(),executor,ms,parameter,rowBounds,resultHandler,boundSql,cacheKey);
                }
                return result;
            }else {
                //分表键为空 但是分表算法为Hash算法
                if ("hash".equals(sharding.mode())){
                    if (page!=null){
                        int counts = 0;
                        for (int i=0;i<sharding.length();i++){
                            Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());
                            sqlParser.processSelect(parse,i);
                            Long count = this.getCount(parse.toString(),executor,ms,parameter,rowBounds,resultHandler,boundSql);
                            counts+=count;
                            if (count>0){
                                cacheKey.update(new Object());
                                String limitSql = getLimitSql(page,parse.toString());
                                List<E> query = ExecutorUtil.query(limitSql, executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
                                result.addAll(query);
                            }
                        }
                        page.setTotal(counts);
                        page.setCountPage();
                        if (page.getTotal()>0){
                            sqlParser.sort(statement,result);
                            return page.page(result);
                        }
                    }else {
                        for (int i=0;i<sharding.length();i++){
                            Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());
                            sqlParser.processSelect(parse,i);
                            cacheKey.update(new Object());
                            List<E> query = ExecutorUtil.query(parse.toString(), executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
                            result.addAll(query);
                        }
                    }
                }
            }
            return result;
        }
        return null;
    }


    private String getLimitSql(Page page,String sql){
        StringBuffer sb = new StringBuffer();
        sb.append(sql);
        sb.append(" limit ");
        sb.append(page.getStart());
        sb.append("," + page.getPageSize() );
        return sb.toString();
    }

    private <E> List<E> queryPage(Page page,String sql,Executor executor,MappedStatement ms,BoundSql boundSql, CacheKey cacheKey,RowBounds rowBounds, ResultHandler resultHandler,Object parameter) throws SQLException, IllegalAccessException {
        Long count = this.getCount(sql,executor,ms,parameter,rowBounds,resultHandler,boundSql);
        page.setTotal(count.intValue());
        page.setCountPage();
        String limitSql = getLimitSql(page,sql);
        List<E> query = ExecutorUtil.query(limitSql, executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
        page.addAll(query);
        return page;
    }

    private Long getCount(String sql,Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException, IllegalAccessException {
        String countMsId = ms.getId() + "_query_count";
        MappedStatement countMs = ExecutorUtil.getExistedMappedStatement(ms.getConfiguration(), countMsId);
        Long count;
        if (countMs != null) {
            count = ExecutorUtil.executeManualCount(executor, countMs, parameter, boundSql, resultHandler);
        } else {
            countMs = this.msMap.get(countMsId);
            if (countMs == null) {
                countMs = MSUtils.newCountMappedStatement(ms, countMsId);
                this.msMap.put(countMsId, countMs);
            }
            count = ExecutorUtil.executeAutoCount(sql,executor, countMs, parameter, boundSql, rowBounds, resultHandler);
        }
        return count;
    }


    private Sharding getAnnotation(Object parameter){
        if (parameter!=null){
            Sharding annotation = AnnotationUtils.findAnnotation(parameter.getClass(), Sharding.class);
            if (annotation!=null){
                return annotation;
            }else {
                if (parameter instanceof Map){
                    if (((Map) parameter).containsKey("sharding")){
                        Object obj = ((Map) parameter).get("sharding");
                        return AnnotationUtils.findAnnotation(obj.getClass(), Sharding.class);
                    }
                }
            }
        }
        return null;
    }
}
