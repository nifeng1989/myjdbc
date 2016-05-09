package net.fengni.jdbc;

/**
 * Created by fengni on 2015/10/15.
 */
public class NormalEntityInfo extends AbstractEntityInfo {

    protected NormalEntityInfo(Class clazz) {
        super(clazz);
    }

    protected String getSelectSql() {
        return selectSql;
    }

    protected String getInsertSql() {
        return insertSql;
    }


    protected String getUpdateSql() {
        return updateSql;
    }

    protected String getDeleteSql() {
        return deleteSql;
    }


    public String getTable() {
        return table;
    }

}
