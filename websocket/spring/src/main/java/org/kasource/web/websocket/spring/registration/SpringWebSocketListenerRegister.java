package org.kasource.web.websocket.spring.registration;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

public class SpringWebSocketListenerRegister implements ServletContextAware, InitializingBean, WebSocketListenerRegister {

    private ServletContext servletContext;
    private WebSocketListenerRegister listenerRegister;
    
    
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        
    }



    @Override
    public void registerListener(Object listener) {
        listenerRegister.registerListener(listener);
        
    }

}
