package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.table.TableImage;


/**
 * Created by Сергей on 09.05.2017.
 */
public interface Constraint {
    public String getConstraintName();
    public TableImage getTable();
    public String getAlterTableExpression();
}
