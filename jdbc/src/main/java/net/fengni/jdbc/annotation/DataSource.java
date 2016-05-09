package net.fengni.jdbc.annotation;

import net.fengni.jdbc.util.CommonUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fengni on 2015/11/3.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    public String dataSourceRW() default CommonUtil.DEFAULT_DATASOURCE_RW;//读写的的数据源

    public String dataSourceRO() default CommonUtil.EMPTY_STRING;//只读数据源

    public boolean useDataSourceRO() default false;//在BaseDao里是默认提供的方法是否查从库，如get(),list(),fillPage();
}
