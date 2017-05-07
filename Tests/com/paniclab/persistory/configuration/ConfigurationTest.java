package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Сергей on 07.05.2017.
 */
public class ConfigurationTest {

    private Configuration cfg;

    @Before
    public void setUp() throws Exception {
        cfg = new ConfigurationFactory().getDefault().create();
        assertNotNull(cfg);
    }

    @After
    public void tearDown() throws Exception {
        cfg = null;
    }

    @Test
    public void getConfiguration() throws Exception {
        assertEquals(Configuration.PRODUCTION, cfg.get(Configuration.MODE));

        cfg.set(Configuration.MODE, Configuration.DEVELOPING);
        assertEquals(Configuration.DEVELOPING, cfg.get(Configuration.MODE));
    }

    @Test(expected = InternalError.class)
    public void getConfiguration_that_is_not_exist() throws Exception {
        String value = cfg.get("XXXX");
    }

}