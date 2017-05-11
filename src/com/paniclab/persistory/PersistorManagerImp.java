package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;
import com.paniclab.persistory.configuration.ConfigurationImpl;

/**
 * Created by Сергей on 09.05.2017.
 */
public class PersistorManagerImp implements PersistorManager {
    private String vendor;
    private Configuration config;

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
    public Configuration getConfig() {
        return config;
    }

    @Override
    public Dialect getDialect() {
        return null;
    }
}
