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

public class XmlBinaryProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<byte[]> {
    private ProtocolHandlerXmlConfig config; 
    private List<ProtocolHandler<byte[]>> protocols = new ArrayList<ProtocolHandler<byte[]>>();
    private ProtocolHandler<byte[]> defaultProtocol;
    private Map<String, List<ProtocolHandler<byte[]>>> protocolUrlMap = new HashMap<String, List<ProtocolHandler<byte[]>>>();
    private Map<String, ProtocolHandler<byte[]>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<byte[]>>();
    
    public XmlBinaryProtocolHandlerConfig(ProtocolHandlerXmlConfig config) {
        this.config = config;
        try {
            loadBinaryProtocols();
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load text protocols", e);
        }
    }
    
    @Override
    public ProtocolHandler<byte[]> getDefaultHandler() {
        return defaultProtocol;
    }

    @Override
    public List<ProtocolHandler<byte[]>> getHandlers() {
        return protocols;
    }

    private void loadBinaryProtocols() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        defaultProtocol = getDefaultProtocol(config.getDefaultHandlerClass(), byte[].class);
        List<ProtocolHandler<byte[]>> binaryProtocols = getProtocols(config.getHandlerClass(), byte[].class);
        protocols.addAll(binaryProtocols);
        for(ProtocolUrlMapping urlMapping : config.getProtocolUrlMapping()) {
            List<ProtocolHandler<byte[]>> urlTextProtocols = getProtocols(urlMapping.getHandlerClass(), byte[].class);
            protocolUrlMap.put(urlMapping.getUrl(), urlTextProtocols);
            ProtocolHandler<byte[]> defaultByUrl = getDefaultProtocol(urlMapping.getDefaultHandlerClass(), byte[].class);
            defaultProtocolUrlMap.put(urlMapping.getUrl(), defaultByUrl);
        }
        
    }

    @Override
    public Map<String, List<ProtocolHandler<byte[]>>> getProtocolUrlMap() {
        return protocolUrlMap;
    }

    /**
     * @return the defaultProtocolUrlMap
     */
    @Override
    public Map<String, ProtocolHandler<byte[]>> getDefaultProtocolUrlMap() {
        return defaultProtocolUrlMap;
    }
   

}
