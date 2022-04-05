package com.zz.rpc.spring.config;

import com.zz.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ClientConfigBean implements ApplicationContextAware {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RegistryConfigBean registryConfigBean = applicationContext.getBean(RegistryConfigBean.class);
        String registryAddress = registryConfigBean.getAddress();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperServiceDiscovery.class);
        beanDefinitionBuilder.addConstructorArgValue(registryAddress);
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        acf.registerBeanDefinition("zookeeperServiceDiscovery", beanDefinitionBuilder.getBeanDefinition());
    }
}
