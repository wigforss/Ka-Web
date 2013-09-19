package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.Map;

import org.kasource.web.websocket.security.AuthenticationProvider;

public class AuthenticationConfig {
    private AuthenticationProvider defaultAuthenticationProvider;
    
    private Map<String, AuthenticationProvider> authenticationUrlMapping = new HashMap<String, AuthenticationProvider>();

    /**
     * @return the defaultAuthenticationProvider
     */
    public AuthenticationProvider getDefaultAuthenticationProvider() {
        return defaultAuthenticationProvider;
    }

    /**
     * @param defaultAuthenticationProvider the defaultAuthenticationProvider to set
     */
    public void setDefaultAuthenticationProvider(AuthenticationProvider defaultAuthenticationProvider) {
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
    }

    /**
     * @return the authenticationUrlMapping
     */
    public Map<String, AuthenticationProvider> getAuthenticationUrlMapping() {
        return authenticationUrlMapping;
    }

    /**
     * @param authenticationUrlMapping the authenticationUrlMapping to set
     */
    public void setAuthenticationUrlMapping(Map<String, AuthenticationProvider> authenticationUrlMapping) {
        this.authenticationUrlMapping = authenticationUrlMapping;
    }
}
