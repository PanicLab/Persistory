package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.table.ColumnImage;
import com.paniclab.persistory.table.TableImage;

import java.util.Collection;

/**
 * Created by Сергей on 27.05.2017.
 */
public interface ConstraintConstructingKit {
    ConstraintConstructingKit setTable(TableImage t);
    TableImage getTable();

    ConstraintConstructingKit setTableName(String name);
    String getTableName();

    ConstraintConstructingKit setConstraintName(String n);
    String getConstraintName();

    ConstraintConstructingKit setType(int t);
    int getType();

    ConstraintConstructingKit setColumns(Collection<ColumnImage> cols);
    ConstraintConstructingKit setColumn(ColumnImage column);
    Collection<ColumnImage> getColumns();

    ConstraintConstructingKit setExpression(String expression);
    String getExpression();

    ConstraintConstructingKit setReferenceTable(TableImage table);
    TableImage getReferenceTable();

    ConstraintConstructingKit setReferenceColumns(Collection<ColumnImage> cols);
    ConstraintConstructingKit setReferenceColumn(ColumnImage col);
    Collection<ColumnImage> getReferenceColumns();

    boolean isReady();
}
