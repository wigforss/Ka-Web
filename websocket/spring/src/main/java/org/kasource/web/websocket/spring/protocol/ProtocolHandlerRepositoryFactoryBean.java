package org.kasource.web.websocket.spring.protocol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProtocolHandlerRepositoryFactoryBean implements FactoryBean<ProtocolHandlerRepository>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ProtocolHandlerRepository getObject() throws Exception {
        Map<String, ProtocolHandlerConfig> config = applicationContext.getBeansOfType(ProtocolHandlerConfig.class);
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        if(config != null && !config.isEmpty()) {
            for(ProtocolHandlerConfig protocolConfig : config.values()) {
                Type genericType = ((ParameterizedType) protocolConfig.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
                if(genericType.equals(String.class)) {
                    textProtocolHandlerConfig = protocolConfig;
                } else if(genericType.equals(byte[].class)) {
                    binaryProtocolHandlerConfig = protocolConfig;
                }
            }
        }
        ProtocolHandlerRepositoryImpl handler = new ProtocolHandlerRepositoryImpl();
        handler.setBinaryProtocolHandlerConfig(binaryProtocolHandlerConfig);
        handler.setTextProtocolHandlerConfig(textProtocolHandlerConfig);
        return handler;
    }

    @Override
    public Class<?> getObjectType() {
        return ProtocolHandlerRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

}
