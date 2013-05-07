package org.kasource.web.websocket.channel;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.listener.WebSocketEventListener;

/**
 * Web Socket Channel Factory.
 * 
 * Provides access to WebSocketChannels.
 * 
 * @author rikardwi
 **/
public interface WebSocketChannelFactory {
   
    /**
     * Adds a web socket from a published servlet context attribute.
     * 
     * @param attributeName     Name of an attribute
     * @param attributeValue    Value of an attribute
     **/
    public void addWebSocketManagerFromAttribute(String attributeName, Object attributeValue);
    
    /**
     * Initialize using the ServletContext provided.
     * 
     * @param servletContext The servlet context to listen to.
     **/
    public void initialize(ServletContext servletContext) throws Exception;
    
    /**
     * Returns the WebSocketChannel that matches the supplied URL.
     * 
     * If no such WebSocketChannel can be found a new instance will be
     * returned which will be lazily initialized once a connection is established 
     * to a web socket with that name.
     * 
     * 
     * @param url URL of the WebSocketChannel to locate.
     * 
     * @return The WebSocketChannel with the matching URL.
     **/
    public WebSocketChannel get(String url);
  
    /**
     * Register a listener to a channel.
     * 
     * The * wild card is allowed in the socketChannelName argument, which
     * will allow for lazy registration once a web socket channel becomes 
     * available which matches to wild card expression.
     * 
     * @param url               URL of the web socket channel to register on.
     * @param listener          Listener instance.
     **/
    public void listenTo(String url, WebSocketEventListener listener) ;
}
