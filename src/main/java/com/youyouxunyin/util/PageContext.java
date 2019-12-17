package com.youyouxunyin.util;

public final class PageContext {

    private static ThreadLocal<Page> threadLocal = new ThreadLocal();

    public static <E> Page<E> startPage(int pageNum,int pageSize){
        Page page = new Page(pageNum,pageSize);
        threadLocal.set(page);
        return page;
    }

    public static <T> Page<T> getPage(){
        return threadLocal.get();
    }

    public static void clear(){
        threadLocal.remove();
    }
}
