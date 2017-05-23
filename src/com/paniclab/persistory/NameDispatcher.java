package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;
import com.paniclab.persistory.table.TableImage;

/**
 * Created by Сергей on 09.05.2017.
 */
public class NameDispatcher {
    private PersistorManager persistorManager;
    private Configuration configuration;

    public NameDispatcher(PersistorManager pm) {
        persistorManager = pm;
        configuration = Configuration.getCurrent();
    }

    public String getTableName(Class<?> clazz) {
        String prefix = configuration.get("TABLE_NAME_PREFIX");
        String suffix = configuration.get("TABLE_NAME_SUFFIX");
        String subname = clazz.getSimpleName();
        return (prefix + subname + suffix).toUpperCase();
    }

    public String getUniqueConstraintName(String tableName) {
        return tableName.toLowerCase() + "_unique_constraint";
    }

    public String getUniqueConstraintName(TableImage table) {
        return table.getTableName().toLowerCase() + "_unique_constraint";
    }
}
