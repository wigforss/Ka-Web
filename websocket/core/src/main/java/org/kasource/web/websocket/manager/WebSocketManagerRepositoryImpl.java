package org.kasource.web.websocket.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
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
    private ProtocolHandlerRepository protocolHandlerRepository;

    public WebSocketManagerRepositoryImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public WebSocketManagerRepositoryImpl() {

    }

    @Override
    public WebSocketManager getWebSocketManager(String url) {
        if (!managers.containsKey(url)) {
            WebSocketManagerImpl manager = new WebSocketManagerImpl();
            AuthenticationProvider authenticationProvider = getAuthenticationProviderByUrl(url);
            manager.setAuthenticationProvider(authenticationProvider != null ? authenticationProvider
                        : defaultAuthenticationProvider);
            manager.setProtocolHandlerRepository(protocolHandlerRepository);
            managers.put(url, manager);
            servletContext.setAttribute(ATTRIBUTE_PREFIX + url, manager);
            return manager;
        }
        return managers.get(url);
    }

    private AuthenticationProvider getAuthenticationProviderByUrl(String url) {
        for (String key : autenticationProviders.keySet()) {
            String urlPattern = key;
            if (key.contains("*")) {
                urlPattern = key.replace("*", ".*");
            }
            if (url.matches(urlPattern)) {
                return autenticationProviders.get(key);
            }
        }
        return null;
    }

    /**
     * @param servletContext
     *            the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param defaultAuthenticationProvider
     *            the defaultAuthenticationProvider to set
     */
    public void setDefaultAuthenticationProvider(AuthenticationProvider defaultAuthenticationProvider) {
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
    }

    /**
     * @param autenticationProviders
     *            the autenticationProviders to set
     */
    public void setAutenticationProviders(Map<String, AuthenticationProvider> autenticationProviders) {
        this.autenticationProviders = autenticationProviders;
    }

    /**
     * @param protocolHandlerRepository
     *            the protocolHandlerRepository to set
     */
    public void setProtocolHandlerRepository(ProtocolHandlerRepository protocolHandlerRepository) {
        this.protocolHandlerRepository = protocolHandlerRepository;
    }

    /**
     * @return the protocolHandlerRepository
     */
    @Override
    public ProtocolHandlerRepository getProtocolHandlerRepository() {
        return protocolHandlerRepository;
    }

}
