package org.kasource.web.websocket.config.xml;

import java.util.List;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.ProtocolHandlerXmlConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class XmlTextProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<String>{
    private ProtocolHandlerXmlConfig config; 
    private List<ProtocolHandler<String>> protocols;
    private ProtocolHandler<String> defaultProtocol;
    
    public XmlTextProtocolHandlerConfig(ProtocolHandlerXmlConfig config) {
        this.config = config;
        try {
            loadTextProtocols();
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load text protocols", e);
        }
    }

    @Override
    public ProtocolHandler<String> getDefaultHandler() {
        return defaultProtocol;
    }

    @Override
    public List<ProtocolHandler<String>> getHandlers() {     
        return protocols;
    }
    
    private void loadTextProtocols() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        defaultProtocol = getDefaultProtocol(config.getDefaultHandlerClass(), String.class);
        List<ProtocolHandler<String>> textProtocols = getProtocols(config.gethandlerClasses(), String.class);
        protocols.addAll(textProtocols);
        
        
    }
}
