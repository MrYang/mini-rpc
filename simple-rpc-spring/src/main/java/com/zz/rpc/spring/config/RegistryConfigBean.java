package com.zz.rpc.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RegistryConfigBean implements ApplicationContextAware {

    private String id;
    private String name;
    private String protocol;    // zookeeper, direct, consul
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RegistryConfigBean registryConfigBean = applicationContext.getBean(RegistryConfigBean.class);
        String registryAddress = registryConfigBean.getAddress();
        BeanDefinitionBuilder beanDefinitionBuilder;
        ProtocolEnum pe = ProtocolEnum.valueOf(protocol);
        Class<?> clazz;
        switch (pe) {
            case ZOOKEEPER:
                try {
                    clazz = Class.forName("com.zz.rpc.registry.zookeeper.ZookeeperServiceRegistry");
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("ZookeeperServiceRegistry未找到", e);
                }
                beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
                beanDefinitionBuilder.addConstructorArgValue(registryAddress);
                break;
            default:
                throw new RuntimeException("未实现其他注册接口");
        }
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        acf.registerBeanDefinition(id, beanDefinitionBuilder.getBeanDefinition());
    }

    enum ProtocolEnum {
        ZOOKEEPER("zookeeper"), CONSUL("consul"), DIRECT("direct");
        String name;

        ProtocolEnum(String name) {
            this.name = name;
        }
    }
}
