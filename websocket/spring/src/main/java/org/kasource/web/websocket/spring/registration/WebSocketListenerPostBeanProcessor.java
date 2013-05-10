package org.kasource.web.websocket.spring.registration;

import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.ServletContextAware;


/**
 * Bean Post Processor that registers @WebSocketListener, @WebSocketBinaryListener or @WebSocketTextListener annotated
 * objects or methods as listeners.
 * 
 * @author rikardwi
 **/
public class WebSocketListenerPostBeanProcessor implements BeanPostProcessor, ServletContextAware, InitializingBean {
   
       private ServletContext servletContext;
       private WebSocketListenerRegister listenerRegister;
       private Queue<Object> beans = new LinkedList<Object>(); 
        
        
        
        @Override
        public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
            if(listenerRegister == null) {
                beans.add(object);
            } else {
                listenerRegister.registerListener(object);
            }
            return object;
        }

      
        
        @Override
        public Object postProcessBeforeInitialization(Object object, String beanName) throws BeansException {       
            return object;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            if(listenerRegister == null) {
                listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
            }
            if(!beans.isEmpty()) {
                while(!beans.isEmpty()) {
                    listenerRegister.registerListener(beans.poll());
                }
            }
            
            
        }

        @Override
        public void setServletContext(ServletContext servletContext) {
           this.servletContext = servletContext;
            
        }



        /**
         * @param listenerRegister the listenerRegister to set
         */
        public void setListenerRegister(WebSocketListenerRegister listenerRegister) {
            this.listenerRegister = listenerRegister;
        }
        
        
        
        
        
}
