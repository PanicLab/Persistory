package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.table.ColumnImage;
import com.paniclab.persistory.table.TableImage;

import java.util.Collection;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface ForeignKeyConstraint extends Constraint, ColumnConstraint {
    public TableImage getReferenceTable();
    public Collection<ColumnImage> getReferenceColumns();
}
