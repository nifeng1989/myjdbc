package com.nifeng.jdbc.query;

/**
 * Created by fengni on 2015/11/4.
 */
public class Limit {
    private int startIndex;
    private int size;
    public Limit(int startIndex,int size){
        this.startIndex = startIndex;
        this.size = size;
    }
    int getStartIndex() {
        return startIndex;
    }

    int getSize() {
        return size;
    }
}
