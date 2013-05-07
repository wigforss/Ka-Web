package org.kasource.web.websocket.config;

import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public interface ProtocolHandlerConfig<T> {
    public ProtocolHandler<T> getDefaultHandler();
    
    public List<ProtocolHandler<T>> getHandlers();
    
    public Map<String, List<ProtocolHandler<T>>> getProtocolUrlMap();
    
    public Map<String, ProtocolHandler<T>> getDefaultProtocolUrlMap();
}
