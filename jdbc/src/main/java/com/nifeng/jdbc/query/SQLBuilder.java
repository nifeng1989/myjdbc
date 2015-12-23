package com.nifeng.jdbc.query;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by fengni on 2015/11/3.
 */
public class SQLBuilder {
    private List<Condition> conditionList = new LinkedList<>();
    private List<Order> orderList = new LinkedList<>();
    private Limit limit;
    private String table;

    private Map<String, Object> params;

    public SQLBuilder(String table) {
        this.table = table;
    }

    public SQLBuilder addOrder(Order order) {
        if (order != null) {
            orderList.add(order);
        }
        return this;
    }

    public SQLBuilder addOrder(List<Order> orderList) {
        if (orderList != null) {
            this.orderList.addAll(orderList);
        }
        return this;
    }

    public SQLBuilder removeOrder(Order order) {
        orderList.remove(order);
        return this;
    }

    public SQLBuilder addCondition(Condition condition) {
        if (condition != null) {
            conditionList.add(condition);
        }
        return this;
    }

    public SQLBuilder addCondition(List<Condition> conditionList) {
        if (conditionList != null) {
            conditionList.addAll(conditionList);
        }
        return this;
    }

    public SQLBuilder addCondition(Condition[] conditions) {
        if (conditions != null && conditions.length > 0) {
            for (Condition condition : conditions) {
                conditionList.add(condition);
            }
        }
        return this;
    }

    public SQLBuilder removeCondition(Condition condition) {
        conditionList.remove(condition);
        return this;
    }

    public Limit getLimit() {
        return limit;
    }

    public SQLBuilder setLimit(Limit limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 根据查询条件拼SQL语句，
     * eg. select * from %s where id=1;
     * 拿到sql语句后需要 format一下表名
     */
    public String select() {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(table);
        if (conditionList != null && conditionList.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < conditionList.size(); ) {
                Condition condition = conditionList.get(i);
                sb.append(condition.toSql());
                i++;
                if (i < conditionList.size()) {
                    sb.append(" and ");
                }
            }
        }
        if (orderList != null && orderList.size() > 0) {
            sb.append(" order by ");
            for (int i = 0; i < orderList.size(); ) {
                Order order = orderList.get(i);
                sb.append(order.toSql());
                i++;
                if (i <= orderList.size()) {
                    sb.append(",");
                }
            }
        }
        if (limit != null) {
            sb.append(" limit ").append(limit.getStartIndex());
            sb.append(",").append(limit.getSize());
        }
        return sb.toString();
    }

    public String count() {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ").append(table);
        if (conditionList != null && conditionList.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < conditionList.size(); ) {
                Condition condition = conditionList.get(i);
                sb.append(condition.toSql());
                i++;
                if (i < conditionList.size()) {
                    sb.append(" and ");
                }
            }
        }
        return sb.toString();
    }

    public Map<String, Object> getParams() {
        if (conditionList != null && conditionList.size() > 0) {
            if (params == null) {
                params = new HashMap<>();
                for (Condition condition : conditionList) {
                    params.put(condition.getColumn(), condition.getValue());
                }
            }
        }
        return params;
    }
}
