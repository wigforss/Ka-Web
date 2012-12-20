package org.kasource.web.websocket;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;



public class WebSocketFactoryImpl implements  WebSocketFactory, ServletContextAttributeListener {
    private static final String ATTRIBUTE_PREFIX = WebSocketImpl.class.getPackage().getName() + "."; 
    
    private Map<String, WebSocketImpl> webSockets = new ConcurrentHashMap<String, WebSocketImpl>();
    protected ServletContext servletContext;
    private Map<String, WebSocketMessageListener> lazyMessageListeners = new ConcurrentHashMap<String, WebSocketMessageListener>();
    private Map<String, WebSocketEventListener> lazyEventListeners = new ConcurrentHashMap<String, WebSocketEventListener>();
   
    
    /**
     * On initialization, create all web sockets currently available in
     * the servlet context.
     **/
    public void initialize(ServletContext servletContext) throws Exception {
        Enumeration<String> attributeEnum = servletContext.getAttributeNames();
        while (attributeEnum.hasMoreElements()) {
            String attributeName = attributeEnum.nextElement();
            addWebSocketManagerFromAttribute(attributeName, servletContext.getAttribute(attributeName));
        }
        servletContext.addListener(this);
        
    }

    
    /**
     * Returns the WebSocket with socketName.
     * 
     * Note that if this method is invoked before such a 
     * WebSocket is available, an uninitialized WebSocket will be returned, 
     * which might be initialized at a later point in time.
     * 
     * @param socketName    Name of the socket to return.
     * 
     * @return A WebSocket representing socketName supplied.
     **/
    public WebSocketImpl get(String socketName) {
        if(webSockets.containsKey(socketName)) {
            return webSockets.get(socketName);
        }
        WebSocketManager manager = (WebSocketManager) servletContext.getAttribute(socketName);
        WebSocketImpl webSocket = null;
        if(manager == null) {
             webSocket = new WebSocketImpl();
        } else {
             webSocket = new WebSocketImpl(manager);
        }
        webSockets.put(socketName, webSocket);
        
        return webSocket;
    }

    /**
     * Register a message listener for a socket name.
     * 
     * @param socketName    Name of the socket to listen to.
     * @param listener      Listener instance.
     **/
    public void listenTo(String socketName, WebSocketMessageListener listener) {
        if(socketName.contains("*")) {
            for(Map.Entry<String, WebSocketImpl> entry : webSockets.entrySet()) {
                if(entry.getKey().matches(socketName.replace("*", ".*"))) {
                    entry.getValue().addMessageListener(listener);
                }
            }
            lazyMessageListeners.put(socketName, listener);
        } else {
            WebSocketImpl webSocket = get(socketName);
            webSocket.addMessageListener(listener);
        }
    }
    
    /**
     * Register an event listener for a socket name.
     * 
     * @param socketName    Name of the socket to listen to.
     * @param listener      Listener instance.
     **/
    public void listenTo(String socketName, WebSocketEventListener listener) {
        if(socketName.contains("*")) {
            for(Map.Entry<String, WebSocketImpl> entry : webSockets.entrySet()) {
                if(entry.getKey().matches(socketName.replace("*", ".*"))) {
                    entry.getValue().addEventListener(listener);
                }
            }
            lazyEventListeners.put(socketName, listener);
        } else {
            WebSocketImpl webSocket = get(socketName);
            webSocket.addEventListener(listener);
        }
    }

    
    
    /**
     * Adds a web socket from a published servlet context attribute.
     * 
     * @param attributeName     Name of an attribute
     * @param attributeValue    Value of an attribute
     **/
    private void addWebSocketManagerFromAttribute(String attributeName, Object attributeValue) {
        if(attributeValue instanceof WebSocketManager) {
            String socketName = attributeName.substring(ATTRIBUTE_PREFIX.length());
            WebSocketImpl websocket = webSockets.get(socketName);
            if(websocket != null) {
                websocket.initialize((WebSocketManager) attributeValue);
            } else {
                WebSocketImpl webSocket = new WebSocketImpl((WebSocketManager) attributeValue);
                webSockets.put(socketName, webSocket);
                addLazyListeners(socketName, webSocket);
            }
        }
    }
    
    /**
     * Add lazily registered listeners.
     * 
     * @param name      Name of the WebSocket
     * @param webSocket WebSocket object.
     **/
    private void addLazyListeners(String name, WebSocketImpl webSocket) {
        for(Map.Entry<String, WebSocketMessageListener> entry : lazyMessageListeners.entrySet()) {
            String filter = entry.getKey().replace("*", ".*");
            if(name.matches(filter)) {
                webSocket.addMessageListener(entry.getValue());
            }
        }
        for(Map.Entry<String, WebSocketEventListener> entry : lazyEventListeners.entrySet()) {
            String filter = entry.getKey().replace("*", ".*");
            if(name.matches(filter)) {
                webSocket.addEventListener(entry.getValue());
            }
        }
    }

    /**
     * Listener invoked when a new attribute has been added.
     * 
     * @param attributeEvent Event with the added attribute.
     **/
    @Override
    public void attributeAdded(ServletContextAttributeEvent attributeEvent) {
        addWebSocketManagerFromAttribute(attributeEvent.getName(), attributeEvent.getValue());      
    }


    @Override
    public void attributeRemoved(ServletContextAttributeEvent scab) {
    }


    @Override
    public void attributeReplaced(ServletContextAttributeEvent scab) {
    }

    

}
