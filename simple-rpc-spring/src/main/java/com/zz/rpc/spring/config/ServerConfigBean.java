package com.zz.rpc.spring.config;

import com.zz.rpc.core.registry.ServiceRegistry;
import com.zz.rpc.core.rpc.RpcServer;
import com.zz.rpc.netty.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ServerConfigBean implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, DisposableBean {

    private String id;
    private Integer port;
    private ApplicationContext applicationContext;
    private RpcServer rpcServer;

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
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            rpcServer = new NettyServer(port);
            rpcServer.start();
        }).start();
    }

    @Override
    public void destroy() throws Exception {
        rpcServer.close();
        ServiceRegistry serviceRegistry = applicationContext.getBean(ServiceRegistry.class);
        serviceRegistry.unRegister();
    }
}
