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

    public ConstraintFactory() {}

    public ConstraintConstructingKit getConstructingKit() {
        return new ConstraintImpl.Builder();
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
}
