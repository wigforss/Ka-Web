package org.kasource.web.websocket.config;

import java.util.List;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class BinaryProtocolHandlerConfigImpl implements ProtocolHandlerConfig<byte[]>{

    private ProtocolHandler<byte[]> defaultHandler;
    private List<ProtocolHandler<byte[]>> handlers;
    
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

}
