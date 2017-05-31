package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.table.ColumnImage;
import com.paniclab.persistory.table.TableImage;

import java.util.Collection;

/**
 * Created by Сергей on 27.05.2017.
 */
public interface ConstraintBuilder {
    ConstraintBuilder setTable(TableImage t);
    TableImage getTable();

    ConstraintBuilder setTableName(String name);
    String getTableName();

    ConstraintBuilder setConstraintName(String n);
    String getConstraintName();

    ConstraintBuilder setType(int t);
    int getType();

    ConstraintBuilder setColumns(Collection<ColumnImage> cols);
    ConstraintBuilder setColumn(ColumnImage column);
    Collection<ColumnImage> getColumns();

    ConstraintBuilder setExpression(String expression);
    String getExpression();

    ConstraintBuilder setReferenceTable(TableImage table);
    TableImage getReferenceTable();

    ConstraintBuilder setReferenceColumns(Collection<ColumnImage> cols);
    ConstraintBuilder setReferenceColumn(ColumnImage col);
    Collection<ColumnImage> getReferenceColumns();

    boolean isReady();

    Constraint build();
}
