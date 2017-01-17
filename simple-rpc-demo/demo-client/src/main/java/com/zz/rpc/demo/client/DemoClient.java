package com.zz.rpc.demo.client;

import com.zz.rpc.spring.config.RefererConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        RefererConfigBean configBean = applicationContext.getBean(RefererConfigBean.class);

        System.out.println(configBean.getInterfaceClass());
        System.out.println(configBean.getId());
        System.out.println(configBean.getPort());
    }
}
