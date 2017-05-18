package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Сергей on 07.05.2017.
 */
public class ConfigurationTest {

    private Configuration cfg;

    @Before
    public void setUp() throws Exception {
        //cfg = Configuration.builder().create();
        //assertNotNull(cfg);
    }

    @After
    public void tearDown() throws Exception {
        //cfg = null;
    }

    @Test
    public void getConfiguration() throws Exception {
        cfg = Configuration.builder().create();
        assertEquals(Configuration.PRODUCTION, cfg.get(Configuration.MODE));

        cfg = Configuration.builder()
                .set(Configuration.MODE, Configuration.DEVELOPING)
                .create();
        assertEquals(Configuration.DEVELOPING, cfg.get(Configuration.MODE));
    }

    @Test(expected = InternalError.class)
    public void getConfiguration_that_is_not_exist() throws Exception {
        cfg = Configuration.builder().create();
        String value = cfg.get("XXXX");
    }

    @Test
    public void config_equals() throws Exception {
        cfg = Configuration.builder().setApplicationMode(Configuration.DEBUG).create();

        Configuration other = Configuration.builder().create();
        assertNotEquals(cfg, other);

        Configuration anotherOne = Configuration.builder().setApplicationMode(Configuration.DEBUG).create();
        assertEquals(cfg, anotherOne);
    }

    @Test
    public void createConfig_equals_but_not_identities() throws Exception {
        cfg = Configuration.builder().setApplicationMode(Configuration.DEBUG).create();
        Configuration anotherOne = Configuration.builder().setApplicationMode(Configuration.DEBUG).create();
        assertEquals(cfg, anotherOne);
        assertFalse(cfg == anotherOne);
    }

    @Test
    public void getCurrent_equals_but_not_identities() throws Exception {
        cfg = Configuration.builder().setApplicationMode(Configuration.DEBUG).create();

        Configuration other = Configuration.getCurrent();
        assertEquals(cfg, other);
        assertFalse(cfg == other);

        Configuration anotherOne = Configuration.getCurrent();
        assertEquals(anotherOne, other);
        assertFalse(anotherOne == other);
    }

    @Ignore
    @Test(expected = InternalError.class)
    public void getCurrent_config_without_creating_it_first() throws Exception {
        cfg = Configuration.getCurrent();
    }



}