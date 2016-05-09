package net.fengni.jdbc.shard;

/**
 * Created by fengni on 2015/10/28.
 */
public interface IShardRule {
    //获取分表的序号
    public int getIndex(Object object);

    //获取分表的名称
    public String getTableName(Object object);

    void setTable(String table);

    void setSize(int size);
}
