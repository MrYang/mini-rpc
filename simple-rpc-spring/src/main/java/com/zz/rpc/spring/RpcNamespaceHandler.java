package com.zz.rpc.spring;

import com.zz.rpc.spring.config.*;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("server", new RpcBeanDefinitionParser(ServerConfigBean.class));
        registerBeanDefinitionParser("protocol", new RpcBeanDefinitionParser(ProtocolConfigBean.class));
        registerBeanDefinitionParser("registry", new RpcBeanDefinitionParser(RegistryConfigBean.class));
        registerBeanDefinitionParser("service", new RpcBeanDefinitionParser(ServiceConfigBean.class));
        registerBeanDefinitionParser("referer", new RpcBeanDefinitionParser(RefererConfigBean.class));
    }
}
