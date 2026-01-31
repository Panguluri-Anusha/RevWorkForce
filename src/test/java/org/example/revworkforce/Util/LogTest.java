package org.example.revworkforce.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogTest {

    private static final Logger logger = LogManager.getLogger(LogTest.class);

    @Test
    void testLogFileCreated() {

        // Force logging so file is created
        logger.info("Test log entry");

        File file = new File("logs/app.log");

        assertTrue(file.exists(), "Log file was not created!");
    }
}
