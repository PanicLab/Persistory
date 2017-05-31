package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.InternalError;

import static com.paniclab.persistory.Utils.isNot;


/**
 * Created by Сергей on 09.05.2017.
 */
public class ConstraintFactory {

    public ConstraintFactory() {}

    public ConstraintBuilder getConstraintBuilder() {
        return new ConstraintImpl.Builder();
    }


    public UniqueConstraint getUniqueConstraint(ConstraintBuilder builder) {
        if(isNot(builder.isReady())) throw new InternalError("Невозможно создать ограничение. Шаблон полностью не " +
                "заполнен");
        if(builder.getType() != Constraint.UNIQUE) throw new InternalError("Невозможно создать unique constraint по " +
                "данному шаблону. Тип шаблона: " + builder.getType());

        return (UniqueConstraint)builder.build();
    }


    public ForeignKeyConstraint getForeignKeyConstraint(ConstraintBuilder builder) {
        if(isNot(builder.isReady())) throw new InternalError("Невозможно создать ограничение. Шаблон полностью не " +
                "заполнен");
        if(builder.getType() != Constraint.FOREIGN_KEY) throw new InternalError("Невозможно создать foreign key " +
                "constraint по данному шаблону. Тип шаблона: " + builder.getType());
        return (ForeignKeyConstraint)builder.build();
    }


    public PrimaryKeyConstraint getPrimaryKeyConstraint(ConstraintBuilder builder) {
        if(isNot(builder.isReady())) throw new InternalError("Невозможно создать ограничение. Шаблон полностью не " +
                "заполнен");
        if(builder.getType() != Constraint.PRIMARY_KEY) throw new InternalError("Невозможно создать primary key " +
                "constraint по данному шаблону. Тип шаблона: " + builder.getType());
        return (PrimaryKeyConstraint)builder.build();
    }

    public CheckConstraint getCheckConstraint(ConstraintBuilder builder) {
        if(isNot(builder.isReady())) throw new InternalError("Невозможно создать ограничение. Шаблон полностью не " +
                "заполнен");
        if(builder.getType() != Constraint.CHECK) throw new InternalError("Невозможно создать check constraint" +
                " по данному шаблону. Тип шаблона: " + builder.getType());
        return (CheckConstraint) builder.build();
    }
}
