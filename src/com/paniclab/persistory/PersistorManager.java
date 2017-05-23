package com.paniclab.persistory;

/**
 * Created by Сергей on 09.05.2017.
 */
public interface PersistorManager {
    String H2 = "H2";
    String SQLITE = "SQLITE";


    String getVendor();
    public Config getConfig();
    Dialect getDialect();

    public interface Config {}
}
