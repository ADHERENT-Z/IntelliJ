package com.zyw.logback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogbackTest.class);

    public static void main(String[] args){

        /**
         * 21-08-23
         * logback 入门
         */
//        for (int i = 0; i < 200; i++) {
//            // 日志输出
//            LOGGER.error("error");
//            LOGGER.warn("wring");
//            LOGGER.info("info");
//            LOGGER.debug("debug");// 默认级别
//            LOGGER.trace("trace");
//        }


        LOGGER.info("{}", 243);
    }
}
