package com.zyw.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);

    public static void main(String[] args){

        /**
         * 21-08-23
         * 绑定日志的实现（Binding）
         * SLF4J 支持各种日志框架：SLF4J 发行版附带了几个称为 “SLF4J 绑定”的 jar 文件
         * 每个绑定对应一个支持的框架
         *
         * SLF4J的绑定流程：
         * 1.添加 slf4j-api 的依赖
         * 2.使用 slf4j 的 API 在项目中进行统一的日志记录
         * 3.绑定具体的日志实现框架
         *  3.1.绑定已实现了 slf4j 的日志框架，直接添加对应依赖
         *  3.2.绑定没有实现 slf4j 的日志框架，先添加日志的适配器，再添加实现类的依赖
         * 4.slf4j有且仅有一个日志实现框架的绑定（maven：如果出现多个默认使用第一个依赖日志实现）
         *
         * 绑定 log4j 日志实现，需要导入适配器 slf4j-log4j12.jar log4j.jar
         * 绑定 jul(JAVA JDK 内置) 日志实现，只需要导入适配器 slf4j-jdk14
         */
        // 打印日志信息
        LOGGER.error("erro");
        LOGGER.warn("warn");
        LOGGER.info("info");
        LOGGER.debug("debug");
        LOGGER.trace("trace");

        // 使用占位符输出日志信息
        String name = "Jack";
        Integer age = 19;
        LOGGER.info("用户：{}，{}", name, age);

        // 将系统异常信息写入日志
        try {
            int i = 1/0;
        }catch (Exception e){
            LOGGER.info("出现异常：", e);
        }

        /**
         * 如果依赖的某些组件依赖于 SLF4J 以外的日志记录 API。
         * 而这些组件在不久的将来不会切换到 SLF4J，为了解决这种情况，SLF4J 附带了几个桥接模块，
         * 这些模块将对 log4j, jcl 和 java.util.logging API 的调用重定向，就像是对
         * SLF4J API 一样
         *
         * 具体实现请看 slf4j_log4j2_demo 桥接旧的日志框架
         */
    }
}
