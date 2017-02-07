package com.zz.rpc.spring.config;

import com.zz.rpc.core.registry.ServiceDiscovery;
import com.zz.rpc.netty.NettyClient;
import com.zz.rpc.netty.RefererInvocationHandler;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

public class RefererConfigBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private static boolean initClient = false;

    private String id;
    private Class<T> interfaceClass;
    private ServiceDiscovery serviceDiscovery;
    private NettyClient nettyClient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(RefererConfigBean.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RefererInvocationHandler(interfaceClass, serviceDiscovery, nettyClient));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        serviceDiscovery = applicationContext.getBean(ZookeeperServiceDiscovery.class);
        if (!initClient) {
            String serviceAddress = serviceDiscovery.discover(interfaceClass.getName());
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(NettyClient.class);
            String host = serviceAddress.split(":")[0];
            int port = Integer.parseInt(serviceAddress.split(":")[1]);
            beanDefinitionBuilder.addConstructorArgValue(host);
            beanDefinitionBuilder.addConstructorArgValue(port);
            DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            acf.registerBeanDefinition("nettyClient", beanDefinitionBuilder.getBeanDefinition());
        }

        nettyClient = applicationContext.getBean(NettyClient.class);
    }
}
