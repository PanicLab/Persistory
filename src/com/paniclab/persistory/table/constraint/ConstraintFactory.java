package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.PersistorManagerImp;
import com.paniclab.persistory.NameDispatcher;
import com.paniclab.persistory.table.TableImage;


/**
 * Created by Сергей on 09.05.2017.
 */
public class ConstraintFactory {

    private ConstraintImpl constraint = new ConstraintImpl();
    //TODO переделать создание NameDispatcher
    private NameDispatcher nameDispatcher = new NameDispatcher(new PersistorManagerImp());

    public ConstraintFactory() {}

    public UniqueConstraint getUniqueConstraint(TableImage table) {
        constraint.setType(Constraint.UNIQUE);
        constraint.setTable(table);
        String name = nameDispatcher.getUniqueConstraintName(table);
        constraint.setConstraintName(name);
        constraint.setColumns(table.getUniqueColumns());

        return UniqueConstraint.class.cast(constraint);
    }
}
