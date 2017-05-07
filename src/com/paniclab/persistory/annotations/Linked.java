package com.paniclab.persistory.annotations;

import java.lang.annotation.*;

/**
 * Аннотация класса, используемая совместно с @Table. Обозначает связь таблицы класса с другой таблицей. Может быть
 * использована несколько раз. Тип связи по умолчанию UNIDIRECTIONAL. В случае, если тип связи BIDIRECTIONAL, с
 * противоположной стороны должна присутствовать аннотация Linked. По умолчанию используется промежуточная таблица,
 * однако данное поведение может быть изменено соответствующей аннотацией. Параметры промежуточной таблицы по умолчанию
 * также могут быть изменены соответствующей аннотацией.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LinkedHelper.class)
public @interface Linked {
    int ONE_TO_ONE = 10001;
    int ONE_TO_MANY = 10002;
    int MANY_TO_ONE = 10003;
    int MANY_TO_MANY = 10004;
    int UNIDIRECTIONAL = 10005;
    int BIDIRECTIONAL = 10006;

    Class<?> reference();
    int assotiation();
    int type()default UNIDIRECTIONAL;
}
