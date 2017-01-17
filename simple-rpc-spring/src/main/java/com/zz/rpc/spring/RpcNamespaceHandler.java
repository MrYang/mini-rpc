package com.zz.rpc.spring;

import com.zz.rpc.spring.config.ServiceConfigBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new RpcBeanDefinitionParser(ServiceConfigBean.class));
        registerBeanDefinitionParser("referer", new SimpleBenDefinitionParser());
    }
}
