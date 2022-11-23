package com.zyw.jul;

import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

public class JULTest {

    public static void main(String[] args){

        /**
         * 21-08-19
         */
        // 创建日志记录器对象
//        Logger logger = Logger.getLogger("com.zyw.jul.JULTest");
//        // 日志记录输出
//        logger.info("hello jul");
//
//        logger.log(Level.INFO,"info msg");
//
//        String name = "jack";
//        Integer age = 10;
//        logger.log(Level.INFO, "user info:{0},{1}", new Object[]{name, age});

        /**
         * 21-08-20
         * 日志的级别
         */
        // 创建日志记录器对象
//        Logger logger = Logger.getLogger("com.zyw.jul.JULTest");
//        // 日志记录输出：默认只实现info以上的级别
//        logger.severe("severe");
//        logger.warning("warning");
//        logger.info("info");
//        logger.config("config");
//        logger.fine("fine");
//        logger.finer("finer");
//        logger.finest("finest");

        /**
         * 21-08-20
         * 自定义日志级别配置
         */
        // 创建日志记录器对象
//        Logger logger = Logger.getLogger("com.zyw.jul.JULTest");
//        // 自定义日志级别
//        // a.关闭系统默认配置
//        logger.setUseParentHandlers(false);
//        // b.创建 handler 对象
//        ConsoleHandler consoleHandler = new ConsoleHandler();
//        // c.创建 formatter 对象
//        SimpleFormatter simpleFormatter = new SimpleFormatter();
//        // d.进行关联
//        consoleHandler.setFormatter(simpleFormatter);
//        logger.addHandler(consoleHandler);
//        // e.设置日志级别
//        logger.setLevel(Level.ALL);
//        consoleHandler.setLevel(Level.ALL);
//
//        // 输出日志到文件
//        FileHandler fileHandler = null;
//        try {
//            fileHandler = new FileHandler("e:/logs/jul.log");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        fileHandler.setFormatter(simpleFormatter);
//        logger.addHandler(fileHandler);
//
//        // 日志记录输出
//        logger.severe("severe");
//        logger.warning("warning");
//        logger.info("info");
//        logger.config("config");
//        logger.fine("fine");
//        logger.finer("finer");
//        logger.finest("finest");

        /**
         * 21-08-20
         * Logger之间的父子关系
         */
        // 日志记录器对象父子关系
//        Logger logger1 = Logger.getLogger("com.zyw.jul");
//        Logger logger2 = Logger.getLogger("com.zyw");
//
//        System.out.println(logger1.getParent() == logger2);
//        // 所有日志记录器的顶级父元素：class为 java.util.logging.LogManager$RootLogger name为 “”
//        System.out.println("logger2 parent:" + logger2.getParent() + ",name" + logger2.getParent().getName());
//
//        // 自定义日志级别
//        // a.关闭系统默认配置
//        logger2.setUseParentHandlers(false);
//        // b.创建 handler 对象
//        ConsoleHandler consoleHandler = new ConsoleHandler();
//        // c.创建 formatter 对象
//        SimpleFormatter simpleFormatter = new SimpleFormatter();
//        // d.进行关联
//        consoleHandler.setFormatter(simpleFormatter);
//        logger2.addHandler(consoleHandler);
//        // e.设置日志级别
//        logger2.setLevel(Level.ALL);
//        consoleHandler.setLevel(Level.ALL);
//
//        // 日志记录输出:测试日志记录器对象父子关系
//        logger1.severe("severe");
//        logger1.warning("warning");
//        logger1.info("info");
//        logger1.config("config");
//        logger1.fine("fine");
//        logger1.finer("finer");
//        logger1.finest("finest");

        /**
         * 21-08-20
         * 日志的配置文件
         */
        // 读取自定义配置文件
//        InputStream in = JULTest.class.getClassLoader().getResourceAsStream("logging.properties");
//        // 获取日志管理器对象
//        LogManager logManager = LogManager.getLogManager();
//        // 通过日志管理器加载配置文件
//        try {
//            logManager.readConfiguration(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Logger logger = Logger.getLogger("com.zyw.jul.JULTest");
//        // 日志记录输出
//        logger.severe("severe");
//        logger.warning("warning");
//        logger.info("info");
//        logger.config("config");
//        logger.fine("fine");
//        logger.finer("finer");
//        logger.finest("finest");

    }
}
