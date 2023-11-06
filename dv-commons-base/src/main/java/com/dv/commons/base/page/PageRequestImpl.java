package com.dv.commons.base.page;

import lombok.Data;

@Data
class PageRequestImpl implements PageRequest {
    protected static final int DEFAULT_SIZE = 10;
    protected static final int MAX_SIZE = 10;
    protected static final int MIN_SIZE = 1;
    protected static final int DEFAULT_PAGE = 1;
    protected static final long DEFAULT_OFFSET = 0;

    protected int pageSize;
    protected int page;

    protected int size;
    protected long offset;

    PageRequestImpl(int page,int pageSize){
        this.page = Math.max(DEFAULT_PAGE,page);
        this.pageSize = handleSize(pageSize);
        this.size = this.pageSize;
        this.offset = (long) (this.page - 1) * this.pageSize;
    }

    PageRequestImpl(long offset,int size){
        this.offset = Math.max(offset, DEFAULT_OFFSET);
        this.size = handleSize(size);
        this.page = (int) (this.offset/this.size + 1);
        this.pageSize = this.size;
    }

    private int handleSize(int size){
        if (size<MIN_SIZE){
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }
}
