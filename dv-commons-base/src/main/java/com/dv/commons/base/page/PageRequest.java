package com.dv.commons.base.page;

public interface PageRequest {

    default long getOffset(){
        return (long) (this.getPage() - 1) * this.getPageSize();
    }

    default int getSize(){
        return this.getPageSize();
    }

    int getPage();

    int getPageSize();

    static PageRequest of(int page,int pageSize){
        return new PageRequestImpl(page,pageSize);
    }

    static PageRequest of(long offset,int size){
        return new PageRequestImpl(offset,size);
    }
}
