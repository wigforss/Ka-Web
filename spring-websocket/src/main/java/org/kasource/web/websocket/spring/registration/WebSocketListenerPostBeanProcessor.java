package org.kasource.web.websocket.spring.registration;

import java.lang.reflect.Method;



import org.kasource.web.websocket.WebSocketMessageListener;
import org.kasource.web.websocket.annotations.WebSocketBinaryListener;
import org.kasource.web.websocket.annotations.WebSocketEventListener;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.annotations.WebSocketTextListener;
import org.kasource.web.websocket.listener.WebSocketEventMethod;
import org.kasource.web.websocket.listener.WebSocketListenerMethod;
import org.kasource.web.websocket.spring.SpringWebSocketFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * Bean Post Processor that registers @WebSocketListener, @WebSocketBinaryListener or @WebSocketTextListener annotated
 * objects or methods as listeners.
 * 
 * @author rikardwi
 **/
public class WebSocketListenerPostBeanProcessor implements BeanPostProcessor {
   
   
        @Autowired
        private SpringWebSocketFactory factory;
       
        
        @Override
        public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
            if(object.getClass().isAnnotationPresent(WebSocketListener.class)) {
                    boolean interfaceImplementationFound = false;
                    if(object instanceof WebSocketMessageListener) {
                        registerListener((WebSocketMessageListener) object);
                        interfaceImplementationFound = true;
                    }
                    if(object instanceof org.kasource.web.websocket.WebSocketEventListener) {
                        registerListener((org.kasource.web.websocket.WebSocketEventListener) object);
                        interfaceImplementationFound = true;
                    }
                    if(!interfaceImplementationFound) {
                        throw new FatalBeanException("bean " + beanName + " of class " + object.getClass() + " must implement either " +  WebSocketMessageListener.class + " or " +  org.kasource.web.websocket.WebSocketEventListener.class + " to annotated with @WebSocketListener");
                    }
            } 
            inspectMethodAnnotations(object);
            return object;
        }

      
        
        @Override
        public Object postProcessBeforeInitialization(Object object, String beanName) throws BeansException {       
            return object;
        }
        
        private void inspectMethodAnnotations(Object object) {
            Method[] methods = object.getClass().getMethods();
            for(Method method : methods) {
                if(method.isAnnotationPresent(WebSocketBinaryListener.class)) {
                    registerListener(object, method, method.getAnnotation(WebSocketBinaryListener.class).value());
                } else if(method.isAnnotationPresent(WebSocketTextListener.class)) {
                    registerListener(object, method, method.getAnnotation(WebSocketTextListener.class).value());
                } else if(method.isAnnotationPresent(WebSocketEventListener.class)) {
                    registerListener(object, method, method.getAnnotation(WebSocketEventListener.class));
                }
                
            }
            
        }
        
        private void registerListener(Object object, Method method, String socketName) {         
            factory.listenTo(socketName, new WebSocketListenerMethod(object, method));   
        }
        
        
        private void registerListener(WebSocketMessageListener listener) {
            WebSocketListener annotation = listener.getClass().getAnnotation(WebSocketListener.class);
            factory.listenTo(annotation.value(), listener);
        }
        
        private void registerListener(org.kasource.web.websocket.WebSocketEventListener listener) {
            WebSocketListener annotation = listener.getClass().getAnnotation(WebSocketListener.class);
            factory.listenTo(annotation.value(), listener);
        }
        
        private void registerListener( Object object, Method method, WebSocketEventListener annotation) {
            factory.listenTo(annotation.value(), new WebSocketEventMethod(object, method, annotation.filter()));
        }
}
