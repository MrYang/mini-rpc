package com.zz.rpc.spring.config;

import com.zz.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServerConfigBean implements ApplicationContextAware {

    private String id;
    private Integer port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RegistryConfigBean registryConfigBean = applicationContext.getBean(RegistryConfigBean.class);
        String registryAddress = registryConfigBean.getAddress();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperServiceRegistry.class);
        beanDefinitionBuilder.addConstructorArgValue(registryAddress);
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        acf.registerBeanDefinition("zookeeperServiceRegistry", beanDefinitionBuilder.getBeanDefinition());
    }
}
