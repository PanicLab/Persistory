package com.paniclab.persistory.table;

import java.util.Collection;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface TableImage {
    public String getTableName();
    public Class<?> getCorrespondingClass() throws Exception;

    public Collection<ColumnImage> getUniqueColumns();
}
