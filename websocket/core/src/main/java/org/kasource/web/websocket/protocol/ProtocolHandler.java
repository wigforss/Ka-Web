package org.kasource.web.websocket.protocol;


public interface ProtocolHandler<K> {
    <T> T toObject(K message, Class<T> ofType) throws ConversionException;
    
     K toMessage(Object object) throws ConversionException;
     
     String getProtocolName();
}
