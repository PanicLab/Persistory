package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Сергей on 27.04.2017.
 */
public class Configuration {
    public static final Path DEFAULT_CONFIG = Paths.get("res", "cfg", "default.cfg");
    private Map<String, String> properties = new HashMap<>();

    Configuration() {
    }

    public String get(String property) {
        String value = properties.get(property);
        if(value == null) throw new InternalError("Запрашиваемый параметр конфигурации " + property + " не существует");
        return value;
    }

    public void set(String property, String value) {
        properties.put(property, value);
    }


    public static final String MODE = "MODE";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPING = "DEVELOPING";
    public static final String DEBUG = "DEBUG";

}
