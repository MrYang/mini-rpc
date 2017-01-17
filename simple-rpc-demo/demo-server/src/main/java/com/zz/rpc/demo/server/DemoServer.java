package com.zz.rpc.demo.server;

import com.zz.rpc.spring.config.ServiceConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoServer {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ServiceConfigBean configBean = applicationContext.getBean(ServiceConfigBean.class);

        System.out.println(configBean.getInterfaceClass());
        System.out.println(configBean.getId());
        System.out.println(configBean.getRef());
    }
}
