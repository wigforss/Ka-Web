package org.kasource.web.websocket.register;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.annotations.OnWebSocketEvent;
import org.kasource.web.websocket.annotations.OnWebSocketMessage;
import org.kasource.web.websocket.annotations.WebSocketEventAnnotation;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.event.listeners.WebSocketBinaryMessageListener;
import org.kasource.web.websocket.event.listeners.WebSocketClientConnectionListener;
import org.kasource.web.websocket.event.listeners.WebSocketClientDisconnectedListener;
import org.kasource.web.websocket.event.listeners.WebSocketTextMessageListener;
import org.kasource.web.websocket.listener.WebSocketBinaryMessageHandler;
import org.kasource.web.websocket.listener.WebSocketClientConnectedHandler;
import org.kasource.web.websocket.listener.WebSocketClientDisconnectedHandler;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.WebSocketEventMethod;
import org.kasource.web.websocket.listener.WebSocketMessageMethod;
import org.kasource.web.websocket.listener.WebSocketTextMessageHandler;
import org.kasource.web.websocket.listener.extension.ExtendedWebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation that registers listeners to web socket events.
 * 
 * Both annotation and interface configuration is supported, @See WebSocketListenerRegister.
 * 
 * @author rikardwi
 **/
public class WebSocketListenerRegisterImpl implements WebSocketListenerRegister {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketListenerRegisterImpl.class); 
    
    private WebSocketChannelFactory webSocketFactory;
    
    public WebSocketListenerRegisterImpl(ServletContext servletContext) {
        
        this.webSocketFactory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());;
    }
    
    /**
     * Registers the listener object if its configuration is applicable for 
     * Web Socket listening.
     * 
     * @param listener Object to inspect and register for event listener if properly configured.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    @Override
    public void registerListener(Object listener) throws IllegalArgumentException {
        WebSocketListener listenerAnnotation = listener.getClass().getAnnotation(WebSocketListener.class);
        String webSocketChannelName = null;
        if (listenerAnnotation == null) {
            // Find out of the WebSocketListenerRegistration is implemented
            if(listener instanceof WebSocketListenerRegistration) {
                webSocketChannelName = ((WebSocketListenerRegistration) listener).getWebSocketChannelName();
            }
        } else {
            webSocketChannelName = listenerAnnotation.value();
        }
        if (webSocketChannelName != null) {
            registerListener(listener, webSocketChannelName);
        }
    }
    
    /**
     * Registers the listener object to the WebSocketChannel named webSocketChannelName if its configuration is applicable for 
     * Web Socket listening.
     * 
     * @param listener Object to inspect and register for event listener if properly configured.
     * @param webSocketChannelName Name of the WebSocketChannel to register the listener at.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    private void registerListener(Object listener, String webSocketChannelName) throws IllegalArgumentException {
        if(WebSocketEventListener.class.isAssignableFrom(listener.getClass()))  {
            webSocketFactory.listenTo(webSocketChannelName, (WebSocketEventListener) listener);   
        }
        if(WebSocketBinaryMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(webSocketChannelName, new WebSocketBinaryMessageHandler((WebSocketBinaryMessageListener) listener));   
        }
        if(WebSocketTextMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(webSocketChannelName, new WebSocketTextMessageHandler((WebSocketTextMessageListener) listener));   
        }
        if(WebSocketClientConnectionListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(webSocketChannelName, new WebSocketClientConnectedHandler((WebSocketClientConnectionListener) listener));   
        }
        if(WebSocketClientDisconnectedListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(webSocketChannelName, new WebSocketClientDisconnectedHandler((WebSocketClientDisconnectedListener) listener));   
        }
        registerAnnotatedMethods(listener, webSocketChannelName);
    }
    
    
    /**
     * Finds and registers all eligible annotated methods as separate listeners.
     * 
     * @param listener               Listener object to register.
     * @param webSocketChannelName   Name of the WebSocketChannel to register at.
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    private void registerAnnotatedMethods(Object listener, String webSocketChannelName) throws IllegalArgumentException {
        Method[] methods = listener.getClass().getMethods();
        for(Method method : methods) {
            if (webSocketChannelName != null || method.isAnnotationPresent(WebSocketListener.class)) {
                WebSocketListener methodListerAnnotation = method.getAnnotation(WebSocketListener.class);
               
                if(methodListerAnnotation != null) {
                    webSocketChannelName = methodListerAnnotation.value();
                }
                
                if( method.isAnnotationPresent(OnWebSocketEvent.class)) {
                    registerListener(webSocketChannelName, new WebSocketEventMethod(listener, method));
                } else if(method.isAnnotationPresent(OnWebSocketMessage.class)) { 
                    registerListener(webSocketChannelName, new WebSocketMessageMethod(listener, method));
                } else {
                    Annotation[] annotations = method.getAnnotations(); 
                
                    for(Annotation annotation : annotations) {
                        if(annotation.annotationType().isAnnotationPresent(WebSocketEventAnnotation.class)) {
                            registerExtensionListener(method, listener, annotation, webSocketChannelName);
                            break;
                        }
                    }
               
                }
            }
        }
        
    }
    
    /**
     * Register a listener method that is annotated with a extension annotation, if the annotation contains an
     * ExtendedWebSocketEventListener in its value field.
     * 
     * @param method               Method annotated with the extension annotation.
     * @param listener             Listener object to register.
     * @param annotation           The extension annotation to use. 
     * @param socketChannelName    Name of the WebSocketChannel to register at.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     */
    private void registerExtensionListener(Method method, Object listener, Annotation annotation, String socketChannelName) throws IllegalArgumentException{
       
            try {
                Method valueMethod = annotation.annotationType().getMethod("value");
                Object value = valueMethod.invoke(annotation);
                if(value instanceof Class && ExtendedWebSocketEventListener.class.isAssignableFrom((Class<?>) value)) {
                    @SuppressWarnings("unchecked")
                    ExtendedWebSocketEventListener extension = createExtensionInstance((Class<? extends ExtendedWebSocketEventListener>) value, method, listener, annotation);      
                    webSocketFactory.listenTo(socketChannelName, extension);   
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not get value from " + annotation.annotationType(), e);
            } 
            
    }
    
    
    
    /**
     * Creates an new instance of the extension handler.
     * 
     * @param extensionClass Class of the extension.
     * @param method         Method annotated with the extension annotation.
     * @param listener       Listener object to register.
     * @param annotation     The annotation the contains the extension configuration.
     * 
     * @return A new instance of the provided extensionClass as an ExtendedWebSocketEventListener.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     */
    private ExtendedWebSocketEventListener createExtensionInstance(Class<? extends ExtendedWebSocketEventListener> extensionClass,
                Method method, Object listener, Annotation annotation) throws IllegalArgumentException {
        
        try {
            ExtendedWebSocketEventListener extension = extensionClass.newInstance();
            Method initialize = extension.getClass().getMethod("initialize");
            Method setMethod = extension.getClass().getMethod("setMethod", Method.class);
            Method setAnnotation = extension.getClass().getMethod("setAnnotation", Annotation.class);
            Method setListener = extension.getClass().getMethod("setListener", Object.class);
            setMethod.invoke(extension, method);
            setAnnotation.invoke(extension, annotation);
            setListener.invoke(extension, listener);
            initialize.invoke(extension);
            return extension;
        } catch (Exception e) {
            LOG.warn("Could not create instance or initialize extension " + extensionClass, e);
           throw new IllegalArgumentException("Could not create instance or initialize extension " + extensionClass, e);
        } 
       
        
    }
    
    private void registerListener(String channelName, WebSocketEventListener listener) {         
        webSocketFactory.listenTo(channelName, listener);   
    }
    
   
}
