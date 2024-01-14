package com.example.easy_mdc;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void t1() {
        logger.info("info");
        logger.warn("warn");
        logger.debug("debug");

        ch.qos.logback.classic.Logger root = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("ROOT"));
        /**
         *     public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF");
         *     public static final Level ERROR = new Level(40000, "ERROR");
         *     public static final Level WARN = new Level(30000, "WARN");
         *     public static final Level INFO = new Level(20000, "INFO");
         *     public static final Level DEBUG = new Level(10000, "DEBUG");
         *     public static final Level TRACE = new Level(5000, "TRACE");
         *     public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL");
         */
        root.setLevel(Level.INFO);
        root.warn("This message is logged because WARN > INFO.");
        root.debug("This message is not logged because DEBUG < INFO.");
        root.trace("This message is not logged because TRACE < INFO.");
        logger.info("info2");
        logger.debug("debug2");
        root.setLevel(Level.ALL);
        logger.info("info3");
        logger.debug("debug3");
    }
}
