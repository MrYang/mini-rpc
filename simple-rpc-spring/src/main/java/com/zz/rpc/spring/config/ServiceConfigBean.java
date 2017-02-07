package com.zz.rpc.spring.config;

import com.zz.rpc.core.utils.NetUtils;
import com.zz.rpc.netty.NettyServer;
import com.zz.rpc.netty.RpcServerHandler;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ServiceConfigBean<T> implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static boolean isStart = false;

    private String id;
    private Class<T> interfaceClass;
    private T ref;

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

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {
            int port = event.getApplicationContext().getBean(ServerConfigBean.class).getPort();
            new NettyServer(port).start();
            isStart = true;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Object bean = applicationContext.getBean(interfaceClass);
        RpcServerHandler.serviceMap.put(interfaceClass.getName(), bean);
        ServerConfigBean serverConfigBean = applicationContext.getBean(ServerConfigBean.class);
        String serviceAddress = NetUtils.getLocalAddress().getHostAddress() + ":" + serverConfigBean.getPort();
        ZookeeperServiceRegistry zookeeperServiceRegistry = applicationContext.getBean(ZookeeperServiceRegistry.class);
        zookeeperServiceRegistry.register(interfaceClass.getName(), serviceAddress);
    }
}
