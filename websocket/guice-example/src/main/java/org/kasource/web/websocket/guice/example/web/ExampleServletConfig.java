package org.kasource.web.websocket.guice.example.web;



import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ExampleServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new ServletModule(), new ExampleModule());
      
        injector.getInstance(ChatServer.class);
        return injector;

    }
    
    
}
