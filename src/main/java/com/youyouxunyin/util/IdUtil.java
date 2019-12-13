package com.youyouxunyin.util;

import cn.hutool.core.lang.Snowflake;

public class IdUtil {


    private static Snowflake snowflake = new Snowflake(1,0);

    public static String nextId(){
        return snowflake.nextIdStr();
    }

    public static long getId(){
        return snowflake.nextId();
    }
}
