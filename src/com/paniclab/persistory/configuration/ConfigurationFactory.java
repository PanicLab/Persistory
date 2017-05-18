package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

/**
 * Created by Сергей on 13.05.2017.
 */
public class ConfigurationFactory {

    private ConfigurationImpl.Builder builder;
    private Secretary secretary = new Secretary();
    private static boolean alreadyCreated = false;

    public ConfigurationFactory() {
        builder = secretary.getDefaults();
    }


    public ConfigurationFactory set(String property, String value) {
        if(property.equals(Configuration.MODE)) return setApplicationMode(value);
        builder.set(property,value);
        return this;
    }

    public ConfigurationFactory setApplicationMode(String value) {
        switch (value) {
            case Configuration.PRODUCTION:
            case Configuration.DEVELOPING:
            case Configuration.DEBUG:
                builder.set(Configuration.MODE, value);
                break;
            default:
                throw new InternalError("Неизвестный режим запуска преложения: " + value);
        }
        return this;
    }

    public ConfigurationFactory add(String property, String value) {
        builder.add(property, value);
        return this;
    }

    public Configuration create() {
        secretary.saveCurrent();
        alreadyCreated = true;
        return builder.build();
    }

    public Configuration getCurrent() {
        if(!alreadyCreated) throw new InternalError("Нельзя получить текущие настройки, не создав их");
        return secretary.loadCurrent().build();
    }
}
