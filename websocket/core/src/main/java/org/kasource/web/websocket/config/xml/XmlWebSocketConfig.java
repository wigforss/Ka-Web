package org.kasource.web.websocket.config.xml;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.AbstractClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.ClientIdGeneratorXmlConfig;
import org.kasource.web.websocket.config.xml.jaxb.WebsocketXmlConfigRoot;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;

public class XmlWebSocketConfig implements WebSocketConfig {

    private WebsocketXmlConfigRoot config;

    private Set<String> orginWhitelist = new HashSet<String>();
    private ClientIdGenerator idGenerator;
    private ProtocolHandlerRepository protocolHandlerRepository;
    private WebSocketManagerRepositoryImpl managerRepository;
    private WebSocketChannelFactory channelFactory;
    
    public XmlWebSocketConfig(WebsocketXmlConfigRoot config, ServletContext servletContext) {
        this.config = config;
        initialize(servletContext);
    }
    
    private void initialize(ServletContext servletContext) {
        loadProtocols();
        
        channelFactory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());
        XmlAuthentication authentication = null;
        if(config.getAuthentication() != null) {
            authentication = new XmlAuthentication(config.getAuthentication());
        }
       
        try {
            idGenerator = loadClientIdGenerator();
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not load ClientIdGenerator", e);
        }
        
        if(config.getOrginWhitelist() != null) {
            orginWhitelist.addAll(config.getOrginWhitelist().getOrgin());
        }
        managerRepository = new WebSocketManagerRepositoryImpl(servletContext);
        if(authentication != null) {
            managerRepository.setDefaultAuthenticationProvider(authentication.getAutenticationProvider());
            managerRepository.setAutenticationProviders(authentication.getAuthenticationUrlMapping());
        }
    }

    private void loadProtocols() {
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        if(config.getTextProtocolHandler() != null) {
            textProtocolHandlerConfig = new XmlTextProtocolHandlerConfig(config.getTextProtocolHandler());
        }
        
        if(config.getBinaryProtocolHandler() != null) {
            binaryProtocolHandlerConfig = new XmlBinaryProtocolHandlerConfig(config.getBinaryProtocolHandler());
        }
        
        try {
            protocolHandlerRepository = new ProtocolHandlerRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig); 
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not load Protocol Handlers", e);
        }
    }
    
    private ClientIdGenerator loadClientIdGenerator() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClientIdGeneratorXmlConfig generatorConfig = config.getClientIdGenerator();
        if(generatorConfig != null) {
            Class<?> clazz = Class.forName(generatorConfig.getClazz());
            ClientIdGenerator generator = (ClientIdGenerator) clazz.newInstance();
            if(generator instanceof AbstractClientIdGenerator) {
                AbstractClientIdGenerator abstractGenerator = (AbstractClientIdGenerator) generator;
                abstractGenerator.setHeaderValue(generatorConfig.isHeaderValue());
                if(generatorConfig.getIdKey() != null) {
                    abstractGenerator.setIdKey(generatorConfig.getIdKey());
                }
                
            }
            return generator;
        } else {
            return new DefaultClientIdGenerator();
        }
    }
    
   

    @Override
    public boolean isDynamicAddressing() {   
        return config.isDynamicAddressing();
    }

    @Override
    public ClientIdGenerator getClientIdGenerator() {
        return idGenerator;
    }


    @Override
    public ProtocolHandlerRepository getProtocolHandlerRepository() {
        return protocolHandlerRepository;
    }
    
    @Override
    public Set<String> getOrginWhitelist() {
        
        return orginWhitelist;
    }


    @Override
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    @Override
    public WebSocketChannelFactory getChannelFactory() {
        return channelFactory;
    }

}
