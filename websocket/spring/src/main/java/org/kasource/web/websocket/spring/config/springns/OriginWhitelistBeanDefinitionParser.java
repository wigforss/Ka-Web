package org.kasource.web.websocket.spring.config.springns;

import java.util.ArrayList;
import java.util.List;

import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class OriginWhitelistBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    
    protected Class<?> getBeanClass(Element element) {
        return OriginWhiteListConfig.class;
    }
    
    /**
     * parse the originWhitelist XML element.
     * 
     * @param element originWhitelist XML element.
     * @param pc      Parser context.
     * @param bean    Bean definition.
     **/
    @SuppressWarnings("unchecked")
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.ORIGIN_WHITELIST_ID);
        List<String> originWhiteList = new ArrayList<String>();
        List<Element> originElements = WebSocketXmlNamespaceHandler.getChildElementsByName(element, "origin");
        for (Element origin : originElements) {
            originWhiteList.add(origin.getFirstChild().getNodeValue());  
        }
        
        MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
        
        PropertyValue value = props.getPropertyValue("originWhiteList");
        if (value == null) {
            ManagedSet<String> origins = new ManagedSet<String>();  
            origins.addAll(originWhiteList);
            props.addPropertyValue("originWhiteList", origins);
        } else {
            ManagedSet<String> origins = (ManagedSet<String>) value.getValue();
            origins.addAll(originWhiteList);
        }
        
    }
    
}
