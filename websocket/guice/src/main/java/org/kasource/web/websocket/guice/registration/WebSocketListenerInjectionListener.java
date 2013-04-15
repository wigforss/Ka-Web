package org.kasource.web.websocket.guice.registration;



import java.util.Deque;
import java.util.LinkedList;


import javax.servlet.ServletContext;

import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;


import com.google.inject.Inject;
import com.google.inject.spi.InjectionListener;


public class WebSocketListenerInjectionListener implements InjectionListener<Object> {

    private WebSocketListenerRegister listenerRegister;
    private Deque<Object> listeners = new LinkedList<Object>();
  
   
    @Override
    public void afterInjection(Object injectee) {
        if(listenerRegister == null) {
            listeners.add(injectee);     
        } else {
            listenerRegister.registerListener(injectee); 
        }
    }
        
    
    
   @Inject
    public void initialize(ServletContext servletContext) {      
        this.listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        Object listener = null;
        while((listener = listeners.pollFirst()) != null) {
            listenerRegister.registerListener(listener);          
        }
                
    }
    
   
}
