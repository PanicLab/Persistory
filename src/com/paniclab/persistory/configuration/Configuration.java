package com.paniclab.persistory.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;


public interface Configuration {
    Path DEFAULT_CONFIG = Paths.get("res", "cfg", "default.cfg");

    String MODE = "MODE";
    String PRODUCTION = "PRODUCTION";
    String DEVELOPING = "DEVELOPING";
    String DEBUG = "DEBUG";

    String COL_NAME_PREFIX = "COL_NAME_PREFIX";
    String COL_NAME_SUFFIX = "COL_NAME_SUFFIX";
    String TABLE_NAME_PREFIX = "TABLE_NAME_PREFIX";
    String TABLE_NAME_SUFFIX = "TABLE_NAME_SUFFIX";

    String get(String property);

    static ConfigurationBuilder builder() {
        return new ConfigurationBuilder().getDefault();
    }
}
