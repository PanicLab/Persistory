package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;

/**
 * Created by Сергей on 09.05.2017.
 */
public class NameDispatcher {
    private PersistorManager persistorManager;
    private Configuration configuration;

    public NameDispatcher(PersistorManager pm) {
        persistorManager = pm;
    }
}
