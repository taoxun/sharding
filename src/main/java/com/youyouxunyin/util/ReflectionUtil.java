package com.youyouxunyin.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionUtil {


    public static Field getField(Object obj, String name) {
        Field field = ReflectionUtils.findField(obj.getClass(), name);
        field.setAccessible(true);
        return field;
    }

    public static void setField(Object obj,String target,String value) throws IllegalAccessException {
        Field field = getField(obj, target);
        field.set(obj,value);
    }

    public static Object getFieldValue(Object object,String key) throws IllegalAccessException {
        if (object instanceof Map){
            Object o = ((Map) object).get(key);
            if (o!=null){
                return o;
            }else {
                Object sharding = ((Map) object).get("sharding");
                return getFieldValue(sharding,key);
            }
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields) {
            f.setAccessible(true);
            if (f.getName().equals(key)){
                return f.get(object);
            }
        }
        return null;
    }
}
