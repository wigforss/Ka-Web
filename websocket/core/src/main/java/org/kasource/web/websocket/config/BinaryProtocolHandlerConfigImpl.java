package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class BinaryProtocolHandlerConfigImpl implements ProtocolHandlerConfig<byte[]>{

    private ProtocolHandler<byte[]> defaultHandler;
    private List<ProtocolHandler<byte[]>> handlers;
    private  Map<String, List<ProtocolHandler<byte[]>>> protocolUrlMap;
    private Map<String, ProtocolHandler<byte[]>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<byte[]>>();
    
    @Override
    public ProtocolHandler<byte[]> getDefaultHandler() {
        return defaultHandler;
    }

    @Override
    public List<ProtocolHandler<byte[]>> getHandlers() {
        return handlers;
    }

    /**
     * @param defaultHandler the defaultHandler to set
     */
    public void setDefaultHandler(ProtocolHandler<byte[]> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * @param handlers the handlers to set
     */
    public void setHandlers(List<ProtocolHandler<byte[]>> handlers) {
        this.handlers = handlers;
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

    /**
     * @param defaultProtocolUrlMap the defaultProtocolUrlMap to set
     */
    public void setDefaultProtocolUrlMap(Map<String, ProtocolHandler<byte[]>> defaultProtocolUrlMap) {
        this.defaultProtocolUrlMap = defaultProtocolUrlMap;
    }

}
