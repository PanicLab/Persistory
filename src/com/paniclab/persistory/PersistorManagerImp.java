package com.paniclab.persistory;

/**
 * Created by Сергей on 09.05.2017.
 */
public class PersistorManagerImp implements PersistorManager {
    private String vendor;
    private PersistorManager.Config config;

    public PersistorManagerImp() {
        super();
    }

    public PersistorManagerImp(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public PersistorManager.Config getConfig() {
        return config;
    }

    @Override
    public Dialect getDialect() {
        return null;
    }
}
