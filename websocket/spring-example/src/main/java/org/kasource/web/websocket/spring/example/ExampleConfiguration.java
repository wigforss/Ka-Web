package org.kasource.web.websocket.spring.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGeneratorImpl;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;
import org.kasource.web.websocket.spring.config.configuration.SpringKaWebSocket;


@Configuration
@Import(SpringKaWebSocket.class)
public class ExampleConfiguration {

    
    @Bean(name = "jsonProtocol")
    public TextProtocolHandler getJsonProtocol() {
        return new JsonProtocolHandler();
    }
    
    @Bean(name = "idGenerator")
    public ClientIdGenerator getClientIdGenerator() {
        return new ClientIdGeneratorImpl();
    }
    
    @Bean(name = "originWhitelist")
    public OriginWhiteListConfig getOriginWhiteListConfig() {
        OriginWhiteListConfig originWhitelist = new OriginWhiteListConfig();
        Set<String> whitelist = new HashSet<String>();
        whitelist.add("http://localhost:8080");
        originWhitelist.setOriginWhiteList(whitelist);
        return originWhitelist;
    }
    
    @Bean(name ="authenticationProvider")
    public AuthenticationProvider getAuthenticationProvider() {
        return new PassthroughAutenticationProvider();
    }
    
    @Autowired
    @Bean(name = "textProtocolConfig")
    public TextProtocolHandlerConfigImpl getTextProtocolHandlerConfig(TextProtocolHandler textProtocolHandler) {
        TextProtocolHandlerConfigImpl config = new TextProtocolHandlerConfigImpl();
        Map<String, ProtocolHandler<String>> defaultProtocolUrlMap = new HashMap<String, ProtocolHandler<String>>();
        defaultProtocolUrlMap.put("/chat/*", textProtocolHandler);
        config.setDefaultProtocolUrlMap(defaultProtocolUrlMap);
        return config;
    }
    
    @Autowired
    @Bean(name = "auhernticationConfig")
    public AuthenticationConfig getAuthenticationConfig(AuthenticationProvider authenticationProvider) {
        AuthenticationConfig auth = new AuthenticationConfig();
        Map<String, AuthenticationProvider> authenticationUrlMapping = new HashMap<String, AuthenticationProvider>();
        authenticationUrlMapping.put("/chat/*", authenticationProvider);
        auth.setAuthenticationUrlMapping(authenticationUrlMapping);
        return auth;
    }
    
    
    @Autowired
    @Bean(name = "chatConfig")
    public WebSocketServletConfig getChatWebSocketServletConfig(WebSocketServletConfigImpl prototypeConfig, WebSocketConfigImpl config) {
        prototypeConfig.setDynamicAddressing(true);
        prototypeConfig.setServletName("chat");
        config.registerServlet(prototypeConfig);
        return prototypeConfig;
    }
}
