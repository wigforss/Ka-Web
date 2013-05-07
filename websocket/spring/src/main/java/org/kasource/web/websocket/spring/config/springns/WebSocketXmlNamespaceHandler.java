package org.kasource.web.websocket.spring.config.springns;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebSocketXmlNamespaceHandler extends NamespaceHandlerSupport  {

    static final String AUTHENTICATION_CONFIG_ID = "spring.websocket.autenticationConfig";
    static final String CONFIGURER_ID = "spring.websocket.configurer";
    static final String CONFIG_ID = "spring.websocket.config";
    static final String MANAGER_REPO_ID = "spring.websocket.managerRepository";
    static final String PROTOCOL_REPO_ID = "spring.websocket.protocolRepository";
    static final String CHANNEL_FACTORY_ID = "spring.websocket.chanelFactory";
    static final String POST_BEAN_PROCESSOR_ID = "spring.websocket.postBeanProcessor";
    static final String TEXT_PROTOCOLS_CONFIG_ID = "spring.websocket.textProtocolsConfig";
    static final String BINARY_PROTOCOLS_CONFIG_ID = "spring.websocket.binaryProtocolsConfig";
    static final String ORIGIN_WHITELIST_ID = "spring.websocket.originWhitelist";
    
    @Override
    public void init() {
        registerBeanDefinitionParser("websocket", new ConfigurerBeanDefinitionParser()); 
        registerBeanDefinitionParser("servlet", new ServletBeanDefinitionParser());
        registerBeanDefinitionParser("authentication", new AuthenticationBeanDefinitionParser()); 
        registerBeanDefinitionParser("textProtocolHandlers", new ProtocolsBeanDefinitionParser()); 
        registerBeanDefinitionParser("binaryProtocolHandlers", new ProtocolsBeanDefinitionParser()); 
        registerBeanDefinitionParser("originWhitelist", new OriginWhitelistBeanDefinitionParser());
    }

    
    public static List<Element> getChildElementsByName(Element element, String localName) {
        List<Element> nodes = new ArrayList<Element>();
        NodeList nodeList = element.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            
            if(node.getNodeType()==Node.ELEMENT_NODE && localName.equals(node.getLocalName())) {
                nodes.add((Element) node);
            }
        }
        return nodes;
    }
}
