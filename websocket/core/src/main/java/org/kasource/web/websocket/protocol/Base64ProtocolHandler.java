package org.kasource.web.websocket.protocol;

import org.apache.commons.codec.binary.Base64;

public class Base64ProtocolHandler implements TextProtocolHandler {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(String message, Class<T> ofType) throws ConversionException {
        if(byte[].class.isAssignableFrom(ofType)) {
            return (T) Base64.decodeBase64(message);
        } else {
            throw new ConversionException("Base64MessageConverter.toObject only supports byte[]: " + ofType);
        }
       
    }

    @Override
    public String toMessage(Object object) throws ConversionException {
        if(object instanceof byte[]) {
            return Base64.encodeBase64String((byte[]) object);
        } else {
            throw new ConversionException("Base64MessageConverter.toMessage only supports byte[]: " + object);
        }
    }

    @Override
    public String getProtocolName() {
        return "base46";
    }

}
