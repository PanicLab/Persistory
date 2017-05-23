package com.paniclab.persistory.logger;

import com.paniclab.persistory.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Сергей on 23.05.2017.
 */
public class LoggerTest {
    @Before
    public void setUp() throws Exception {
        Configuration cfg = Configuration.builder()
                                         .setApplicationMode(Configuration.DEVELOPING)
                                         .create();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void newFileLogger() throws Exception {
        Logger logger = Logger.newFileLogger(this);
        assertNotNull(logger);
        System.out.println(logger.getName());
        assertEquals("@" + this.hashCode() + " " + this.getClass().getSimpleName(), logger.getName() );
    }

}