package org.kasource.web.websocket.spring.config.springns;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.CHANNEL_FACTORY_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.CONFIGURER_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.CONFIG_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.MANAGER_REPO_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.POST_BEAN_PROCESSOR_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.PROTOCOL_REPO_ID;

import org.kasource.web.websocket.spring.channel.SpringWebSocketChannelFactory;
import org.kasource.web.websocket.spring.config.SpringWebSocketConfigFactoryBean;
import org.kasource.web.websocket.spring.config.SpringWebSocketConfigurer;
import org.kasource.web.websocket.spring.manager.WebSocketManagerRepositoryFactoryBean;
import org.kasource.web.websocket.spring.protocol.ProtocolHandlerRepositoryFactoryBean;
import org.kasource.web.websocket.spring.registration.WebSocketListenerPostBeanProcessor;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConfigurerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
 
    /**
     * Returns SpringWebSocketConfigurer.
     * 
     * @param element websocket XML element.
     * 
     * @return SpringWebSocketConfigurer class.
     **/
    protected Class<?> getBeanClass(Element element) {
        return SpringWebSocketConfigurer.class;
    }
    
    /**
     * parse the websocket XML element.
     * 
     * @param element websocket XML element.
     * @param pc      Parser context.
     * @param bean    Bean definition.
     **/
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, CONFIGURER_ID);
        bean.addPropertyReference("channelFactory", CHANNEL_FACTORY_ID);
        bean.setInitMethodName("configure");
        bean.addConstructorArgReference(CONFIG_ID);
        bean.setLazyInit(false);
      
        createBeans(pc, element);

    }
    
    /**
     * Create beans needed by Ka-websocket.
     * 
     * @param pc           Parser Context.
     **/
    private void createBeans(ParserContext pc, Element element) {
        createProtocolRepository(pc);
        createManagerRepository(pc);
        createChannelFactory(pc);
        createPostBeanProcessor(pc);
        createConfig(pc, element);
    }
    
    private void createProtocolRepository(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .rootBeanDefinition(
                            ProtocolHandlerRepositoryFactoryBean.class);
        builder.setLazyInit(false);
        
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                    .getBeanDefinition(), PROTOCOL_REPO_ID));
    }
    
    private void createManagerRepository(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(WebSocketManagerRepositoryFactoryBean.class);
       
        builder.setLazyInit(false);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), MANAGER_REPO_ID));
    }
    
    private void createConfig(ParserContext pc, Element element) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(SpringWebSocketConfigFactoryBean.class);
        builder.addPropertyReference("channelFactory", CHANNEL_FACTORY_ID);
        builder.addPropertyReference("managerRepository", MANAGER_REPO_ID);
        builder.addPropertyReference("protocolHandlerRepository", PROTOCOL_REPO_ID);
        builder.setLazyInit(false);
        
       
        if(element.getAttribute("clientIdGeneratorRef") != null) {
             String ref = element.getAttribute("clientIdGeneratorRef");
             builder.addPropertyReference("clientIdGenerator", ref);
        }
            
        
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), CONFIG_ID));
    }
    
    private void createChannelFactory(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .rootBeanDefinition(SpringWebSocketChannelFactory.class);
    builder.setLazyInit(false);
    pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), CHANNEL_FACTORY_ID));
    }
    
    private void createPostBeanProcessor(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(WebSocketListenerPostBeanProcessor.class);
        builder.setLazyInit(false);
        builder.addDependsOn(CHANNEL_FACTORY_ID);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                .getBeanDefinition(), POST_BEAN_PROCESSOR_ID));
    }
}
