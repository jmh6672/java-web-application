package com.haram.webserver;

import com.haram.singleton.Environment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnvironmentTest {
    Logger logger = LoggerFactory.getLogger(EnvironmentTest.class);
    Environment environment = Environment.getInstance();


    @Test
    public void propMappingTest() {
        environment.list(System.out);

        assertEquals("10000",environment.getProperty("server.port"));
    }


    @Test
    public void logbackTest() throws IOException {
        logger.info("Test");
    }
}
