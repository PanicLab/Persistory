package com.paniclab.persistory.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Интерфейс представляет объект, инкапсулирующий в себе конфигурацию системы. Для создания объекта конфигурации
 * используется следующий синтаксис:
 *
 * 	Configuration cfg = Configuration.builder()
 * 	                                    .set(Configuration.MODE, Configuration.DEBUG)
 * 	                                    .set(Configuration.DEFAULT_DBMS, PersistorManager.H2);
 * 	                                    .add(Configuration.DOMAIN_PACKAGE, "com.github.paniclab.coolApp.Models")
 * 	                                    .add(Configuration.CLASS_TO_PERSIST, "com.github.paniclab.coolApp.Models.jet")
 * 	                                    .create();
 *
 * 	Альтернативный вариант синтаксиса:
 *
 * 	Configuration another = new ConfigurationFactory.set(Configuration.MODE, Configuration.DEBUG)
 * 	                                                .set(Configuration.DEFAULT_DBMS, PersistorManager.H2)
 * 	                                                .create();
 *
 * 	Создание конфигурации опирается на конфигурационный файл, содержащий в себе настройки по умолчанию. В процессе
 * 	создания экземпляра типа Configuration параметры могут быть добавлены (или переопределены) программно.
 */

public interface Configuration {
    Path DEFAULT_CONFIG_PATH = Paths.get("res", "cfg", "default.cfg");

    String MODE = "MODE";
    String PRODUCTION = "PRODUCTION";
    String DEVELOPING = "DEVELOPING";
    String DEBUG = "DEBUG";

    String COL_NAME_PREFIX = "COL_NAME_PREFIX";
    String COL_NAME_SUFFIX = "COL_NAME_SUFFIX";
    String TABLE_NAME_PREFIX = "TABLE_NAME_PREFIX";
    String TABLE_NAME_SUFFIX = "TABLE_NAME_SUFFIX";

    String get(String property);

    static ConfigurationFactory builder() {
        return new ConfigurationFactory();
    }
}
