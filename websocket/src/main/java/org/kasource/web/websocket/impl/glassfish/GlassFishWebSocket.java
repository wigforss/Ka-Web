package org.kasource.web.websocket.impl.glassfish;


import java.io.CharConversionException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.util.URLDecoder;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.grizzly.websockets.WebSocketListener;
import org.kasource.web.websocket.WebSocketImpl;
import org.kasource.web.websocket.WebSocketManager;



public class GlassFishWebSocket extends WebSocketApplication  {
    private static final String ATTRIBUTE_PREFIX = WebSocketImpl.class.getPackage().getName() + "."; 
    private static final String DEFAULT_CLIENT_ID_PARAMETER = "username";
    
    private String clientIdParameter = DEFAULT_CLIENT_ID_PARAMETER;
    private ServletContext servletContext;
    private Map<String, GlassFishWebSocketManager> managers = new ConcurrentHashMap<String, GlassFishWebSocketManager>();

    
   public void initialize() {
       WebSocketEngine.getEngine().register(this);   
   }

    @Override
    public boolean isApplicationRequest(HttpRequestPacket request) {
       
        return true;
    }
    
    public WebSocket createSocket(final ProtocolHandler handler, 
            final HttpRequestPacket requestPacket,
            final WebSocketListener... listeners) {
        String socketName = getLastPathElement(requestPacket);
        GlassFishWebSocketManager manager = (GlassFishWebSocketManager) getWebSocketManager(socketName);
       
        String clientId = getParamterValue(requestPacket, clientIdParameter);
        String id = clientId;
        int i = 0;
        while (manager.hasClient(id)) {
            id = clientId + "-" + i;
        }
        if(clientId == null) {
            clientId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }
        return new GlassFishWebSocketClient(manager, handler, requestPacket, listeners, id, getParamterMap(requestPacket));
    }
    
    
    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            GlassFishWebSocketManager manager = new GlassFishWebSocketManager();
            managers.put(socketName, manager);
            servletContext.setAttribute(ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
    
   
    
    
    private String getLastPathElement(HttpRequestPacket requestPacket) {
        int index = requestPacket.getRequestURI().lastIndexOf('/');
        return requestPacket.getRequestURI().substring(index + 1);
    }
    
    // TODO: Could not find a method like HttpServletRequest.getParamter(), find better
    //way then this...
    private String getParamterValue(HttpRequestPacket requestPacket, String paramterName) {
        String clientId = null;
        String queryString;
        try {
            queryString = URLDecoder.decode(requestPacket.getQueryString());
        } catch (CharConversionException e) {
          return null;
        }
        if(queryString.startsWith("?")) {
            queryString = queryString.substring(1);
        }
        String[] parameters = queryString.split("&");
        for(String parameter : parameters) {
            String[] paratmeterParts = parameter.split("=");
            if(paratmeterParts[0].trim().equals(paramterName)) {
                clientId = paratmeterParts[1].trim();
            }
        }
        return clientId;
    }
    
    // TODO: Could not find a method like HttpServletRequest.getParamterMap(), find better
    //way then this...
    private Map<String, String[]> getParamterMap(HttpRequestPacket requestPacket) {
        
        Map<String, String[]> params = new HashMap<String, String[]>();
        String queryString;
        try {
            queryString = URLDecoder.decode(requestPacket.getQueryString());
        } catch (CharConversionException e) {
          return params;
        }
        if(queryString.startsWith("?")) {
            queryString = queryString.substring(1);
        }
        String[] parameters = queryString.split("&");
        for(String parameter : parameters) {
            String[] paratmeterParts = parameter.split("=");
            String key = paratmeterParts[0].trim();
            String value = paratmeterParts[1].trim();
            String[] values = params.get(key);
            if(values == null) {
                params.put(key, new String[]{value});
            } else {
                String[] newValues = new String[values.length+1];
                System.arraycopy(values, 0, newValues, 0, values.length);
                newValues[values.length] = value;
                params.put(key, newValues);
            }
        }
        return params;
    }
    

    /**
     * @param clientIdParameter the clientIdParameter to set
     */
    public void setClientIdParameter(String clientIdParameter) {
        this.clientIdParameter = clientIdParameter;
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
