package org.kasource.web.websocket.spring.config.springns;


import java.util.List;

import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class AuthenticationBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return AuthenticationConfig.class;
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.AUTHENTICATION_CONFIG_ID);
       
       
        String ref = element.getAttribute("defaultProviderRef");
        
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("defaultAuthenticationProvider", ref);
        }
        registerUrlProviders(bean, element);
       
    }
    
    @SuppressWarnings("unchecked")
    private void registerUrlProviders(BeanDefinitionBuilder bean, Element element) {
        
        MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
        
        PropertyValue value = props.getPropertyValue("authenticationUrlMapping");
         
        ManagedMap<String, RuntimeBeanReference>  authenticationUrlMapping = null;
        if (value == null) {
            authenticationUrlMapping = new ManagedMap<String, RuntimeBeanReference>();  
            props.addPropertyValue("authenticationUrlMapping", authenticationUrlMapping);
        } else {
            authenticationUrlMapping = (ManagedMap<String, RuntimeBeanReference>) value.getValue();
        }
        List<Element> mappings = WebSocketXmlNamespaceHandler.getChildElementsByName(element, "authenticationUrlMapping");
        for(Element mapping : mappings) {
            
            String url = mapping.getAttribute("url");
            String ref = mapping.getAttribute("ref");
            authenticationUrlMapping.put(url, new RuntimeBeanReference(ref));
        }
        
    }
    
   
}
