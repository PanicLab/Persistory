package com.paniclab.persistory.table.constraint;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface CheckConstraint extends Constraint {
    public String getExpression();
}
