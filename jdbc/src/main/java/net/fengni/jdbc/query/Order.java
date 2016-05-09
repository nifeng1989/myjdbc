package net.fengni.jdbc.query;

import java.io.Serializable;

/**
 * Created by fengni on 2015/11/4.
 */
public class Order implements Serializable {
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
