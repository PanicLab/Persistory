package com.paniclab.persistory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Сергей on 11.05.2017.
 */
public class Seed<T> {

    private final Class<T> subject;
    private final Map<String, Object> data = new HashMap<>();
    private boolean isPollinated;

    //private Seed() {}
    public Seed(Class<T> clazz) {
        this.subject = clazz;
    }

    @SuppressWarnings("unchecked")
    public Seed(T obj) {
        this.subject = (Class<T>) obj.getClass();
        //this.subject = (Class)((ParameterizedType)obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getSubject() {
        return subject;
    }

    private void populate() {
        for(Field field: subject.getDeclaredFields()) {
            data.put(field.getName(), null);
        }
    }
}
