package org.kasource.web.websocket.config.xml;


import java.util.HashMap;
import java.util.Map;

import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.AuthenticationProviderXmlConfig;
import org.kasource.web.websocket.config.xml.jaxb.AuthenticationUrlMapping;
import org.kasource.web.websocket.config.xml.jaxb.AuthenticationXmlConfig;
import org.kasource.web.websocket.security.AbstractAuthenticationProvider;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

public class XmlAuthentication {

    
    private AuthenticationProvider provider;
    private Map<String, AuthenticationProvider> authenticationUrlMapping = new HashMap<String, AuthenticationProvider>();
    
    public XmlAuthentication(AuthenticationXmlConfig config) {
       
        try {
            if(config.getAuthenticationProvider() != null) {
                provider = getAuthenticationProvider(config.getAuthenticationProvider());
            }
            if(config.getAuthenticationUrlMapping() != null && !config.getAuthenticationUrlMapping().isEmpty()) {
                for(AuthenticationUrlMapping mapping : config.getAuthenticationUrlMapping()) {
                    authenticationUrlMapping.put(mapping.getUrl(), getAuthenticationProvider(mapping.getAuthenticationProvider()));
                }
            }
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load AuthenticationProvider", e);
        }
    }
    

    public AuthenticationProvider getAutenticationProvider() {
        return provider;
    }

   
    
    private AuthenticationProvider getAuthenticationProvider(AuthenticationProviderXmlConfig config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        AuthenticationProvider authProvider = null;
       
        
        String provider = config.getProvider();
        if(provider != null && !provider.trim().isEmpty()) {
            Class<?> providerClass = Class.forName(provider);
            authProvider = (AuthenticationProvider) providerClass.newInstance();
        }
        
        if(authProvider == null) {
            authProvider = new PassthroughAutenticationProvider();
            
        }
        if(authProvider instanceof AbstractAuthenticationProvider){
            AbstractAuthenticationProvider authenticationProvider = (AbstractAuthenticationProvider) authProvider;
            authenticationProvider.setHeaderBased(config.isHeaderAuthentication());
            if(config.getUsernameKey() != null) {
                authenticationProvider.setUsernameKey(config.getUsernameKey());
            }
            if(config.getPasswordKey() != null) {
                authenticationProvider.setPasswordKey(config.getPasswordKey());
            }
        }
        return authProvider;
    }


    /**
     * @return the authenticationUrlMapping
     */
    public Map<String, AuthenticationProvider> getAuthenticationUrlMapping() {
        return authenticationUrlMapping;
    }

}
