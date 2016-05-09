package net.fengni.jdbc.shard;

/**
 * Created by fengni on 2015/10/28.
 */
public class DefaultShardRule implements IShardRule {
    private int size;
    private String table;

    public int getIndex(Object object) {
        return (Integer) object % size;
    }

    public String getTableName(Object object) {
        return table + "_" + getIndex(object);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
