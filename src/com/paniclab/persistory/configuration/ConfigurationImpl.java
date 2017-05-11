package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

import java.util.HashMap;
import java.util.Map;

/**
 * Экземпляр данного класса инкапсулирует настройки системы в целом. Предполагается, что настройки будут различными для
 * каждой СУБД, при этом базироваться они будут на неких общих, свойственной системе в целом, настройках. Предполагается,
 * что будет существовать лишь один экземпляр класса для каждой СУБД.
 */
public final class ConfigurationImpl implements Configuration {
    private final Map<String, String> properties;

    private ConfigurationImpl() {
        throw new InternalError("Создание экземпляра подобным способом запрещено.");
    }

    ConfigurationImpl(ConfigHelper helper) {
        this.properties = helper.properties;
        helper.properties = null;
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


    static class ConfigHelper {
        private Map<String, String> properties = new HashMap<>();

        ConfigHelper() {
        }

        void set(String property, String value) {
            properties.put(property, value);
        }
    }
}
