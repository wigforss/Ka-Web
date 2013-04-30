package org.kasource.web.websocket.protocol;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;


public class ProtocolHandlerRepositoryImpl implements ProtocolHandlerRepository {
    private Map<String, ProtocolHandler<String>> textProtocols = new HashMap<String, ProtocolHandler<String>>();
    private Map<String, ProtocolHandler<byte[]>> binaryProtocols = new HashMap<String, ProtocolHandler<byte[]>>();

    
    private ProtocolHandler<String> defaultTextProtocol;
    private ProtocolHandler<byte[]> defaultBinaryProtocol;

    public ProtocolHandlerRepositoryImpl() {}
    
    public ProtocolHandlerRepositoryImpl(ProtocolHandlerConfig<String> textProtocolConfig, 
                                         ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        try {
            setTextProtocolHandlerConfig(textProtocolConfig);
            setBinaryProtocolHandlerConfig(binaryProtocolConfig);
        } catch (Exception e) {
            throw new WebSocketConfigException("Could not initialize ProtocolHandlerRepository", e);
        }
    }

    public void setTextProtocolHandlerConfig(ProtocolHandlerConfig<String> textProtocolConfig) {
        if(textProtocolConfig != null) {
            defaultTextProtocol = textProtocolConfig.getDefaultHandler();
            loadTextProtocols(textProtocolConfig);
        }
    }
    
    public void setBinaryProtocolHandlerConfig(ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        if(binaryProtocolConfig != null) {
            defaultBinaryProtocol = binaryProtocolConfig.getDefaultHandler();
            loadBinaryProtocols(binaryProtocolConfig);
        }
    }
    
    public void initialize(ProtocolHandlerConfig<String> textProtocolConfig, 
                ProtocolHandlerConfig<byte[]> binaryProtocolConfig) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        
    }

    public boolean hasProtocol(String protocol) {
        if (textProtocols.containsKey(protocol)) {
            return true;
        } else if (binaryProtocols.containsKey(protocol)) {
            return true;
        }
        return false;
    }

    private void loadTextProtocols(ProtocolHandlerConfig<String> textProtocolConfig) {

        List<ProtocolHandler<String>> protocols = textProtocolConfig.getHandlers();
        for (ProtocolHandler<String> protocol : protocols) {
            textProtocols.put(protocol.getProtocolName(), protocol);
        }

    }

    private void loadBinaryProtocols(ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {

        List<ProtocolHandler<byte[]>> protocols = binaryProtocolConfig.getHandlers();
        for (ProtocolHandler<byte[]> protocol : protocols) {
            binaryProtocols.put(protocol.getProtocolName(), protocol);
        }

    }

    /**
     * @return the defaultTextProtocol
     */
    public ProtocolHandler<String> getDefaultTextProtocol() {
        return defaultTextProtocol;
    }

    /**
     * @return the defaultBinaryProtocol
     */
    public ProtocolHandler<byte[]> getDefaultBinaryProtocol() {
        return defaultBinaryProtocol;
    }

    @Override
    public ProtocolHandler<String> getTextProtocol(String protocol, boolean useDefault) {
        ProtocolHandler<String> textProtocol = textProtocols.get(protocol);
        if(textProtocol == null && useDefault) {
            return defaultTextProtocol;
        }
        return textProtocol;
    }

    @Override
    public ProtocolHandler<byte[]> getBinaryProtocol(String protocol, boolean useDefault) {
        ProtocolHandler<byte[]> binaryProtocol = binaryProtocols.get(protocol);
        if(binaryProtocol == null && useDefault) {
            return defaultBinaryProtocol;
        }
        return binaryProtocol;
    }

}
