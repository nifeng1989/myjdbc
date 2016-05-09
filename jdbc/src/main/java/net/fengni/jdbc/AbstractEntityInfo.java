package net.fengni.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengni on 2015/10/15.
 */
public abstract class AbstractEntityInfo {

    Class clazz;
    String table;
    boolean autoIncrement = false;
    String dataSourceRW;
    String dataSourceRO;
    boolean useDataSourceRO;
    Map<String, String> fieldColumnMap = new HashMap<String, String>();
    Map<String, String> columnFieldMap = new HashMap<String, String>();
    String selectSql;
    String insertSql;
    String updateSql;
    String deleteSql;
    String primaryKeyField;
    RowMapper rowMapper;

    protected AbstractEntityInfo(Class clazz) {
        this.clazz = clazz;
    }

    protected Class getClazz() {
        return clazz;
    }


    void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    protected Map<String, String> getFieldColumnMap() {
        return fieldColumnMap;
    }


    protected Map<String, String> getColumnFieldMap() {
        return columnFieldMap;
    }

    void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }


    protected void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }


    protected void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }


    protected void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    protected void setPrimaryKeyField(String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    protected void setTable(String table) {
        this.table = table;
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }

    protected void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }


    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    protected void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    protected String getDataSourceRW() {
        return dataSourceRW;
    }

    protected void setDataSourceRW(String dataSourceRW) {
        this.dataSourceRW = dataSourceRW;
    }

    protected String getDataSourceRO() {
        return dataSourceRO;
    }

    protected void setDataSourceRO(String dataSourceRO) {
        this.dataSourceRO = dataSourceRO;
    }

    protected boolean isUseDataSourceRO() {
        return useDataSourceRO;
    }

    protected void setUseDataSourceRO(boolean useDataSourceRO) {
        this.useDataSourceRO = useDataSourceRO;
    }
}
