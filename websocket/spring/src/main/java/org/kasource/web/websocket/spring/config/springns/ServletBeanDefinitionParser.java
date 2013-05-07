package org.kasource.web.websocket.spring.config.springns;

import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.MANAGER_REPO_ID;
import static org.kasource.web.websocket.spring.config.springns.WebSocketXmlNamespaceHandler.PROTOCOL_REPO_ID;

import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServletBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    protected Class<?> getBeanClass(Element element) {
        return WebSocketServletConfigImpl.class;
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
        String servletName = element.getAttribute("servletName");
        element.setAttribute(ID_ATTRIBUTE, servletName + "Config");
        bean.addPropertyValue("servletName", servletName);
        bean.addPropertyValue("dynamicAddressing", "true".equals(element.getAttribute("dynamicAddressing")));
        bean.addPropertyReference("managerRepository", MANAGER_REPO_ID);
        bean.addPropertyReference("protocolRepository", PROTOCOL_REPO_ID);
        String ref = element.getAttribute("clientIdGeneratorRef");
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("clientIdGenerator", ref);
        }
      
    }
    
  
}
