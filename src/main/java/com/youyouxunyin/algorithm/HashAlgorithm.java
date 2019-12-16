package com.youyouxunyin.algorithm;


public class HashAlgorithm implements Algorithm {

    @Override
    public String doSharding(String tableName, Object value,int length) {

        if (this.isEmpty(value)){
            return tableName;
        }else{
            int h;
            int hash = (h = value.hashCode()) ^ (h >>> 16);
            int index;
            if (is2Power(length)){
                index = (length - 1) & hash;
            }else {
                index = Math.floorMod(hash, length);
            }
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

    public boolean is2Power(int length){
        return (length & (length-1)) ==0;
    }
}
