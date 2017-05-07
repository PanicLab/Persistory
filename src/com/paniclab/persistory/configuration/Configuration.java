package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Экземпляр данного класса инкапсулирует настройки системы в целом. Предполагается, что настройки будут различными для
 * каждой СУБД, при этом базироваться они будут на неких общих, свойственной системе в целом, настройках. Предполагается,
 * что будет существовать лишь один экземпляр класса для каждой СУБД.
 */
public class Configuration {
    public static final Path DEFAULT_CONFIG = Paths.get("res", "cfg", "default.cfg");
    private Map<String, String> properties = new HashMap<>();

    Configuration() {
    }

    /**
     * Метод извлекает значение параметра конфигурации, передаваемого в качестве аргумента.
     * @param property: один из параметров конфигурации.
     * @return: значение параметра конфигурации. Если параметр конфигурации не существует, возбуждается исключение
     * com.paniclab.persistory.InternalError;
     */
    public String get(String property) {
        String value = properties.get(property);
        if(value == null) throw new InternalError("Запрашиваемый параметр конфигурации " + property + " не существует");
        return value;
    }

    /**
     * Метод задает параметр конфигурации и его значение.
     * @param property: один из параметров конфигурации.
     * @param value: значение параметра конфигурации
     */
    void set(String property, String value) {
        properties.put(property, value);
    }


    public static final String MODE = "MODE";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPING = "DEVELOPING";
    public static final String DEBUG = "DEBUG";

}
