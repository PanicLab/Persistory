package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

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

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof ConfigurationImpl)) return false;
        ConfigurationImpl other = ConfigurationImpl.class.cast(obj);
        return this.properties.equals(other.properties);
    }


    final static class Builder {

        private final Map<String, String> properties;

        Builder(Map<String, String> map) {
            this.properties = map;
        }


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

        Configuration build() {
            return new ConfigurationImpl(this);
        }
    }
}
