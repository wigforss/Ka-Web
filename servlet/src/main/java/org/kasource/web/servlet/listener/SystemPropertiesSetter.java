package org.kasource.web.servlet.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kasource.commons.util.StringUtils;

/**
 * Populates system properties with properties from a property file and / or a default list of properties.
 * 
 * Reads the context-param with context-name <i>system.properties.file</i> and loads its
 * properties into system properties.
 * 
 * Note that the context-value may contain ${variable}, which matches system properties already defined
 * or environment variables. 
 * 
 * Add this class as a context listener in web.xml.
 * 
 * {@code
 *   <context-param>   
 *       <param-name>system.properties.file</param-name>
 *       <param-value>file:${catalina.base}/conf/system.properties</param-value>
 *   </context-param>
 *   <listener>    
 *       <listener-class>org.kasource.web.servlet.listener.SystemPropertiesSetter</listener-class>  
 *   </listener>
 * 
 * }
 * 
 * Missing file will cause an exception to be thrown unless the ignore missing file
 * parameter is set to true.
 * 
 {@code
 *   <context-param>   
 *       <param-name>system.properties.file</param-name>
 *       <param-value>file:${catalina.base}/conf/system.properties</param-value>
 *   </context-param>
 *   <context-param>   
 *       <param-name>system.properties.file.ignore.missing</param-name>
 *       <param-value>true</param-value>
 *   </context-param>
 *   <listener>    
 *       <listener-class>org.kasource.web.servlet.listener.SystemPropertiesSetter</listener-class>  
 *   </listener>
 * 
 * }
 * 
 * Default system properties may also be included
 * 
 * {@code
 *   <context-param>   
 *       <param-name>system.properties.file</param-name>
 *       <param-value>file:${catalina.base}/conf/system.properties</param-value>
 *   </context-param>
 *   <context-param>
 *       <param-name>system.properties.default</param-name>
 *       <param-value>param1=value1, param2=value2, paramN=valueN</param-value>
 *   </context-param>
 *   <listener>    
 *       <listener-class>org.kasource.web.servlet.listener.SystemPropertiesSetter</listener-class>  
 *   </listener>
 * 
 * }
 * 
 * @author rikardwi
 **/
public class SystemPropertiesSetter implements ServletContextListener {

    private static final String PREFIX_WAR = "war:";
    private static final String PREFIX_CLASSPATH = "classpath:";
    private static final String PREFIX_FILE = "file:";
    private static final String PROPERTIES_FILE = "system.properties.file";
    private static final String DEFAULT_PROPERTIES = "system.properties.default";
    private static final String IGNORE_MISSING_FILE = "system.properties.file.ignore.missing";
    
    
    /**
     * Sets properties from the context-param named system.properties.file into System.properties.
     **/
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        addDefaultProperties(context);
        String propertiesFile = context.getInitParameter(PROPERTIES_FILE);
        if (propertiesFile != null) {
            propertiesFile = StringUtils.replaceVariables(propertiesFile, new HashMap<String, Object>(), true);
            final Properties propsFromFile = new Properties();
            loadProperties(propsFromFile, propertiesFile, context);
            for (String prop : propsFromFile.stringPropertyNames()) {
                String propertyValue = propsFromFile.getProperty(prop);
                if(propertyValue != null) {
                    System.setProperty(prop, propertyValue);
                }
            }
                
        }
      
        
    }

    private void addDefaultProperties(ServletContext context) {
        String properties = context.getInitParameter(DEFAULT_PROPERTIES);
        if(properties != null) {
            String[] propertyArray = properties.split(",");
            for(String property : propertyArray) {
                addProperty(property.trim());
            }
        }
        
    }
    
    private void addProperty(String property) {
        int index = property.indexOf("=");
        if(index > -1) {
            String name = property.substring(0, index);
            String value = property.substring(index+1, property.length());
            System.setProperty(name.trim(), value.trim());
        }
    }
    
    /**
     * Loads properties file into the Property object.
     * 
     * @param propsFromFile   The property object to populate
     * @param propertiesFile  Name of the file to read from.
     **/
    private void loadProperties(Properties propsFromFile, String propertiesFile, ServletContext context) {
        
        String ignorMissingFileString = context.getInitParameter(IGNORE_MISSING_FILE);
        boolean ignoreMissingFile = (ignorMissingFileString != null && ignorMissingFileString.trim().toLowerCase().equals("true"));
        try {
           if(propertiesFile.startsWith(PREFIX_FILE)) {
               propsFromFile.load(new FileInputStream(propertiesFile.substring(PREFIX_FILE.length())));
           } else if(propertiesFile.startsWith(PREFIX_CLASSPATH)){ 
                   propsFromFile.load(getClass().getResourceAsStream(propertiesFile.substring(PREFIX_CLASSPATH.length())));
           } else {
               if(propertiesFile.startsWith(PREFIX_WAR)) {
                   propertiesFile = propertiesFile.substring(PREFIX_WAR.length());
               }
               propsFromFile.load(context.getResourceAsStream(propertiesFile));
           }
           
        } catch (IOException e) {
           if(!ignoreMissingFile) {
               throw new IllegalStateException("Could not load property file " + propertiesFile);
           }
        }
    }
    
    
   
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
