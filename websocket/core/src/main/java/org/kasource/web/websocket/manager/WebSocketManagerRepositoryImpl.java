package org.kasource.web.websocket.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import javax.servlet.ServletContext;

import org.kasource.web.websocket.security.AuthenticationProvider;



/**
 * Standard implementation of WebSocketManagerRepository.
 * 
 * @author rikardwi
 **/
public class WebSocketManagerRepositoryImpl implements WebSocketManagerRepository {
    private Map<String, WebSocketManager> managers = new ConcurrentHashMap<String, WebSocketManager>();
    private Map<String, AuthenticationProvider> autenticationProviders = new HashMap<String, AuthenticationProvider>();
    private ServletContext servletContext;
    private AuthenticationProvider defaultAuthenticationProvider;
    
    public WebSocketManagerRepositoryImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public WebSocketManagerRepositoryImpl() {
       
    }
    
    @Override
    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            WebSocketManagerImpl manager = new WebSocketManagerImpl();
            AuthenticationProvider authenticationProvider = autenticationProviders.get(socketName);
            manager.setAuthenticationProvider(authenticationProvider != null ? authenticationProvider :defaultAuthenticationProvider);
            managers.put(socketName, manager);
            servletContext.setAttribute(ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }

    

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param defaultAuthenticationProvider the defaultAuthenticationProvider to set
     */
    public void setDefaultAuthenticationProvider(AuthenticationProvider defaultAuthenticationProvider) {
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
    }

    /**
     * @param autenticationProviders the autenticationProviders to set
     */
    public void setAutenticationProviders(Map<String, AuthenticationProvider> autenticationProviders) {
        this.autenticationProviders = autenticationProviders;
    }

}
