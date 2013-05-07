package org.kasource.web.websocket.spring.config.springns;

import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.BINARY_PROTOCOLS_CONFIG_ID;

import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BinaryProtocolsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return BinaryProtocolHandlerConfigImpl.class;
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, BINARY_PROTOCOLS_CONFIG_ID);
        bean.setLazyInit(false);
        
        String ref = element.getAttribute("defaultBinaryProtocolRef");
        
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("defaultHandler", ref);
        }
        addProtocols(bean, element);
        
       
    }
    
    @SuppressWarnings("unchecked")
    private void addProtocols(BeanDefinitionBuilder bean, Element element) {
        MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
        PropertyValue value = props.getPropertyValue("handlers");
        ManagedList<RuntimeBeanReference>  protocols = null;
        if (value == null) {
            protocols = new ManagedList<RuntimeBeanReference>();  
            props.addPropertyValue("handlers", protocols);
        } else {
            protocols = (ManagedList<RuntimeBeanReference>) value.getValue();
        }
        NodeList mappingNodes = element.getChildNodes();
        for(int i = 0; i < mappingNodes.getLength(); ++i) {
            Node mapping = mappingNodes.item(i);
            if("protocolHandler".equals(mapping.getLocalName())) {
                String ref = mapping.getAttributes().getNamedItem("ref").getNodeValue();
                protocols.add(new RuntimeBeanReference(ref));
            }
        }
    }
}
