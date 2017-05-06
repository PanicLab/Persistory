package com.paniclab.persistory.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Сергей on 27.04.2017.
 */
public class Configuration {
    public static final Path DEFAULT_CONFIG = Paths.get("res", "cfg", "default.cfg");

    Configuration() {
    }

    public String get(String property) {
        return "";
    }

    public void set(String property, String value) {

    }




    public static final String MODE = "MODE";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPING = "DEVELOPING";
    public static final String DEBUG = "DEBUG";

}
