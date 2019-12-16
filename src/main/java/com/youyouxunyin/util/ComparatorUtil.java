package com.youyouxunyin.util;

import java.lang.reflect.Field;
import java.util.Comparator;

public class ComparatorUtil implements Comparator {

    //要排序的属性名
    private String name;
    //是否升序
    private boolean flag;
    /**
     * @param name 要排序的属性名,如果是基本类型的排序，请入null值
     * @param flag true/false : 升序/降序
     */
    public ComparatorUtil(String name, boolean flag) {
        super();
        this.name = name;
        this.flag = flag;
    }

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        //入参为空|参数类型不相等，都不进行处理。
        if(o1 == null || o2 == null || !o1.getClass().getName().equals(o2.getClass().getName())){
            return result;
        }
        //7大基本类型的处理(boolean除外.且把Collections.reverse()/Collections.sort()的事也做了)
        if(isBaseType(o1)){
            //比较
            return baseTypeOpt(o1,o2);
        }
        try {
            Field f1 = o1.getClass().getDeclaredField(name);
            Field f2 = o2.getClass().getDeclaredField(name);
            //设置private可读
            f1.setAccessible(true);
            f2.setAccessible(true);
            result = baseTypeOpt(f1.get(o1),f2.get(o2));
        }  catch (Exception e) { //异常懒得处理了,如果没有对应的属性,那就不排序了.(手动滑稽)
            e.printStackTrace();
        }
        return result;
    }

    private int baseTypeOpt(Object o1, Object o2) {
        int result = 0;
        if(o1 instanceof String){
            result = o1.toString().compareTo(o2.toString());
        }else if(o1 instanceof Integer){
            result = ((Integer)o1) - ((Integer)o2);
        }else if(o1 instanceof Double){
            if(((Double)o1 - (Double)o2) > 0){
                result = 1;
            }else if(((Double)o1 - (Double)o2) < 0){
                result = -1;
            }
        }else if(o1 instanceof Float){
            if(((Float)o1 - (Float)o2) > 0){
                result = 1;
            }else if(((Float)o1 - (Float)o2) < 0){
                result = -1;
            }
        }else if(o1 instanceof Character){
            result = ((Character)o1).compareTo(((Character)o2));
        }else if(o1 instanceof Short){
            result = ((Short)o1) - ((Short)o2);
        }else if(o1 instanceof Long){
            if(((Long)o1 - (Long)o2) > 0){
                result = 1;
            }else if(((Long)o1 - (Long)o2) < 0){
                result = -1;
            }
        }
        //降序
        if(!isFlag()){
            result = -result;
        }
        return result;
    }

    private boolean isBaseType(Object o) {
        if((o instanceof String) || (o instanceof Integer)
                || (o instanceof Double) || (o instanceof Float)
                || (o instanceof Character) || (o instanceof Short)
                || (o instanceof Long)){
            return true;
        }
        return false;
    }

    public boolean isFlag() {
        return flag;
    }
}
