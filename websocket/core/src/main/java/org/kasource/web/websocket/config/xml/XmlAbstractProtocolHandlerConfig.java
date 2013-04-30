package org.kasource.web.websocket.config.xml;

import java.util.ArrayList;
import java.util.List;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public abstract class XmlAbstractProtocolHandlerConfig {

    protected <T> List<ProtocolHandler<T>> getProtocols(List<String> handlers, Class<T> ofType)
                throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<ProtocolHandler<T>> protocols = new ArrayList<ProtocolHandler<T>>();
        for (String handlerClass : handlers) {
            Class<?> protocolHandler = Class.forName(handlerClass);
            @SuppressWarnings("unchecked")
            ProtocolHandler<T> handler = (ProtocolHandler<T>) protocolHandler.newInstance();
            protocols.add(handler);
        }
        return protocols;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> ProtocolHandler<T> getDefaultProtocol(String protcolHandler, Class<T> ofType) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (protcolHandler != null && !protcolHandler.isEmpty()) {
            Class<?> handlerClass = Class.forName(protcolHandler);
            ProtocolHandler<T> handler = (ProtocolHandler<T>) handlerClass.newInstance();
            return handler;
        }
        return null;
    }
}
