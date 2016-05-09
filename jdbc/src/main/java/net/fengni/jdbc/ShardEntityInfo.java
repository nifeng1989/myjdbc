package net.fengni.jdbc;

import net.fengni.jdbc.shard.IShardRule;

/**
 * Created by fengni on 2015/10/15.
 */
public class ShardEntityInfo extends AbstractEntityInfo {

    IShardRule shardRule;
    protected ShardEntityInfo(Class clazz) {
        super(clazz);
    }

    protected String getSelectSql(Object object) {
        return String.format(selectSql, shardRule.getTableName(object));
    }


    protected String getInsertSql(Object object) {
        return String.format(insertSql, shardRule.getTableName(object));
    }



    protected String getUpdateSql(Object object) {
        return String.format(updateSql, shardRule.getTableName(object));
    }


    protected String getDeleteSql(Object object) {
        return String.format(deleteSql, shardRule.getTableName(object));
    }


    public String getTable(Object object) {
        if (shardRule != null) {
            return shardRule.getTableName(object);
        }
        return table;
    }


    protected void setShardRule(IShardRule shardRule) {
        this.shardRule = shardRule;
    }


}
