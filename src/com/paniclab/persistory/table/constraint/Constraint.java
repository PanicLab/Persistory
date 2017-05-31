package com.paniclab.persistory.table.constraint;


import com.paniclab.persistory.table.TableImage;


/**
 * Created by Сергей on 09.05.2017.
 */
public interface Constraint {

    public static int UNIQUE = 100021;
    public static int PRIMARY_KEY = 100022;
    public static int FOREIGN_KEY = 100023;
    public static int CHECK = 100024;

    static ConstraintFactory builder() {
        return new ConstraintFactory();
    }
    public String getConstraintName();
    public TableImage getTable();
    public String getTableName();
    public int getType();
    public int getConstraintType();

    //public String getAlterTableExpression();

}
