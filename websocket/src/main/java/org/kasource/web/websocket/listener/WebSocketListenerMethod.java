package org.kasource.web.websocket.listener;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.kasource.web.websocket.WebSocketMessageListener;

public class WebSocketListenerMethod implements WebSocketMessageListener {

    private Object listener;
    private Method method;
    private boolean isBinary;
    
    
    public WebSocketListenerMethod(Object listener, Method method) {
        if(method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Message Listener method must be public");
        }
        Class<?>[] params = method.getParameterTypes();
        if(params.length != 2) {
            throw new IllegalArgumentException("WebSocket Message Listener method must have two parameters");
        }
        if(!params[1].equals(String.class)) {
            throw new IllegalArgumentException("WebSocket Message Listeners method second argument have to be a String");
        }
        if(params[0].equals(String.class)) {
            isBinary = false;
        } else if(params[0].equals(byte[].class)) {
            isBinary = true;
        } else {
            throw new IllegalArgumentException("WebSocket Message Listeners method first argument have to be a String or an byte array");
        }
        this.method = method;
        this.listener = listener;
    }


    @Override
    public void onMessage(String message, String clientId) {
        if(!isBinary) {
            try {
                method.invoke(listener, message, clientId);
            } catch (IllegalArgumentException e) {
               
            } catch (IllegalAccessException e) {
             
            } catch (InvocationTargetException e) {
              
            }
        }
        
    }


    @Override
    public void onBinaryMessage(byte[] message, String clientId) {
       if(isBinary) {
           try {
            method.invoke(listener, message, clientId);
        } catch (IllegalArgumentException e) {
           
        } catch (IllegalAccessException e) {
           
        } catch (InvocationTargetException e) {
           
        }
       }
        
    }
}
