package com.zz.rpc.spring.config;

import com.zz.rpc.core.registry.ServiceRegistry;
import com.zz.rpc.core.rpc.RpcServiceParams;
import com.zz.rpc.core.rpc.RpcServiceUtils;
import com.zz.rpc.core.utils.NetUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceConfigBean<T> implements ApplicationContextAware {

    private String id;
    private Class<T> interfaceClass;
    private T ref;

    private String group;
    private String version;
    private int timeout;    // 方法调用超时时间
    private int actives;    // 最大请求数
    private String registry;    // 注册到多个注册中心的id

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getActives() {
        return actives;
    }

    public void setActives(int actives) {
        this.actives = actives;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RpcServiceUtils.serviceMap.put(RpcServiceParams.key(version, group, interfaceClass.getName()), this.ref);
        ServerConfigBean serverConfigBean = applicationContext.getBean(ServerConfigBean.class);
        String serviceAddress = NetUtils.getLocalAddress().getHostAddress() + ":" + serverConfigBean.getPort();
        ServiceRegistry serviceRegistry = applicationContext.getBean(ServiceRegistry.class);
        serviceRegistry.register(interfaceClass.getName(), serviceAddress);
    }
}
