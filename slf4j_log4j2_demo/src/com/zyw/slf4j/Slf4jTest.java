package com.zyw.slf4j;

import org.apache.log4j.Logger;

public class Slf4jTest {

    // 定义 log4j 日志记录器
    public static final Logger LOGGER = Logger.getLogger(Slf4jTest.class);

    public static void main(String[] args){
        /**
         * 如果依赖的某些组件依赖于 SLF4J 以外的日志记录 API。
         * 而这些组件在不久的将来不会切换到 SLF4J，为了解决这种情况，SLF4J 附带了几个桥接模块，
         * 这些模块将对 log4j, jcl 和 java.util.logging API 的调用重定向，就像是对
         * SLF4J API 一样
         *
         * 桥接解决的是项目中日志的遗留问题，当系统中存在之前的日志 API,
         * 就可以通过桥接转换到 slf4j 的实现
         * 1.先除去之前老的日志框架的依赖
         * 2.添加 SLF4J 提供的桥接组件
         * 3.为项目添加 SLF4J 的具体实现
         */

        /**
         * 测试 log4j-over-slf4j 桥接器
         * 将 log4j 变为 SLF4J+logback
         * 步骤：
         * 1.删除依赖： log4j
         * 2.添加依赖：slf4j-api\log4j-over-slf4j（桥接组件）\logback-classic\logback-core
         *
         */
        LOGGER.info("Hello log4j");
    }
}
