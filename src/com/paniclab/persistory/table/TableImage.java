package com.paniclab.persistory.table;


import java.util.Collection;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface TableImage {
    public String getTableName();
    public Class<?> getCorrespondingClass() throws Exception;
    boolean hasParent(); //таблица связана с другой посредством внешнего ключа или промежуточной таблицы
    public Collection<ColumnImage> getUniqueColumns();
    public Collection<ColumnImage> getPrimaryKeyColumns();
}
