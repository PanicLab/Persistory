package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.PersistorManagerImp;
import com.paniclab.persistory.NameDispatcher;
import com.paniclab.persistory.table.TableImage;

import static com.paniclab.persistory.Utils.isNot;


/**
 * Created by Сергей on 09.05.2017.
 */
public class ConstraintFactory {

    //TODO переделать создание NameDispatcher
    private NameDispatcher nameDispatcher = new NameDispatcher(new PersistorManagerImp());

    public ConstraintFactory() {}

    public UniqueConstraint getUniqueConstraint(TableImage table) {

        ConstraintImpl.Builder builder =
                new ConstraintImpl.Builder().setType(Constraint.UNIQUE)
                                            .setTable(table)
                                            .setConstraintName(nameDispatcher.getUniqueConstraintName(table))
                                            .setColumns(table.getUniqueColumns());
        return builder.build();
    }

    public UniqueConstraint getUniqueConstraint(ConstraintConstructingKit kit) {
        if(isNot(kit.isReady())) throw new InternalError("Невозможно создать ограничение. Шаблон полностью не " +
                "заполнен");

        ConstraintImpl.Builder builder;
        if(kit instanceof ConstraintImpl.Builder) {
            builder = ConstraintImpl.Builder.class.cast(kit);
        } else {
            builder = new ConstraintImpl.Builder();
        }

        if(builder.getType() != Constraint.UNIQUE) throw new InternalError("Невозможно создать unique constraint по " +
                "данному шаблону. Тип шаблона: " + builder.getType());

        return builder.build();
    }



    public CheckConstraint getCheckConstraint(TableImage table) {
        ConstraintImpl.Builder builder =
                new ConstraintImpl.Builder().setType(Constraint.UNIQUE)
                                            .setTable(table)
                                            .setConstraintName(nameDispatcher.getUniqueConstraintName(table))
                                            .setExpression(getCheckExpression(table));
        return builder.build();
    }

    //TODO дописать метод, выдернув выражение из аннотации
    private String getCheckExpression(TableImage table) {
        return "";
    }

    public PrimaryKeyConstraint getPrimaryKeyConstraint(TableImage table) {
        ConstraintImpl.Builder builder =
                new ConstraintImpl.Builder().setType(Constraint.PRIMARY_KEY)
                                            .setTable(table)
                                            .setConstraintName(
                                                    nameDispatcher.getPrimaryKeyConstraintName(table.getTableName()))
                                            .setColumns(table.getPrimaryKeyColumns());
        return builder.build();
    }

    public ForeignKeyConstraint getForeignKeyConstraint(TableImage table) {
        return null;
    }

    public ConstraintConstructingKit getConstructingKit() {
        return new ConstraintImpl.Builder();
    }
}
