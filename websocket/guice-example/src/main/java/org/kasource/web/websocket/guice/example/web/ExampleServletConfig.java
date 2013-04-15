package org.kasource.web.websocket.guice.example.web;

import org.kasource.web.websocket.guice.KaWebSocketModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ExampleServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        // Create Injector
        Injector injector = Guice.createInjector(new ServletModule(), new KaWebSocketModule());
        
        injector.getInstance(ChatServer.class);
        return injector;

    }
    
    


}
