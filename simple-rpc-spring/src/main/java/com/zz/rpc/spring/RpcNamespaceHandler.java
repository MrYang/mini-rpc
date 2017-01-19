package com.zz.rpc.spring;

import com.zz.rpc.spring.config.ClientConfigBean;
import com.zz.rpc.spring.config.RegistryConfigBean;
import com.zz.rpc.spring.config.ServerConfigBean;
import com.zz.rpc.spring.config.ServiceConfigBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("server", new RpcBeanDefinitionParser(ServerConfigBean.class));
        registerBeanDefinitionParser("client", new RpcBeanDefinitionParser(ClientConfigBean.class));
        registerBeanDefinitionParser("registry", new RpcBeanDefinitionParser(RegistryConfigBean.class));
        registerBeanDefinitionParser("service", new RpcBeanDefinitionParser(ServiceConfigBean.class));
        registerBeanDefinitionParser("referer", new SimpleBenDefinitionParser());
    }
}
