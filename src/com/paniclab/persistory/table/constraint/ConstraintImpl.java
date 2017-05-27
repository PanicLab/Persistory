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
    private String tableName;
    private String expression;
    private Collection<ColumnImage> columns;
    private TableImage referenceTable;
    private Collection<ColumnImage> referenceColumns;


    private ConstraintImpl() {}


    private ConstraintImpl(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.table = builder.table;
        this.tableName = builder.tableName;
        this.expression = builder.expression;
        this.columns = Collections.unmodifiableCollection(builder.columns);
        this.referenceTable = builder.referenceTable;
        this.referenceColumns = Collections.unmodifiableCollection(builder.referenceColumns);
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
    public String getTableName() {
        return tableName;
    }


    @Override
    public Collection<ColumnImage> getColumns() {
        return columns;
    }

    @Override
    public TableImage getReferenceTable() {
        return referenceTable;
    }

    @Override
    public Collection<ColumnImage> getReferenceColumns() {
        return referenceColumns;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    static class Builder implements ConstraintConstructingKit {

        private String name;
        private int type;
        private TableImage table;
        private String tableName;
        private String expression;
        private Collection<ColumnImage> columns;
        private TableImage referenceTable;
        private Collection<ColumnImage> referenceColumns;

        //TODO добавить присвоение прокси-таблицы полю table по-умолчанию
        Builder() {}

        @Override
        public Builder setTable(TableImage t) {
            this.table = t;
            return this;
        }

        @Override
        public  TableImage getTable() {
            return table;
        }

        @Override
        public Builder setTableName(String name) {
            this.tableName = name;
            return this;
        }

        @Override
        public String getTableName() {
            return tableName;
        }

        @Override
        public Builder setConstraintName(String n) {
            this.name = n;
            return this;
        }

        @Override
        public String getConstraintName() {
            return name;
        }

        @Override
        public Builder setType(int t) {
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

        @Override
        public int getType() {
            return type;
        }

        @Override
        public Builder setColumns(Collection<ColumnImage> cols) {
            if (columns == null) columns = new ArrayList<>();
            columns.addAll(cols);
            return this;
        }

        @Override
        public Builder setColumn(ColumnImage column) {
            if (columns == null) columns = new ArrayList<>();
            columns.add(column);
            return this;
        }

        @Override
        public Collection<ColumnImage> getColumns() {
            return columns;
        }

        @Override
        public Builder setExpression(String expression) {
            this.expression = expression;
            return this;
        }

        @Override
        public String getExpression() {
            return expression;
        }

        @Override
        public Builder setReferenceTable(TableImage table) {
            this.referenceTable = table;
            return this;
        }

        @Override
        public TableImage getReferenceTable() {
            return referenceTable;
        }

        @Override
        public Builder setReferenceColumns(Collection<ColumnImage> cols) {
            if(referenceColumns == null) referenceColumns = new ArrayList<>();
            referenceColumns.addAll(cols);
            return this;
        }

        @Override
        public Builder setReferenceColumn(ColumnImage col) {
            if(referenceColumns == null) referenceColumns = new ArrayList<>();
            referenceColumns.add(col);
            return this;
        }

        @Override
        public Collection<ColumnImage> getReferenceColumns() {
            return referenceColumns;
        }

        @Override
        public boolean isReady() {
            if (isNot(isAssign(type))) return false;
            if (name == null || name.isEmpty()) return false;
            if (tableName == null || tableName.isEmpty()) return false;
            if (table == null) return false;
            switch (type) {
                case Constraint.CHECK:
                    return isNot((expression == null || expression.equals("")));
                case Constraint.UNIQUE:
                case Constraint.PRIMARY_KEY:
                    return isNot(columns == null || columns.isEmpty());
                case Constraint.FOREIGN_KEY:
                    if(columns == null || columns.isEmpty()) return false;
                    if(referenceColumns == null || referenceColumns.isEmpty()) return false;
                    return referenceTable != null;
                default:
                    throw new InternalError("Ошибка при создании объекта Constraint. Неизвестный тип ограничения: "
                            + type);
            }
        }

        private boolean isAssign(int type) {
            if (type == Constraint.UNIQUE) return true;
            if (type == Constraint.CHECK) return true;
            if (type == Constraint.PRIMARY_KEY) return true;
            if (type == Constraint.FOREIGN_KEY) return true;
            return false;
        }

        ConstraintImpl build() {
            return new ConstraintImpl(this);
        }
    }

}
