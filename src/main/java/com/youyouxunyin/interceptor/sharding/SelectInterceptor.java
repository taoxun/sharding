package com.youyouxunyin.interceptor.sharding;

import com.youyouxunyin.annotations.Sharding;
import com.youyouxunyin.interceptor.SelectSqlParser;
import com.youyouxunyin.util.ExecutorUtil;
import com.youyouxunyin.util.ReflectionUtil;
import com.youyouxunyin.util.ShardingContext;
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

        Sharding annotation = getAnnotation(parameter);
        if (annotation!=null){
            List<Object> list = rewrite(executor, rowBounds, resultHandler, ms, boundSql, cacheKey, annotation, parameter);
            if (list!=null &&list.size()>=0){
                return list;
            }
        }
        return invocation.proceed();
    }

    private <E> List<E> rewrite(Executor executor, RowBounds rowBounds, ResultHandler resultHandler, MappedStatement ms, BoundSql boundSql, CacheKey cacheKey, Sharding sharding, Object parameter) throws JSQLParserException, IllegalAccessException, NoSuchFieldException, SQLException {

        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {

            Object value = ReflectionUtil.getFieldValue(parameter, sharding.field());
            Statement statement = CCJSqlParserUtil.parse(boundSql.getSql());
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", sharding.tableName());
            map.put("value", value);
            map.put("mode", sharding.mode());
            map.put("length", sharding.length());
            ShardingContext.set(map);

            if (!StringUtils.isEmpty(value)) {
                sqlParser.processSelect(statement,-1);
                return ExecutorUtil.query(statement.toString(),executor,ms,parameter,rowBounds,resultHandler,boundSql,cacheKey);
            }else {

                if ("hash".equals(sharding.mode())){
                    List<E> result = new ArrayList<>();
                    for (int i=0;i<sharding.length();i++){
                        Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());
                        sqlParser.processSelect(parse,i);
                        cacheKey.update(new Object());
                        List<E> query = ExecutorUtil.query(parse.toString(), executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
                        result.addAll(query);
                    }
                    sqlParser.sort(statement,result);
                    return result;
                }
            }
        }
        return null;
    }

    private Sharding getAnnotation(Object parameter){
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
        return null;
    }
}
