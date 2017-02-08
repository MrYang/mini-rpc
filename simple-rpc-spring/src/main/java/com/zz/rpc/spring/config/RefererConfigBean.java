package com.zz.rpc.spring.config;

import com.zz.rpc.core.registry.Callback;
import com.zz.rpc.core.registry.ServiceDiscovery;
import com.zz.rpc.netty.NettyClient;
import com.zz.rpc.netty.RefererInvocationHandler;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.*;

public class RefererConfigBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private Set<String> servers = new HashSet<>();
    private List<NettyClient> clients = new ArrayList<>();

    private String id;
    private Class<T> interfaceClass;

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
        return (T) Proxy.newProxyInstance(RefererConfigBean.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RefererInvocationHandler(interfaceClass, clients));
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
        ServiceDiscovery serviceDiscovery = applicationContext.getBean(ZookeeperServiceDiscovery.class);
        String serviceAddress = serviceDiscovery.discover(interfaceClass.getName(), new Callback() {
            @Override
            public void add(String clientAddress) {
                String host = clientAddress.split(":")[0];
                int port = Integer.parseInt(clientAddress.split(":")[1]);
                NettyClient nettyClient = new NettyClient(host, port);
                clients.add(nettyClient);
            }

            @Override
            public void remove(String clientAddress) {
                String host = clientAddress.split(":")[0];
                int port = Integer.parseInt(clientAddress.split(":")[1]);
                Iterator<NettyClient> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    NettyClient nettyClient = iterator.next();
                    if (nettyClient.getHost().equals(host) && nettyClient.getPort() == port) {
                        nettyClient.close();
                        iterator.remove();
                        break;
                    }
                }
            }
        });

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
