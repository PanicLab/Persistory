package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация интерфейса Configuration. Экземпляр класса является неизменяемым, его использование безопасно в
 * многопоточной среде.
 */
final class ConfigurationImpl implements Configuration {
    private final Map<String, String> properties;

    private ConfigurationImpl() {
        throw new InternalError("Создание экземпляра подобным способом запрещено.");
    }

    private ConfigurationImpl(ConfigurationImpl.Builder builder) {
        this.properties = builder.properties;
        builder.properties = null;
    }

    /**
     * Метод извлекает значение параметра конфигурации, передаваемого в качестве аргумента.
     * @param property: один из параметров конфигурации.
     * @return: значение параметра конфигурации. Если параметр конфигурации не существует, возбуждается исключение
     * com.paniclab.persistory.InternalError;
     */
    @Override
    public String get(String property) {
        String value = properties.get(property);
        if(value == null) throw new InternalError("Запрашиваемый параметр конфигурации " + property + " не существует");
        return value;
    }


    final static class Builder {

        private Map<String, String> properties = new HashMap<>();

        Builder() {}


        /**
         * Метод задает параметр конфигурации и его значение.
         * @param property: один из параметров конфигурации.
         * @param value: значение параметра конфигурации
         */
        void set(String property, String value) {
            properties.put(property,value);
        }


        //TODO не реализовано
        void add(String property, String value) {
        }

        Configuration create() {
            return new ConfigurationImpl(this);
        }
    }
}
