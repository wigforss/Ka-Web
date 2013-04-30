package org.kasource.web.websocket.config;

import java.util.List;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public interface ProtocolHandlerConfig<T> {
    public ProtocolHandler<T> getDefaultHandler();
    
    public List<ProtocolHandler<T>> getHandlers();
}
