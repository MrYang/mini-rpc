package com.zz.rpc.spring;

import com.zz.rpc.spring.config.RefererConfigBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

public class SimpleBenDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
        beanDefinition.addPropertyValue("port", 3000);
        beanDefinition.addPropertyValue("id", element.getAttribute("id"));
    }

    @Override
    protected Class getBeanClass(Element element) {
        return RefererConfigBean.class;
    }
}
