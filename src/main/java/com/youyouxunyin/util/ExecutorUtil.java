package com.youyouxunyin.util;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
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

    public static <E> List<E> query(String sql,Executor executor, MappedStatement ms, Object parameter,RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql, CacheKey cacheKey) throws SQLException, IllegalAccessException {

        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameter);
        Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
        Iterator var12 = additionalParameters.keySet().iterator();
        while(var12.hasNext()) {
            String key = (String)var12.next();
            pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
        return executor.query(ms,parameter,rowBounds,resultHandler,cacheKey,pageBoundSql);

    }

    public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) throws IllegalAccessException {
        return (Map<String, Object>) additionalParametersField.get(boundSql);
    }
}
