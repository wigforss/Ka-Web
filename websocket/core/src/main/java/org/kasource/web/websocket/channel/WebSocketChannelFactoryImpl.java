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
     * Returns the WebSocket for a URL.
     * 
     * Note that if this method is invoked before such a 
     * WebSocket is available, an uninitialized WebSocket will be returned, 
     * which might be initialized at a later point in time.
     * 
     * @param url    Name of the socket to return.
     * 
     * @return A WebSocket representing URL supplied.
     **/
    public WebSocketChannelImpl get(String url) {
        if (webSockets.containsKey(url)) {
            return webSockets.get(url);
        }
        
        WebSocketManager manager = (WebSocketManager) servletContext.getAttribute(url);
        WebSocketChannelImpl webSocket = null;
        if (manager == null) {
             webSocket = new WebSocketChannelImpl();
        } else {
             webSocket = new WebSocketChannelImpl(url, manager);
        }
        webSockets.put(url, webSocket);
        
        return webSocket;
    }

    
    
    /**
     * Register an event listener for a URL.
     * 
     * @param url    URL of the socket to listen to.
     * @param listener      Listener instance.
     **/
    public void listenTo(String url, WebSocketEventListener listener) {
        if (url.contains("*")) {
            String urlPattern = url.replace("*", ".*");
            for (Map.Entry<String, WebSocketChannelImpl> entry : webSockets.entrySet()) {
                if (entry.getKey().matches(urlPattern)) {
                    entry.getValue().addListener(listener);
                }
            }
            addLazyListener(url, listener);
        } else {
            WebSocketChannelImpl webSocket = get(url);
            if (webSocket == null) {
                addLazyListener(url, listener);
            }
            webSocket.addListener(listener);
        }
    }

    private void addLazyListener(String url, WebSocketEventListener listener) {
        List<WebSocketEventListener> listeners = lazyListeners.get(url);
        if(listeners == null) {
            listeners = new ArrayList<WebSocketEventListener>();
            lazyListeners.put(url, listeners);
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
            String url = attributeName.substring(WebSocketManagerRepository.ATTRIBUTE_PREFIX.length());
            WebSocketChannelImpl websocket = webSockets.get(url);
            if (websocket != null) {
                websocket.initialize(url, (WebSocketManager) attributeValue);
            } else {
                WebSocketChannelImpl webSocket = new WebSocketChannelImpl(url, (WebSocketManager) attributeValue);
                webSockets.put(url, webSocket);
                addLazyListeners(url, webSocket);
            }
        }
    }
    
    /**
     * Add lazily registered listeners.
     * 
     * @param name      Name of the WebSocket
     * @param webSocket WebSocket object.
     **/
    private void addLazyListeners(String url, WebSocketChannelImpl webSocket) {
        for (Map.Entry<String, List<WebSocketEventListener>> entry : lazyListeners.entrySet()) {
            boolean hasWildcard = isWildcardName(entry.getKey());
            String urlPattern = entry.getKey().replace("*", ".*");
            if (url.matches(urlPattern)) {
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
