package com.youyouxunyin.interceptor;

import java.util.Map;

public class ShardingContext {

    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();

    public static void set(Map<String,Object> map){
        threadLocal.set(map);
    }

    public static Map<String,Object> get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
