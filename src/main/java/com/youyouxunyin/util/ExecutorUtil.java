package com.youyouxunyin.util;

import cn.hutool.db.dialect.Dialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExecutorUtil {

    private static Field additionalParametersField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Long executeManualCount(Executor executor, MappedStatement countMs, Object parameter, BoundSql boundSql, ResultHandler resultHandler) throws SQLException {
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = countMs.getBoundSql(parameter);
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = ((Number)((List)countResultList).get(0)).longValue();
        return count;
    }

    public static Long executeAutoCount(String sql,Executor executor, MappedStatement countMs, Object parameter, BoundSql boundSql, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException, IllegalAccessException {
        Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        countKey.update(new Object());
        String countSql = getCountSql(sql);
        BoundSql countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        setAdditionalParameter(countBoundSql,additionalParameters);
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = (Long)((List)countResultList).get(0);
        return count;
    }

    public static String getCountSql(String sql){
        int index = sql.toLowerCase().indexOf("from");
        String count_sql =  "select count(*) " + sql.substring(index);
        return count_sql;
    }

    public static MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
        MappedStatement mappedStatement = null;
        try {
            mappedStatement = configuration.getMappedStatement(msId, false);
        } catch (Throwable var4) {}
        return mappedStatement;
    }

    public static <E> List<E> query(String sql,Executor executor, MappedStatement ms, Object parameter,RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql, CacheKey cacheKey) throws SQLException, IllegalAccessException {

        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameter);
        Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
        setAdditionalParameter(pageBoundSql,additionalParameters);
        return executor.query(ms,parameter,rowBounds,resultHandler,cacheKey,pageBoundSql);

    }

    private static void setAdditionalParameter(BoundSql boundSql,Map<String, Object> additionalParameters){
        Iterator var11 = additionalParameters.keySet().iterator();

        while(var11.hasNext()) {
            String key = (String)var11.next();
            boundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
    }

    public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) throws IllegalAccessException {
        return (Map<String, Object>) additionalParametersField.get(boundSql);
    }
}
