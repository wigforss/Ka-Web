package org.kasource.web.websocket.spring.config.springns;


import java.util.List;

import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        if(element.getLocalName().toLowerCase().contains("text")) {
            return TextProtocolHandlerConfigImpl.class;
        } else {
            return BinaryProtocolHandlerConfigImpl.class;
        }
        
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        if(element.getLocalName().toLowerCase().contains("text")) {
            element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.TEXT_PROTOCOLS_CONFIG_ID);
        } else {
            element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.BINARY_PROTOCOLS_CONFIG_ID);
        }
        bean.setLazyInit(false);
        List<Element> protocolHandler = WebSocketXmlNamespaceHandler.getChildElementsByName(element, "protocolHandler");
        if(!protocolHandler.isEmpty()) {
            parseProtocolHandler(protocolHandler.get(0), bean);
        }
        
        List<Element> protocolUrlMapping = WebSocketXmlNamespaceHandler.getChildElementsByName(element, "protocolUrlMapping");
        if(!protocolUrlMapping.isEmpty()) {
            ManagedMap<String, RuntimeBeanReference> defaultHandlerPerUrl = new ManagedMap<String, RuntimeBeanReference>();
            ManagedMap<String, List<RuntimeBeanReference>> protocolsPerUrl = new ManagedMap<String, List<RuntimeBeanReference>>();
            
            
            for(Element urlMapping : protocolUrlMapping) {
                parseProtocolUrlMapping(urlMapping, defaultHandlerPerUrl, protocolsPerUrl);
            }
            
            if(!defaultHandlerPerUrl.isEmpty()) {
                MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
                PropertyValue value = props.getPropertyValue("defaultProtocolUrlMap");
                if (value == null) {            
                    props.addPropertyValue("defaultProtocolUrlMap", defaultHandlerPerUrl);
                } else {
                   value.setSource(defaultHandlerPerUrl);
                }
            }
            if(!protocolsPerUrl.isEmpty()) {
                MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
                PropertyValue value = props.getPropertyValue("protocolUrlMap");
                if (value == null) {            
                    props.addPropertyValue("protocolUrlMap", protocolsPerUrl);
                } else {
                   value.setSource(protocolsPerUrl);
                }
            }
            
        }
      
        
       
    }
    
    
 
    
    private void parseProtocolHandler(Element protocolHandler, BeanDefinitionBuilder bean) {
        String ref = protocolHandler.getAttribute("defaultProtocolRef");
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("defaultHandler", ref);
        }
        ManagedList<RuntimeBeanReference> protocolList = getProtocols(protocolHandler);
        
        if(!protocolList.isEmpty()) {
            MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
            PropertyValue value = props.getPropertyValue("handlers");
            if (value == null) {            
                props.addPropertyValue("handlers", protocolList);
            } else {
               value.setSource(protocolList);
            }
        }
        
        
       
    }
    
    
    private void parseProtocolUrlMapping(Element protocolUrlMapping, 
                                         ManagedMap<String, RuntimeBeanReference> defaultHandlerPerUrl, 
                                         ManagedMap<String, List<RuntimeBeanReference>> protocolsPerUrl) {
        List<Element> protocolHandlers = WebSocketXmlNamespaceHandler.getChildElementsByName(protocolUrlMapping, "protocolHandler");
        if(!protocolHandlers.isEmpty()) {
            Element protocolHandler = (Element) protocolHandlers.get(0);
            String url = protocolUrlMapping.getAttribute("url");
            String ref = protocolHandler.getAttribute("defaultProtocolRef");
            if(ref != null && !ref.isEmpty()) {
                defaultHandlerPerUrl.put(url, new RuntimeBeanReference(ref));
            }
            ManagedList<RuntimeBeanReference> protocolList = getProtocols(protocolHandler);
            if(!protocolList.isEmpty()) {
                protocolsPerUrl.put(url, protocolList);
            }
        }
    }
    
    private ManagedList<RuntimeBeanReference> getProtocols(Element protocolHandler) {
        ManagedList<RuntimeBeanReference> protocolList = new ManagedList<RuntimeBeanReference>();
        List<Element> protocols = WebSocketXmlNamespaceHandler.getChildElementsByName(protocolHandler, "protocol");
        if(!protocols.isEmpty()) {
            for (Element protocolRefElement : protocols) { 
                protocolList.add(new RuntimeBeanReference(protocolRefElement.getAttributes().getNamedItem("ref").getTextContent()));
            }
        }
        return protocolList;
    }
    
    
    
   
}
