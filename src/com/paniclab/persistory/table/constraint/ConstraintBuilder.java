package com.paniclab.persistory.table.constraint;

import com.paniclab.persistory.PersistorManagerImp;
import com.paniclab.persistory.NameDispatcher;
import com.paniclab.persistory.table.TableImage;


/**
 * Created by Сергей on 09.05.2017.
 */
public class ConstraintBuilder {

    //TODO переделать создание NameDispatcher
    private NameDispatcher nameDispatcher = new NameDispatcher(new PersistorManagerImp());

    public ConstraintBuilder() {}

    public UniqueConstraint getUniqueConstraint(TableImage table) {

        ConstraintImpl.Chrysalis chrysalis =
                new ConstraintImpl.Chrysalis().setType(Constraint.UNIQUE)
                                            .setTable(table)
                                            .setConstraintName(nameDispatcher.getUniqueConstraintName(table))
                                            .setColumns(table.getUniqueColumns());
        return new ConstraintImpl(chrysalis);
    }

    public CheckConstraint getCheckConstraint(TableImage table) {
        ConstraintImpl.Chrysalis chrysalis =
                new ConstraintImpl.Chrysalis().setType(Constraint.UNIQUE)
                                            .setTable(table)
                                            .setConstraintName(nameDispatcher.getUniqueConstraintName(table))
                                            .setExpression(getCheckExpression(table));
        return new ConstraintImpl(chrysalis);
    }

    //TODO дописать метод, выдернув выражение из аннотации
    private String getCheckExpression(TableImage table) {
        return "";
    }
}
