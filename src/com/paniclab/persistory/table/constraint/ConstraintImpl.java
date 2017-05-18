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


    ConstraintImpl(Embryo embryo) {
        this.name = embryo.name;
        this.type = embryo.type;
        this.table = embryo.table;
        this.expression = embryo.expression;
        this.columns = embryo.columns;
        this.referenceTable = embryo.referenceTable;
        this.referenceColumns = embryo.referenceColumns;
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

    static class Embryo {

        private String name;
        private int type;
        private TableImage table;
        private String expression;
        private Collection<ColumnImage> columns;
        private TableImage referenceTable;
        private Collection<ColumnImage> referenceColumns;

        Embryo() {}

        Embryo setTable(TableImage t) {
            this.table = t;
            return this;
        }

        Embryo setConstraintName(String n) {
            this.name = n;
            return this;
        }

        Embryo setType(int t) {
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

        Embryo setColumns(Collection<ColumnImage> cols) {
            if (columns == null) columns = new ArrayList<>();
            columns.addAll(cols);
            return this;
        }

        Embryo setColumn(ColumnImage column) {
            if (columns == null) columns = new ArrayList<>();
            columns.add(column);
            return this;
        }

        Embryo setExpression(String expression) {
            this.expression = expression;
            return this;
        }

        Embryo setReferenceTable(TableImage table) {
            this.referenceTable = table;
            return this;
        }

        Embryo setReferenceColumns(Collection<ColumnImage> cols) {
            if(referenceColumns == null) referenceColumns = new ArrayList<>();
            referenceColumns.addAll(cols);
            return this;
        }

        Embryo setReferenceColumn(ColumnImage col) {
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
