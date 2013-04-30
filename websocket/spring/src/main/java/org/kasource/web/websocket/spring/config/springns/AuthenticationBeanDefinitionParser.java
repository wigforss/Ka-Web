package org.kasource.web.websocket.spring.config.springns;

import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.AUTHENTICATION_CONFIG_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.MANAGER_REPO_ID;
import org.kasource.web.websocket.spring.config.AuthenticationConfig;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AuthenticationBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return AuthenticationConfig.class;
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, AUTHENTICATION_CONFIG_ID);
        String ref = element.getAttributes().getNamedItem("defaultProviderRef").getNodeValue();
        MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
        PropertyValue value = props.getPropertyValue("defaultAuthenticationProvider");
        if(value == null) {
            props.addPropertyValue("defaultAuthenticationProvider", new RuntimeBeanReference(ref));
        } else {
            value.setSource(new RuntimeBeanReference(ref));
        }
        
        registerUrlProviders(bean, element);
        
        try {
            BeanDefinition managerRepo = pc.getRegistry().getBeanDefinition(MANAGER_REPO_ID);
            MutablePropertyValues managerProps = managerRepo.getPropertyValues();
            PropertyValue authenticationConfigValue = managerProps.getPropertyValue("authenticationConfig");
            if(authenticationConfigValue == null) {
                managerProps.addPropertyValue("authenticationConfig", new RuntimeBeanReference(AUTHENTICATION_CONFIG_ID));
            } else {
                authenticationConfigValue.setSource(new RuntimeBeanReference(AUTHENTICATION_CONFIG_ID));
            }
        } catch(NoSuchBeanDefinitionException nsbde) {
            // Ignore
        }
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
        NodeList mappingNodes = element.getChildNodes();
        for(int i = 0; i < mappingNodes.getLength(); ++i) {
            Node mapping = mappingNodes.item(i);
            String url = mapping.getAttributes().getNamedItem("url").getNodeValue();
            String ref = mapping.getAttributes().getNamedItem("ref").getNodeValue();
            authenticationUrlMapping.put(url, new RuntimeBeanReference(ref));
        }
        
    }
}
