package net.fengni.jdbc.query;

import java.io.Serializable;

/**
 *查询条件
 */
public class Condition implements Serializable {

    /**
     * 属性比较类型.
     * 当类型为IN类型时，Object value必须为Collection类型
     */
    public enum MatchType {
        EQ, LIKE, IN, NE, GE, LE, GT, LT, IS, IS_NOT, NOT_IN
    }

    private String column;
    private Object value;
    private MatchType matchType;

    public Condition(final String column, final Object value, final MatchType matchType) {
        this.column = column;
        this.value = value;
        this.matchType = matchType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
        result = prime * result + ((column == null) ? 0 : column.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Condition other = (Condition) obj;
        if (matchType == null) {
            if (other.matchType != null)
                return false;
        } else if (!matchType.equals(other.matchType))
            return false;
        if (column == null) {
            if (other.column != null)
                return false;
        } else if (!column.equals(other.column))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    String getColumn() {
        return column;
    }

    Object getValue() {
        return value;
    }

    String toSql() {
        if (Condition.MatchType.EQ == matchType) {
            return column + "=:" + column;
        } else if (Condition.MatchType.NE == matchType) {
            return column + "<>:" + column;
        } else if (Condition.MatchType.GT == matchType) {
            return column + ">:" + column;
        } else if (Condition.MatchType.GE == matchType) {
            return column + ">=:" + column;
        } else if (Condition.MatchType.LT == matchType) {
            return column + "<:" + column;
        } else if (Condition.MatchType.LE == matchType) {
            return column + "<=:" + column;
        } else if (Condition.MatchType.IN == matchType) {
            return column + " in (:" + column + ")";

        } else if (Condition.MatchType.NOT_IN == matchType) {
            return column + " not in (:" + column + ")";
        } else if (Condition.MatchType.LIKE == matchType) {
            return column + " like ':" + column + "'";
        } else if(MatchType.IS == matchType){
            return column + " is :" + column;
        } else if (MatchType.IS_NOT == matchType){
            return column + "is not :" + column;
        }else {
            return "";
        }
    }
}
