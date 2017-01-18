package com.zz.rpc.spring.config;

import com.zz.rpc.netty.RefererInvocationHandler;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RefererConfigBean<T> implements FactoryBean<T> {

    private String id;
    private Class<T> interfaceClass;
    private String port;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(RefererConfigBean.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RefererInvocationHandler(interfaceClass));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
