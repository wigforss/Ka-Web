package org.kasource.web.websocket.spring.config.springns;


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

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
    
    @Override
    public void init() {
        registerBeanDefinitionParser("websocket", new ConfigurerBeanDefinitionParser()); 
        registerBeanDefinitionParser("authentication", new AuthenticationBeanDefinitionParser()); 
        registerBeanDefinitionParser("textProtocolHandlers", new TextProtocolsBeanDefinitionParser()); 
        registerBeanDefinitionParser("binaryProtocolHandlers", new BinaryProtocolsBeanDefinitionParser()); 
        
    }

}
