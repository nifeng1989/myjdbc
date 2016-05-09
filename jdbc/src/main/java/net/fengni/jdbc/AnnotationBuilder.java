package net.fengni.jdbc;

import net.fengni.jdbc.annotation.*;
import net.fengni.jdbc.shard.IShardRule;
import net.fengni.jdbc.util.CommonUtil;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by fengni on 2015/9/24.
 */
public class AnnotationBuilder {
    public AnnotationBuilder() {
    }

    public AnnotationBuilder(List<String> scanPackages) {
        build(scanPackages);
    }

    public AnnotationBuilder(String scanPackage) {
        List<String> scanPackages = new LinkedList<String>();
        scanPackages.add(scanPackage);
        build(scanPackages);
    }

    public void build(List<String> packages) {
        Set<Class<?>> classSet = PackageScanner.getClasses(Entity.class, packages);  //得到目标包下所有的Entity类
        try {
            for (Class cls : classSet) {
                parseAnnotation(cls);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void parseAnnotation(Class cls) throws Exception {
        Table table = (Table) cls.getAnnotation(Table.class);
        String tableName = cls.getSimpleName();
        if (table != null) {
            tableName = table.value();
        }
        AbstractEntityInfo entityInfo;
        Shard shard = (Shard) cls.getAnnotation(Shard.class);
        if (shard != null) {
            ShardEntityInfo shardEntityInfo = new ShardEntityInfo(cls);
            IShardRule shardRule = shard.shardRule().newInstance();
            shardRule.setSize(shard.size());
            shardRule.setTable(tableName);
            shardEntityInfo.setShardRule(shardRule);
            shardEntityInfo.setTable(tableName);
            entityInfo = shardEntityInfo;
            parseShardEntity(cls, shardEntityInfo);
        } else {
            NormalEntityInfo normalEntityInfo = new NormalEntityInfo(cls);
            normalEntityInfo.setTable(tableName);
            entityInfo = normalEntityInfo;
            parseNormalEntity(cls, normalEntityInfo);
        }

        DataSource dataSource = (DataSource) cls.getAnnotation(DataSource.class);
        String dataSourceRW = CommonUtil.DEFAULT_DATASOURCE_RW;
        String dataSourceRO = CommonUtil.DEFAULT_DATASOURCE_RW;
        boolean useDataSourceRO = false;
        if (dataSource != null) {
            dataSourceRW = dataSource.dataSourceRW();
            dataSourceRO = dataSource.dataSourceRO();
            if (dataSourceRO == CommonUtil.EMPTY_STRING) {
                dataSourceRO = dataSourceRW;
            }
            useDataSourceRO = dataSource.useDataSourceRO();
        }
        entityInfo.setDataSourceRW(dataSourceRW);
        entityInfo.setDataSourceRO(dataSourceRO);
        entityInfo.setUseDataSourceRO(useDataSourceRO);

        RowMapper rowMapper = new EntityRowMapper(cls);
        entityInfo.setRowMapper(rowMapper);
        EntityHelper.setEntityInfo(cls.getName(), entityInfo);
    }

    private void parseNormalEntity(Class cls, NormalEntityInfo entityInfo) {
        Field[] fields = cls.getDeclaredFields();

        StringBuilder insertSb = new StringBuilder();
        insertSb.append("insert into ").append(entityInfo.getTable()).append("(");
        StringBuilder values = new StringBuilder();

        StringBuilder updateSb = new StringBuilder();
        updateSb.append("update ").append(entityInfo.getTable()).append(" set ");
        StringBuilder conditionSb = new StringBuilder();

        StringBuilder selectSB = new StringBuilder();
        selectSB.append("select ");

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.value();
                if (CommonUtil.EMPTY_STRING.equals(columnName)) {
                    columnName = field.getName();
                }
                entityInfo.getFieldColumnMap().put(field.getName(), columnName);
                entityInfo.getColumnFieldMap().put(columnName, field.getName());

                selectSB.append(columnName).append(",");

                if (field.isAnnotationPresent(Id.class)) {
                    entityInfo.setPrimaryKeyField(field.getName());
                    conditionSb.append(" where ").append(columnName).append("=:").append(columnName);
                    if (field.isAnnotationPresent(AutoIncrement.class)) {
                        entityInfo.setAutoIncrement(true);
                    }
                } else {
                    insertSb.append(columnName).append(",");
                    values.append(":").append(field.getName()).append(",");
                    updateSb.append(columnName).append("=:").append(field.getName()).append(",");
                }
            }
        }
        insertSb.delete(insertSb.length() - 1, insertSb.length());
        values.delete(values.length() - 1, values.length());
        insertSb.append(") values (");
        insertSb.append(values).append(")");
        entityInfo.setInsertSql(insertSb.toString());

        updateSb.delete(updateSb.length() - 1, updateSb.length());
        updateSb.append(conditionSb);
        entityInfo.setUpdateSql(updateSb.toString());

        StringBuilder deleteSB = new StringBuilder();
        deleteSB.append("delete from ").append(entityInfo.getTable()).append(" where ").append(entityInfo.getFieldColumnMap().get(entityInfo.getPrimaryKeyField())).append("=:").append(entityInfo.getPrimaryKeyField());
        entityInfo.setDeleteSql(deleteSB.toString());

        selectSB.delete(selectSB.length()-1,selectSB.length());
        selectSB.append(" from ").append(entityInfo.getTable()).append(" where ").append(entityInfo.getFieldColumnMap().get(entityInfo.getPrimaryKeyField())).append("=:").append(entityInfo.getPrimaryKeyField());
        entityInfo.setSelectSql(selectSB.toString());
    }

    private void parseShardEntity(Class cls, ShardEntityInfo entityInfo) {
        Field[] fields = cls.getDeclaredFields();

        StringBuilder insertSb = new StringBuilder();
        insertSb.append("insert into %s ").append("(");
        StringBuilder values = new StringBuilder();

        StringBuilder updateSb = new StringBuilder();
        updateSb.append("update %s ").append(" set ");
        StringBuilder conditionSb = new StringBuilder();

        StringBuilder selectSB = new StringBuilder();
        selectSB.append("select ");

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.value();
                if (CommonUtil.EMPTY_STRING.equals(columnName)) {
                    columnName = field.getName();
                }
                entityInfo.getFieldColumnMap().put(field.getName(), columnName);
                entityInfo.getColumnFieldMap().put(columnName, field.getName());

                selectSB.append(columnName).append(",");

                if (field.isAnnotationPresent(Id.class)) {
                    entityInfo.setPrimaryKeyField(field.getName());
                    conditionSb.append(" where ").append(columnName).append("=:").append(columnName);
                    if (field.isAnnotationPresent(AutoIncrement.class)) {
                        entityInfo.setAutoIncrement(true);
                    }
                } else {
                    insertSb.append(columnName).append(",");
                    values.append(":").append(field.getName()).append(",");
                    updateSb.append(columnName).append("=:").append(field.getName()).append(",");
                }
            }
        }
        insertSb.delete(insertSb.length() - 1, insertSb.length());
        values.delete(values.length() - 1, values.length());
        insertSb.append(") values (");
        insertSb.append(values).append(")");
        entityInfo.setInsertSql(insertSb.toString());

        updateSb.delete(updateSb.length() - 1, updateSb.length());
        updateSb.append(conditionSb);
        entityInfo.setUpdateSql(updateSb.toString());

        StringBuilder deleteSB = new StringBuilder();
        deleteSB.append("delete from %s ").append(" where ").append(entityInfo.getFieldColumnMap().get(entityInfo.getPrimaryKeyField())).append("=:").append(entityInfo.getPrimaryKeyField());
        entityInfo.setDeleteSql(deleteSB.toString());

        selectSB.append(" from %s ").append(" where ").append(entityInfo.getFieldColumnMap().get(entityInfo.getPrimaryKeyField())).append("=:").append(entityInfo.getPrimaryKeyField());
        entityInfo.setSelectSql(selectSB.toString());
    }
}
