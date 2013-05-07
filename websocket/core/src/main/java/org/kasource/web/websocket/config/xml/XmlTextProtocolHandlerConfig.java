package org.kasource.web.websocket.config.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.ProtocolHandlerXmlConfig;
import org.kasource.web.websocket.config.xml.jaxb.ProtocolUrlMapping;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class XmlTextProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<String>{
    private ProtocolHandlerXmlConfig config; 
    private List<ProtocolHandler<String>> protocols = new ArrayList<ProtocolHandler<String>>();
    private Map<String, List<ProtocolHandler<String>>> protocolUrlMap = new HashMap<String, List<ProtocolHandler<String>>>();
    private ProtocolHandler<String> defaultProtocol;
    private Map<String, ProtocolHandler<String>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<String>>();
    
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
        List<ProtocolHandler<String>> textProtocols = getProtocols(config.getHandlerClass(), String.class);
        protocols.addAll(textProtocols);
        for(ProtocolUrlMapping urlMapping : config.getProtocolUrlMapping()) {
            List<ProtocolHandler<String>> urlTextProtocols = getProtocols(urlMapping.getHandlerClass(), String.class);
            protocolUrlMap.put(urlMapping.getUrl(), urlTextProtocols);
            ProtocolHandler<String> defaultByUrl = getDefaultProtocol(urlMapping.getDefaultHandlerClass(), String.class);
            defaultProtocolUrlMap.put(urlMapping.getUrl(), defaultByUrl);
        }
        
    }

    @Override
    public Map<String, List<ProtocolHandler<String>>> getProtocolUrlMap() {
        return protocolUrlMap;
    }

    @Override
    public Map<String, ProtocolHandler<String>> getDefaultProtocolUrlMap() {
        return defaultProtocolUrlMap;
    }

   
}
