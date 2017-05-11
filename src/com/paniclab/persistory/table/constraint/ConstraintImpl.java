package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.table.ColumnImage;
import com.paniclab.persistory.table.TableImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Сергей on 09.05.2017.
 */
public class ConstraintImpl implements UniqueConstraint, PrimaryKeyConstraint, ForeignKeyConstraint, CheckConstraint {

    private String name;
    private int type;
    private TableImage table;
    private String expression;
    private Collection<ColumnImage> columns;

    ConstraintImpl() {}

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getConstraintName() {
        return name;
    }

    @Override
    public TableImage getTable() {
        return table;
    }

    @Override
    public Collection<ColumnImage> getColumns() {
        return Collections.unmodifiableCollection(columns);
    }

    @Override
    public TableImage getReferenceTable() {
        return null;
    }

    @Override
    public Collection<ColumnImage> getReferenceColumns() {
        return null;
    }

    @Override
    public String getExpression() {
        return null;
    }


    void setType(int t) {
        switch (t) {
            case Constraint.CHECK:
            case Constraint.PRIMARY_KEY:
            case Constraint.FOREIGN_KEY:
            case Constraint.UNIQUE:
                this.type = t;
                break;
            default:
                throw new InternalError("Ошибка при создании объекта Constraint. Неизвестный тип ограничения: " + t);
        }
    }

    void setTable(TableImage t) {
        this.table = t;
    }

    void setConstraintName(String n) {
        this.name = n;
    }

    void setColumns(Collection<ColumnImage> cols) {
        columns = new ArrayList<>();
        columns.addAll(cols);
    }
}
