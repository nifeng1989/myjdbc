package com.nifeng.jdbc;

import com.nifeng.jdbc.query.ColumnNotFoundException;
import com.nifeng.jdbc.query.JdbcTemplateFactory;
import com.nifeng.jdbc.util.CommonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的dao必须从此类继承，且使用泛型
 * Created by fengni on 2015/9/4.
 */
public abstract class ShardBaseDao<T> implements Serializable {

    private JdbcTemplate jdbcTemplateRW;

    private JdbcTemplate jdbcTemplateRO;

    private NamedParameterJdbcTemplate namedJdbcTemplateRW;

    private NamedParameterJdbcTemplate namedJdbcTemplateRO;

    private ShardEntityInfo entityInfo;

    public ShardBaseDao() {
        //将泛型提取出来并注入entityInfo实例
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        String[] classes = type.getActualTypeArguments()[0].toString().split(" ");
        entityInfo = (ShardEntityInfo) EntityHelper.getEntityInfo(classes[1]);
    }

    public JdbcTemplate getJdbcTemplateRW() {
        if (jdbcTemplateRW == null) {
            jdbcTemplateRW = JdbcTemplateFactory.getJdbcTemplateRW(entityInfo.getDataSourceRW());
        }
        return jdbcTemplateRW;
    }

    public JdbcTemplate getJdbcTemplateRO() {
        if (jdbcTemplateRO == null) {
            jdbcTemplateRO = JdbcTemplateFactory.getJdbcTemplateRO(entityInfo.getDataSourceRO());
        }
        return jdbcTemplateRO;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplateRW() {
        if (namedJdbcTemplateRW == null) {
            namedJdbcTemplateRW = JdbcTemplateFactory.getNamedJdbcTemplateRW(entityInfo.getDataSourceRW());
        }
        return namedJdbcTemplateRW;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplateRO() {
        if (namedJdbcTemplateRO == null) {
            namedJdbcTemplateRO = JdbcTemplateFactory.getNamedJdbcTemplateRO(entityInfo.getDataSourceRO());
        }
        return namedJdbcTemplateRO;
    }

    public ShardEntityInfo getEntityInfo() {
        return entityInfo;
    }


    public T getEntity(final int primaryKey) {
        Map<String, Integer> params = new HashMap();
        params.put(entityInfo.getPrimaryKeyField(), primaryKey);

        List<T> list = null;
        if (entityInfo.isUseDataSourceRO()) {
            list = getNamedJdbcTemplateRO().query(entityInfo.getSelectSql(primaryKey), params, entityInfo.getRowMapper());
        } else {
            list = getNamedJdbcTemplateRW().query(entityInfo.getSelectSql(primaryKey), params, entityInfo.getRowMapper());
        }

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public int insert(final T entity) {
        if (entityInfo.isAutoIncrement()) {
            try {
                final KeyHolder key = new GeneratedKeyHolder();
                SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
                Object shardKey = entity.getClass().getField(entityInfo.getPrimaryKeyField()).get(entity);
                getNamedJdbcTemplateRW().update(entityInfo.getInsertSql(shardKey), ps, key);
                Field field = entity.getClass().getDeclaredField(entityInfo.getPrimaryKeyField());
                field.setAccessible(true);
                field.set(entity, CommonUtil.convert(key.getKey(), field.getType()));
                return 1;
            } catch (Exception e) {
                System.out.println(e);
                return 0;
            }

        } else {
            try {
                SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
                Object shardKey = entity.getClass().getField(entityInfo.getPrimaryKeyField()).get(entity);
                getNamedJdbcTemplateRW().update(entityInfo.getInsertSql(shardKey), ps);
                return 1;
            } catch (Exception e) {
                System.out.println(e);
                return 0;
            }
        }
    }

    public int update(final T entity) {
        try {
            SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
            Object shardKey = entity.getClass().getField(entityInfo.getPrimaryKeyField()).get(entity);
            getNamedJdbcTemplateRW().update(entityInfo.getUpdateSql(shardKey), ps);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int update(final T entity, String... columns) throws ColumnNotFoundException {
        if (columns == null || columns.length < 1) {
            return update(entity);
        }
        StringBuffer sb = new StringBuffer(128);
        Object shardKey;
        try {
            shardKey = entity.getClass().getField(entityInfo.getPrimaryKeyField()).get(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        sb.append("update ").append(entityInfo.getTable(shardKey)).append(" set ");
        for (int i = 0; i < columns.length; i++) {
            String field = entityInfo.getColumnFieldMap().get(columns[i]);
            if (field == null || CommonUtil.EMPTY_STRING.equals(field)) {
                ColumnNotFoundException exception = new ColumnNotFoundException("not found field ,column=" + columns[i]);
                throw exception;
            }
            sb.append(" ").append(columns[i]).append("=:").append(field).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(" where ").append(entityInfo.getFieldColumnMap().get(entityInfo.getPrimaryKeyField())).append("=:").append(entityInfo.getPrimaryKeyField());
        SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
        try {

            getNamedJdbcTemplateRW().update(sb.toString(), ps);
            return 1;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(final T entity) {

        try {
            SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
            Object shardKey = entity.getClass().getField(entityInfo.getPrimaryKeyField()).get(entity);
            getNamedJdbcTemplateRW().update(entityInfo.getDeleteSql(shardKey), ps);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int delete(final int primaryKey) {
        Map<String, Integer> params = new HashMap();
        params.put(entityInfo.getPrimaryKeyField(), primaryKey);
        try {
            getNamedJdbcTemplateRW().update(entityInfo.getDeleteSql(primaryKey), params);
            return 1;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*public List<T> list(List<Condition> conditionList,List<Order> orderList,Limit limit){
        SQLBuilder sqlBuilder = new SQLBuilder(entityInfo.getTable());
        sqlBuilder.addCondition(conditionList).addOrder(orderList).setLimit(limit);
        String sql = sqlBuilder.select();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
        if(entityInfo.isUseDataSourceRO()){
            namedParameterJdbcTemplate = getNamedJdbcTemplateRO();
        }else {
            namedParameterJdbcTemplate = getNamedJdbcTemplateRW();
        }
        List<T> list = namedParameterJdbcTemplate.query(sql,sqlBuilder.getParams(),entityInfo.getRowMapper());
        return list;
    }

    public void fillPage(List<Condition> conditionList,List<Order> orderList,Page<T> page){
        SQLBuilder sqlBuilder = new SQLBuilder(entityInfo.getTable());
        sqlBuilder.addCondition(conditionList).addOrder(orderList).setLimit(new Limit(page.getFirst(), page.getPageSize()));
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
        if(entityInfo.isUseDataSourceRO()){
            namedParameterJdbcTemplate = getNamedJdbcTemplateRO();
        }else {
            namedParameterJdbcTemplate = getNamedJdbcTemplateRW();
        }
        String sql = sqlBuilder.select();
        String countSql = sqlBuilder.count();
        int count = namedParameterJdbcTemplate.queryForInt(countSql, sqlBuilder.getParams());
        List<T> list = namedParameterJdbcTemplate.query(sql, sqlBuilder.getParams(), entityInfo.getRowMapper());
        page.setTotalCount(count);
        page.setResult(list);
    }*/

}

