package com.dv.commons.base.page;

import java.util.List;

public interface Page<T> extends PageRequest {

    int getPageNum();

    long getTotalCount();

    List<T> getList();

    static <T> Page<T> of(long offset,int size,long totalCount,List<T> list){
        return new PageImpl<>(offset,size,totalCount,list);
    }

    static <T> Page<T> of(int page,int pageSize,long totalCount,List<T> list){
        return new PageImpl<>(page,pageSize,totalCount,list);
    }
}
