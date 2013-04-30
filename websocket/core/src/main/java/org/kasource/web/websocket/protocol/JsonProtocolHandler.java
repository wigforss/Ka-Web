package org.kasource.web.websocket.protocol;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonProtocolHandler implements TextProtocolHandler {

    private ObjectMapper jsonMapper = new ObjectMapper();
    
    @Override
    public <T> T toObject(String message, Class<T> ofType) {
        try {
            return (T) jsonMapper.readValue(message, ofType);
        } catch (Exception e) {
           throw new ConversionException("Could not convert " + message + " to " + ofType, e);
        } 
    }

    @Override
    public String toMessage(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new ConversionException("Could not convert " + object + " to JSON", e);
         } 
    }

    @Override
    public String getProtocolName() {
        return "json";
    }
    
  
}
