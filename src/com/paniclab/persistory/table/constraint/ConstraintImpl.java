package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.table.ColumnImage;
import com.paniclab.persistory.table.TableImage;


import java.util.*;

import static com.paniclab.persistory.Utils.isNot;

/**
 * Created by Сергей on 09.05.2017.
 */

public class ConstraintImpl implements UniqueConstraint, PrimaryKeyConstraint, ForeignKeyConstraint, CheckConstraint {

    private String name;
    private int type;
    private TableImage table;
    private String expression;
    private Collection<ColumnImage> columns;
    private TableImage referenceTable;
    private Collection<ColumnImage> referenceColumns;


    private ConstraintImpl() {}


    ConstraintImpl(Chrysalis chrysalis) {
        this.name = chrysalis.name;
        this.type = chrysalis.type;
        this.table = chrysalis.table;
        this.expression = chrysalis.expression;
        this.columns = chrysalis.columns;
        this.referenceTable = chrysalis.referenceTable;
        this.referenceColumns = chrysalis.referenceColumns;
    }

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
        return referenceTable;
    }

    @Override
    public Collection<ColumnImage> getReferenceColumns() {
        return Collections.unmodifiableCollection(referenceColumns);
    }

    @Override
    public String getExpression() {
        return expression;
    }

    static class Chrysalis {

        private String name;
        private int type;
        private TableImage table;
        private String expression;
        private Collection<ColumnImage> columns;
        private TableImage referenceTable;
        private Collection<ColumnImage> referenceColumns;

        Chrysalis() {}

        Chrysalis setTable(TableImage t) {
            this.table = t;
            return this;
        }

        Chrysalis setConstraintName(String n) {
            this.name = n;
            return this;
        }

        Chrysalis setType(int t) {
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
            return this;
        }

        Chrysalis setColumns(Collection<ColumnImage> cols) {
            if (columns == null) columns = new ArrayList<>();
            columns.addAll(cols);
            return this;
        }

        Chrysalis setColumn(ColumnImage column) {
            if (columns == null) columns = new ArrayList<>();
            columns.add(column);
            return this;
        }

        Chrysalis setExpression(String expression) {
            this.expression = expression;
            return this;
        }

        Chrysalis setReferenceTable(TableImage table) {
            this.referenceTable = table;
            return this;
        }

        Chrysalis setReferenceColumns(Collection<ColumnImage> cols) {
            if(referenceColumns == null) referenceColumns = new ArrayList<>();
            referenceColumns.addAll(cols);
            return this;
        }

        Chrysalis setReferenceColumn(ColumnImage col) {
            if(referenceColumns == null) referenceColumns = new ArrayList<>();
            referenceColumns.add(col);
            return this;
        }

        public boolean isMature() {
            if (isNot(isAssign(type))) return false;
            if (name == null || name.equals("")) return false;
            if (table == null) return false;
            switch (type) {
                case Constraint.CHECK:
                    return isNot((expression == null || expression.equals("")));
                case Constraint.UNIQUE:
                case Constraint.PRIMARY_KEY:
                    return isNot(columns == null || columns.isEmpty());
                case Constraint.FOREIGN_KEY:
                    break;
                default:
                    throw new InternalError("Ошибка при создании объекта Constraint. Неизвестный тип ограничения: "
                            + type);
            }
            return false;
        }

        private boolean isAssign(int type) {
            if (type == Constraint.UNIQUE) return true;
            if (type == Constraint.CHECK) return true;
            if (type == Constraint.PRIMARY_KEY) return true;
            if (type == Constraint.FOREIGN_KEY) return true;
            return false;
        }
    }

}
