package org.kasource.web.websocket.spring.config;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringWebSocketConfigurer implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private SpringWebSocketConfig config;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator;
    
    public SpringWebSocketConfigurer(SpringWebSocketConfig config) {
        this.config = config;
    }
    
    public void configure() {
        if(clientIdGenerator != null) {
            config.setClientIdGenerator(clientIdGenerator);
        }
        config.setDynamicAddressing(dynamicAddressing);
        config.initialize();
    }

  
    
    
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

    /**
     * @return the dynamicAddressing
     */
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    /**
     * @param dynamicAddressing the dynamicAddressing to set
     */
    public void setDynamicAddressing(boolean dynamicAddressing) {
        this.dynamicAddressing = dynamicAddressing;
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }

}
