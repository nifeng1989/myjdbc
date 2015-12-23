package com.nifeng.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengni on 2015/11/3.
 */
public class EntityHelper {
    private static final Map<String, AbstractEntityInfo> entityMap = new HashMap<String, AbstractEntityInfo>();

    protected static AbstractEntityInfo getEntityInfo(String clazz) {
        return entityMap.get(clazz);
    }

    protected static AbstractEntityInfo setEntityInfo(String clazz, AbstractEntityInfo entityInfo) {
        return entityMap.put(clazz, entityInfo);
    }


}
