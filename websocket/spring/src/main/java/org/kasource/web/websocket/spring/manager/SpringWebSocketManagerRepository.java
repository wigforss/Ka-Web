package org.kasource.web.websocket.spring.manager;



import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.spring.config.AuthenticationConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

public class SpringWebSocketManagerRepository extends WebSocketManagerRepositoryImpl implements  ServletContextAware, InitializingBean { 
    private AuthenticationConfig authenticationConfig;

    /**
     * @param authenticationConfig the authenticationConfig to set
     */
    public void setAuthenticationConfig(AuthenticationConfig authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(authenticationConfig != null) {
            setDefaultAuthenticationProvider(authenticationConfig.getDefaultAuthenticationProvider());
            setAutenticationProviders(authenticationConfig.getAuthenticationUrlMapping());
        }
        
    }
}
