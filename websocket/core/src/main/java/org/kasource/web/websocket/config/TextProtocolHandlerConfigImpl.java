package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class TextProtocolHandlerConfigImpl implements ProtocolHandlerConfig<String>{
    private Map<String, List<ProtocolHandler<String>>> protocolUrlMap;
    private ProtocolHandler<String> defaultHandler;
    private List<ProtocolHandler<String>> handlers;
    private Map<String, ProtocolHandler<String>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<String>>();
    
    @Override
    public ProtocolHandler<String> getDefaultHandler() {
        return defaultHandler;
    }

    @Override
    public List<ProtocolHandler<String>> getHandlers() {
        return handlers;
    }

    /**
     * @param defaultHandler the defaultHandler to set
     */
    public void setDefaultHandler(ProtocolHandler<String> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * @param handlers the handlers to set
     */
    public void setHandlers(List<ProtocolHandler<String>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public Map<String, List<ProtocolHandler<String>>> getProtocolUrlMap() {
        return protocolUrlMap;
    }

    /**
     * @return the defaultProtocolUrlMap
     */
    @Override
    public Map<String, ProtocolHandler<String>> getDefaultProtocolUrlMap() {
        return defaultProtocolUrlMap;
    }

    /**
     * @param defaultProtocolUrlMap the defaultProtocolUrlMap to set
     */
    public void setDefaultProtocolUrlMap(Map<String, ProtocolHandler<String>> defaultProtocolUrlMap) {
        this.defaultProtocolUrlMap = defaultProtocolUrlMap;
    }

    /**
     * @param protocolUrlMap the protocolUrlMap to set
     */
    public void setProtocolUrlMap(Map<String, List<ProtocolHandler<String>>> protocolUrlMap) {
        this.protocolUrlMap = protocolUrlMap;
    }

}
