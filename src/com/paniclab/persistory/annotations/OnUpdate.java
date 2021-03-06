package com.paniclab.persistory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Сергей on 07.05.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OnUpdate {
    int NO_ACTION = 10010;
    int CASCADE = 10011;
    int SET_DEFAULT = 10012;
    int SET_NULL = 10013;

    int value();
}
