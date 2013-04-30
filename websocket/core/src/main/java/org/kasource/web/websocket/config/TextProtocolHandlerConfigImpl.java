package org.kasource.web.websocket.config;

import java.util.List;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class TextProtocolHandlerConfigImpl implements ProtocolHandlerConfig<String>{

    private ProtocolHandler<String> defaultHandler;
    private List<ProtocolHandler<String>> handlers;
    
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

}
