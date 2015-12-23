package com.nifeng.jdbc.query;

import com.nifeng.jdbc.util.ApplicationContextUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by fengni on 2015/11/3.
 */
public class JdbcTemplateFactory {

    private static Map<String, JdbcTemplate> jdbcTemplateMap = new Hashtable<String, JdbcTemplate>();

    private static Map<String, NamedParameterJdbcTemplate> namedJdbcTemplateMap = new Hashtable<String, NamedParameterJdbcTemplate>();

    /**
     * 根据指定的“读写数据源”获取对应的JdbcTemplate
     *
     * @param dataSourceRW
     * @return JdbcTemplate
     */
    public static synchronized JdbcTemplate getJdbcTemplateRW(String dataSourceRW) {
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(dataSourceRW);
        if (jdbcTemplate == null) {
            DataSource dataSource = (DataSource) ApplicationContextUtil.getBean(dataSourceRW);
            jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplateMap.put(dataSourceRW, jdbcTemplate);
        }
        return jdbcTemplate;
    }

    /**
     * 根据指定的“只读数据源”获取对应的JdbcTemplate
     *
     * @param dataSourceRO
     * @return JdbcTemplate
     */
    public static synchronized JdbcTemplate getJdbcTemplateRO(String dataSourceRO) {
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(dataSourceRO);
        if (jdbcTemplate == null) {
            DataSource dataSource = (DataSource) ApplicationContextUtil.getBean(dataSourceRO);
            jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplateMap.put(dataSourceRO, jdbcTemplate);
        }
        return jdbcTemplate;
    }

    /**
     * 根据指定的“读写数据源”NamedParameterJdbcTemplate
     *
     * @param dataSourceRW
     * @return NamedParameterJdbcTemplate
     */
    public static synchronized NamedParameterJdbcTemplate getNamedJdbcTemplateRW(String dataSourceRW) {
        NamedParameterJdbcTemplate jdbcTemplate = namedJdbcTemplateMap.get(dataSourceRW);
        if (jdbcTemplate == null) {
            DataSource dataSource = (DataSource) ApplicationContextUtil.getBean(dataSourceRW);
            jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            namedJdbcTemplateMap.put(dataSourceRW, jdbcTemplate);
        }
        return jdbcTemplate;
    }

    /**
     * 根据指定的“只读数据源”NamedParameterJdbcTemplate
     *
     * @param dataSourceRO
     * @return NamedParameterJdbcTemplate
     */
    public static synchronized NamedParameterJdbcTemplate getNamedJdbcTemplateRO(String dataSourceRO) {
        NamedParameterJdbcTemplate jdbcTemplate = namedJdbcTemplateMap.get(dataSourceRO);
        if (jdbcTemplate == null) {
            DataSource dataSource = (DataSource) ApplicationContextUtil.getBean(dataSourceRO);
            jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            namedJdbcTemplateMap.put(dataSourceRO, jdbcTemplate);
        }
        return jdbcTemplate;
    }

}
