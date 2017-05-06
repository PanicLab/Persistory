package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Сергей on 03.05.2017.
 */
public class LoggerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseMessage() throws Exception {
        Logger logger = new Logger(Configuration.PRODUCTION, Logger.SYS_OUT);
        String message = logger.parseMessage("Однажды, в студеную зимнюю {1} я из лесу вышел, был сильный {2}!", "пору", "мороз");
        assertEquals("Однажды, в студеную зимнюю пору я из лесу вышел, был сильный мороз!", message);

        Logger another = new Logger(Configuration.PRODUCTION);
        String message2 = logger.parseMessage("Птички все на {1}, а я, бедняжка, в {2}", "веточке", "клеточке");
        assertEquals("Птички все на веточке, а я, бедняжка, в клеточке", message2);
    }
}