//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.fengni.jdbc;

import net.fengni.jdbc.util.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EntityRowMapper<T> implements RowMapper<T> {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private Class<T> mappedClass;
    private Map<String, PropertyDescriptor> mappedFields;

    public EntityRowMapper() {
    }

    public EntityRowMapper(Class<T> mappedClass) {
        this.initialize(mappedClass);
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            this.initialize(mappedClass);
        } else if (!this.mappedClass.equals(mappedClass)) {
            throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + mappedClass + " since it is already providing mapping for " + this.mappedClass);
        }

    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(pd.getName(), pd);

            }
        }

    }


    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        AbstractEntityInfo entityInfo = EntityHelper.getEntityInfo(mappedClass.getName());
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        this.initBeanWrapper(bw);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int index = 1; index <= columnCount; ++index) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String fieldName = entityInfo.getColumnFieldMap().get(column);
            if (fieldName == null || CommonUtil.EMPTY_STRING.equals(fieldName)) {
                continue;
            }
            PropertyDescriptor pd = this.mappedFields.get(fieldName);
            if (pd != null) {
                try {
                    Object ex = this.getColumnValue(rs, index, pd);
                    if (this.logger.isDebugEnabled() && rowNumber == 0) {
                        this.logger.debug("Mapping column \'" + column + "\' to property \'" + pd.getName() + "\' of type " + pd.getPropertyType());
                    }

                    try {
                        bw.setPropertyValue(pd.getName(), ex);
                    } catch (TypeMismatchException var13) {
                        if (ex != null) {
                            throw var13;
                        }

                        this.logger.debug("Intercepted TypeMismatchException for row " + rowNumber + " and column \'" + column + "\' with value " + ex + " when setting property \'" + pd.getName() + "\' of type " + pd.getPropertyType() + " on object: " + mappedObject);
                    }

                } catch (NotWritablePropertyException var14) {
                    throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), var14);
                }
            }
        }
        return mappedObject;
    }

    protected void initBeanWrapper(BeanWrapper bw) {
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    public static <T> EntityRowMapper<T> newInstance(Class<T> mappedClass) {
        EntityRowMapper newInstance = new EntityRowMapper();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }
}
