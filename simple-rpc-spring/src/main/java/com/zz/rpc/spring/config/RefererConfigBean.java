package com.zz.rpc.spring.config;

import com.zz.rpc.core.registry.ServiceDiscovery;
import com.zz.rpc.netty.NettyClient;
import com.zz.rpc.netty.RefererInvocationHandler;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RefererConfigBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private static Set<String> servers = new HashSet<>();
    private static List<NettyClient> clients = new ArrayList<>();

    private String id;
    private Class<T> interfaceClass;
    private ServiceDiscovery serviceDiscovery;

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
        return (T) Proxy.newProxyInstance(RefererConfigBean.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RefererInvocationHandler(interfaceClass, serviceDiscovery, clients));
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
        String serviceAddress = serviceDiscovery.discover(interfaceClass.getName());
        String[] serviceAddressArray = serviceAddress.split(",");
        for (String address : serviceAddressArray) {
            if (!servers.contains(address)) {
                servers.add(address);
                String host = address.split(":")[0];
                int port = Integer.parseInt(address.split(":")[1]);
                NettyClient nettyClient = new NettyClient(host, port);
                clients.add(nettyClient);
            }
        }
    }
}
