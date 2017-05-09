package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.table.ColumnImage;

import java.util.Collection;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface ColumnConstraint {
    public Collection<ColumnImage> getColumns();
}
