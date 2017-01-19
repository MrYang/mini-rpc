package com.zz.rpc.spring.config;

import com.zz.rpc.netty.RefererInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

public class RefererConfigBean<T> implements FactoryBean<T> , ApplicationContextAware {

    private String id;
    private Class<T> interfaceClass;
    private String registyAddress;

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
        return (T) Proxy.newProxyInstance(RefererConfigBean.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RefererInvocationHandler(interfaceClass, registyAddress));
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
        RegistryConfigBean registryConfigBean = applicationContext.getBean(RegistryConfigBean.class);
        registyAddress = registryConfigBean.getAddress();
    }
}
