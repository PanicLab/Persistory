package com.paniclab.persistory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Сергей on 07.05.2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JoinForeignKeyColumn {
    String columnName() default "";
    String constraintName() default "";
    boolean notNull() default false;
    boolean unique() default false;
    int onUpdate() default OnUpdate.NO_ACTION;
    int onDelete() default OnDelete.NO_ACTION;
    //boolean inUse() default true;
}
