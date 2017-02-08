package com.zz.rpc.spring.config;

import com.zz.rpc.netty.NettyServer;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ServerConfigBean implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, DisposableBean {

    private String id;
    private Integer port;
    private ApplicationContext applicationContext;
    private NettyServer nettyServer;

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
        RegistryConfigBean registryConfigBean = applicationContext.getBean(RegistryConfigBean.class);
        String registryAddress = registryConfigBean.getAddress();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperServiceRegistry.class);
        beanDefinitionBuilder.addConstructorArgValue(registryAddress);
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        acf.registerBeanDefinition("zookeeperServiceRegistry", beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            nettyServer = new NettyServer(port);
            nettyServer.start();
        }).start();
    }

    @Override
    public void destroy() throws Exception {
        nettyServer.close();
        System.out.println("destroy");
        ZookeeperServiceRegistry zookeeperServiceRegistry = applicationContext.getBean(ZookeeperServiceRegistry.class);
        zookeeperServiceRegistry.unRegister("");
    }
}
