package org.kasource.web.websocket.guice.example.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGeneratorImpl;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.guice.KaWebSocketModule;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class ExampleModule extends AbstractModule {

    @Override
    protected void configure() {
       KaWebSocketModule websocket = new KaWebSocketModule();
       install(websocket);      
    }
    
  
   @Provides
   AuthenticationProvider getAuthenticationProvider() {
       return new PassthroughAutenticationProvider();
   }
  
   @Provides @Named("jsonHandler")
   TextProtocolHandler getTextProtocolHandler() {
       return new JsonProtocolHandler();
   }
   
   @Provides
   ClientIdGenerator getClientIdGenerator() {
       return new ClientIdGeneratorImpl();
   }
   
   @Provides @Named("chatServlet")
   WebSocketServletConfigImpl getChatServlet(WebSocketServletConfigImpl servletConfig) {
       servletConfig.setServletName("chat");
       servletConfig.setDynamicAddressing(false);
      
       return servletConfig;
   }
   
   @Provides @Singleton
   OriginWhiteListConfig getOriginWhiteListConfig() {
       OriginWhiteListConfig list = new OriginWhiteListConfig();
       Set<String> origins = new HashSet<String>();
       origins.add("http://localhost:8080");
       list.setOriginWhiteList(origins);
       return list;
   }
   
  
   
   @Provides @Singleton
   public TextProtocolHandlerConfigImpl getTextProtocolHandlerConfig(@Named("jsonHandler") TextProtocolHandler textProtocolHandler) {
       TextProtocolHandlerConfigImpl config = new TextProtocolHandlerConfigImpl();
       Map<String, ProtocolHandler<String>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<String>>();
       defaultProtocolUrlMap.put("/chat", textProtocolHandler);
       config.setDefaultProtocolUrlMap(defaultProtocolUrlMap);
       return config;
   }
   
   @Provides @Singleton
   AuthenticationConfig getAuthenticationConfig(AuthenticationProvider authenticationProvider) {
       AuthenticationConfig auth = new AuthenticationConfig();
       Map<String, AuthenticationProvider> authenticationUrlMapping = new HashMap<String, AuthenticationProvider>();
       authenticationUrlMapping.put("/chat", authenticationProvider);
       auth.setAuthenticationUrlMapping(authenticationUrlMapping);
       return auth;
   }
   
}
