package org.kasource.web.websocket.channel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;


/**
 * Web Socket Channel Factory standard implementation.
 * 
 * Provides access to WebSocketChannels.
 * 
 * @author rikardwi
 **/
public class WebSocketChannelFactoryImpl implements  WebSocketChannelFactory {
    
    
    private Map<String, WebSocketChannelImpl> webSockets = new ConcurrentHashMap<String, WebSocketChannelImpl>();
    protected ServletContext servletContext;
   
    private Map<String, List<WebSocketEventListener>> lazyListeners = new ConcurrentHashMap<String, List<WebSocketEventListener>>();
 
   
    
    
    /**
     * On initialization, create all web sockets currently available in
     * the servlet context and listen for events when web sockets becomes
     * available in the servlet context.
     * 
     * @param servletContext The servlet context to listen to.
     **/
    @Override
    public void initialize(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        
        Enumeration<String> attributeEnum = servletContext.getAttributeNames();
        while (attributeEnum.hasMoreElements()) {
            String attributeName = attributeEnum.nextElement();
            addWebSocketManagerFromAttribute(attributeName, servletContext.getAttribute(attributeName));
        }
        
        servletContext.setAttribute(WebSocketChannelFactory.class.getName(), this);
    }

    
    
    /**
     * Returns the WebSocket with channelName.
     * 
     * Note that if this method is invoked before such a 
     * WebSocket is available, an uninitialized WebSocket will be returned, 
     * which might be initialized at a later point in time.
     * 
     * @param channelName    Name of the socket to return.
     * 
     * @return A WebSocket representing channelName supplied.
     **/
    public WebSocketChannelImpl get(String channelName) {
        if (webSockets.containsKey(channelName)) {
            return webSockets.get(channelName);
        }
        
        WebSocketManager manager = (WebSocketManager) servletContext.getAttribute(channelName);
        WebSocketChannelImpl webSocket = null;
        if (manager == null) {
             webSocket = new WebSocketChannelImpl();
        } else {
             webSocket = new WebSocketChannelImpl(channelName, manager);
        }
        webSockets.put(channelName, webSocket);
        
        return webSocket;
    }

    
    
    /**
     * Register an event listener for a socket name.
     * 
     * @param channelName    Name of the socket to listen to.
     * @param listener      Listener instance.
     **/
    public void listenTo(String channelName, WebSocketEventListener listener) {
        if (channelName.contains("*")) {
            for (Map.Entry<String, WebSocketChannelImpl> entry : webSockets.entrySet()) {
                if (entry.getKey().matches(channelName.replace("*", ".*"))) {
                    entry.getValue().addListener(listener);
                }
            }
            addLazyListener(channelName, listener);
        } else {
            WebSocketChannelImpl webSocket = get(channelName);
            if (webSocket == null) {
                addLazyListener(channelName, listener);
            }
            webSocket.addListener(listener);
        }
    }

    private void addLazyListener(String channelName, WebSocketEventListener listener) {
        List<WebSocketEventListener> listeners = lazyListeners.get(channelName);
        if(listeners == null) {
            listeners = new ArrayList<WebSocketEventListener>();
            lazyListeners.put(channelName, listeners);
        }
        listeners.add(listener);
    }
    
    
    /**
     * Adds a web socket from a published servlet context attribute.
     * 
     * @param attributeName     Name of an attribute
     * @param attributeValue    Value of an attribute
     **/
    public void addWebSocketManagerFromAttribute(String attributeName, Object attributeValue) {
        if (attributeValue instanceof WebSocketManager) {
            String channelName = attributeName.substring(WebSocketManagerRepository.ATTRIBUTE_PREFIX.length());
            WebSocketChannelImpl websocket = webSockets.get(channelName);
            if (websocket != null) {
                websocket.initialize(channelName, (WebSocketManager) attributeValue);
            } else {
                WebSocketChannelImpl webSocket = new WebSocketChannelImpl(channelName, (WebSocketManager) attributeValue);
                webSockets.put(channelName, webSocket);
                addLazyListeners(channelName, webSocket);
            }
        }
    }
    
    /**
     * Add lazily registered listeners.
     * 
     * @param name      Name of the WebSocket
     * @param webSocket WebSocket object.
     **/
    private void addLazyListeners(String name, WebSocketChannelImpl webSocket) {
        for (Map.Entry<String, List<WebSocketEventListener>> entry : lazyListeners.entrySet()) {
            boolean hasWildcard = isWildcardName(entry.getKey());
            String filter = entry.getKey().replace("*", ".*");
            if (name.matches(filter)) {
                Set<WebSocketEventListener> toRemove = new HashSet<WebSocketEventListener>();
                for (WebSocketEventListener listener : entry.getValue()) {
                    webSocket.addListener(listener);
                    if (!hasWildcard) {
                        toRemove.add(listener);
                    }
                }
                entry.getValue().removeAll(toRemove);
            }
            
        }      
    }

    private boolean isWildcardName(String websocketChannelName) {
        return websocketChannelName.contains("*");
    }
    
    

}
