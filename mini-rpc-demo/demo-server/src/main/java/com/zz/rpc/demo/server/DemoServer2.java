package com.zz.rpc.demo.server;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class DemoServer2 {

    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext2.xml");
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        applicationContext.registerShutdownHook();
    }
}
