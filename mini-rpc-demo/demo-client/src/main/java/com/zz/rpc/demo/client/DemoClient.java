package com.zz.rpc.demo.client;

import com.zz.rpc.demo.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoClient {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        HelloService helloService = applicationContext.getBean(HelloService.class);
        for (int i = 0; i < 100; i++) {
            System.out.println(helloService.hello("rpc" + i));
        }

        for (int i = 100; i < 200; i++) {
            System.out.println(helloService.hello("rpc" + i));
        }

        for (int i = 200; i < 300; i++) {
            System.out.println(helloService.hello("rpc" + i));
        }

        applicationContext.close();
    }
}
