package org.kasource.web.websocket.config.xml;

import java.util.List;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.ProtocolHandlerXmlConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class XmlBinaryProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<byte[]> {
    private ProtocolHandlerXmlConfig config; 
    private List<ProtocolHandler<byte[]>> protocols;
    private ProtocolHandler<byte[]> defaultProtocol;
    
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
        List<ProtocolHandler<byte[]>> binaryProtocols = getProtocols(config.gethandlerClasses(), byte[].class);
        protocols.addAll(binaryProtocols);
    }

   

}
