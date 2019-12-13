package com.youyouxunyin.algorithm;


public class HashAlgorithm implements Algorithm {

    private static final int len = 16;

    @Override
    public String doSharding(String tableName, Object value) {

        if (this.isEmpty(value)){
            return tableName;
        }else{
            int h;
            int hash = (h = value.hashCode()) ^ (h >>> 16);
            int index = (len - 1) & hash;
            return tableName+"_"+index;
        }
    }
    private boolean isEmpty(Object value){
        if (value instanceof Long){
            long v = (long) value;
            return v==0;
        }
        return value==null;
    }
}
