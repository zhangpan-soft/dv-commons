package com.dv.commons.base.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class PageImpl<T> extends PageRequestImpl implements Page<T>{
    protected long totalCount;
    protected List<T> list;
    protected int pageNum;

    PageImpl(long offset,int size,long totalCount,List<T> list){
        super(offset, size);
        handleProperties(totalCount,list);
    }

    PageImpl(int page,int pageSize,long totalCount,List<T> list){
        super(page, pageSize);
        handleProperties(totalCount,list);
    }

    private void handleProperties(long totalCount,List<T> list){
        this.totalCount = totalCount;
        this.list = list;
        this.pageNum = (int) (this.totalCount/this.size + 1);
        if (this.list == null){
            this.list = List.of();
        }
    }
}
