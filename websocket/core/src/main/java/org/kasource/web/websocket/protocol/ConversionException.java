package org.kasource.web.websocket.protocol;

/**
 * Thrown when exception occured during conversion to and from text / object or byte[] / object.
 * @author rikardwi
 **/
public class ConversionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConversionException(String message) {
        super(message);
    }
    
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
