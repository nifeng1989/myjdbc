package com.nifeng.jdbc.query;

/**
 * Created by fengni on 2015/11/4.
 */
public class Order {
    private String column;
    private OrderMode orderMode;
    public Order(String column,OrderMode orderMode){
        this.column = column;
        this.orderMode = orderMode;
    }

    String toSql(){
        return " " + column + " " + orderMode.name();
    }
}
