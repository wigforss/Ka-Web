package org.kasource.web.websocket.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;



public class XmlProtocolHandler implements TextProtocolHandler {

    private static Map<Class<?>, JAXBContext> contexts = new ConcurrentHashMap<Class<?>, JAXBContext>();
    
    private JAXBContext getContext(Class<?> type) throws JAXBException {
        JAXBContext context = contexts.get(type);
        if (context == null) {
            
            try {
                context = JAXBContext.newInstance(type.getPackage().getName());
            } catch (JAXBException jaxb) {
                context = JAXBContext.newInstance(type);
            }
            contexts.put(type, context);
        }
        return context;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(String message, Class<T> ofType) throws ConversionException {
        try {
            JAXBContext context = getContext(ofType);
        
            ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
            return (T) context.createUnmarshaller().unmarshal(in);
        } catch (Exception e) {
           throw new ConversionException("Could not convert "+message + " to " + ofType, e);
        }
    }

    @Override
    public String toMessage(Object object) throws ConversionException {
        try {
        JAXBContext context = getContext(object.getClass());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        context.createMarshaller().marshal(object, out);
        return out.toString();
        } catch (Exception e) {
            throw new ConversionException("Could not convert " + object, e);
         }
    }

    @Override
    public String getProtocolName() {
        return "xml";
    }

}
