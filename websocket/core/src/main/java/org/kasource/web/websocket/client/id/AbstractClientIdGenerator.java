package org.kasource.web.websocket.client.id;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractClientIdGenerator implements ClientIdGenerator {
    private static final String DEFAULT_ID_KEY = "username";
    
    private String idKey = DEFAULT_ID_KEY;
    private boolean headerValue;
    
    
    /**
     * @param idKey the idKey to set
     */
    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }
    
    /**
     * @param headerValue the headerValue to set
     */
    public void setHeaderValue(boolean headerValue) {
        this.headerValue = headerValue;
    }
    
    protected String getIdValue(HttpServletRequest request) {
        if(headerValue) {
            return request.getHeader(idKey);
        } else {
            return request.getParameter(idKey);
        }
    }
    
    protected String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

  
}
