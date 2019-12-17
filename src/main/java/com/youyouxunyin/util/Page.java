package com.youyouxunyin.util;

import java.util.ArrayList;
import java.util.List;

public class Page<E> extends ArrayList<E> {

    private int start;
    private int pageSize;
    private int total;
    private int countPage;

    public Page(int pageNum,int pageSize) {
        super(0);
        this.pageSize = pageSize;
        this.start = (pageNum-1)*this.pageSize;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage() {
        int countPage = total % pageSize == 0 ? total/pageSize:total/pageSize+1;
        this.countPage = countPage;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }


    public Page<E> page(List<E> result){
        if (result.size()>=this.getStart()){
            int end = this.getStart()+this.getPageSize();
            if (end>result.size()){
                end = result.size();
            }
            result = new ArrayList<>(result.subList(this.getStart(),end));
        }else {
            result = new ArrayList<>();
        }
        this.addAll(result);
        return this;
    }
}
