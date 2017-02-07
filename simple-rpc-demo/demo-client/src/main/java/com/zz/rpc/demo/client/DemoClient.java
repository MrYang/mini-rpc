package com.zz.rpc.demo.client;

import com.zz.rpc.demo.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoClient {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        HelloService helloService = applicationContext.getBean(HelloService.class);
        for (int i = 0; i < 100; i++) {
            System.out.println(helloService.hello("rpc" + i));
        }

        Thread.sleep(3*1000);

        System.out.println(helloService.hello("rpc100"));
    }
}
