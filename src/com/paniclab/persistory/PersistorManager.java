package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface PersistorManager {
    String H2 = "H2";
    String SQLITE = "SQLITE";


    String getVendor();
    public Configuration getConfig();
    Dialect getDialect();
}
