package org.kasource.web.servlet.listener.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.kasource.commons.util.StringUtils;
import org.kasource.web.servlet.listener.properties.config.Property;
import org.kasource.web.servlet.listener.properties.config.SystemProperties;

/**
 * Populates system properties with properties from a property file and / or a list of properties.
 * 
 * Reads the context-param with context-name <i>system.properties.config</i> to locate
 * the configuration file. If no such parameter is found the default location /WEB-INF/system-properties.xml is assumed.
 * 
 * Just add this class as a listener in web.xml.
 * 
 * {@code
 *   <listener>    
 *       <listener-class>org.kasource.web.servlet.listener.SystemPropertiesSetter</listener-class>  
 *   </listener>
 * 
 * }
 * 
 * This class supports the file prefixes <b>file:</b>, <b>classpath:</b> and <b>war:</b>. If no prefix is used war resource is assumed.
 * 
 * Example: web.xml for configuring the location of the configuration file.
 * 
 * {@code
 *   <context-param>   
 *       <param-name>system.properties.config</param-name>
 *       <param-value>classpath:/conf/system.properties</param-value>
 *   </context-param>
 *   <listener>    
 *       <listener-class>org.kasource.web.servlet.listener.SystemPropertiesSetter</listener-class>  
 *   </listener>
 * 
 * }
 * 
 * The configuration file may contain a list of properties as well a file location of a properties file. The file 
 * attribute may contain ${variable}, which matches system properties already defined or environment variables. 
 * 
 * Example: Add the following content to /WEB-INF/system-properties.xml
 * 
 * {@code
 * <systemProperties xmlns="http://kasource.org/schema/ka-web/listener/properties"
 *                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *                 xsi:schemaLocation="http://kasource.org/schema/ka-web/listener/properties system-properties-config.xsd"
 *                 file="file:${catalina.base}/conf/my-properties.properties"
 *                 ignoreMissingFile="false"
 *                 overrideProperties="false">
 *   <property name="property1" value="value1"/>
 *   <property name="property2" value="value2"/>
 * </systemProperties>
 * 
 * }
 * 
 * 
 * @author rikardwi
 **/
public class SystemPropertiesSetter implements ServletContextListener {

    private static final String PREFIX_WAR = "war:";
    private static final String PREFIX_CLASSPATH = "classpath:";
    private static final String PREFIX_FILE = "file:";
    private static final String PROPERTIES_CONFIG = "system.properties.config";
    private static final String PROPERTIES_CONFIG_DEFAULT = "war:/WEB-INF/system-properties.xml";
    
    
    /**
     * Sets properties from the context-param named system.properties.file into System.properties.
     **/
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String propertiesConfig = context.getInitParameter(PROPERTIES_CONFIG);
        if(propertiesConfig == null) {
            propertiesConfig = PROPERTIES_CONFIG_DEFAULT;
        }
        SystemProperties config = loadConfiguration(propertiesConfig, context);
        addProperties(config);
        if(config.getFile() != null) {
            addPropertiesFromFile(config, context);
        }
        
    }

    /**
     * Loads and returns the configuration.
     * 
     * @param propertiesConfig  Location of the configuration XML file.
     * 
     * @param context  Servlet Context to read configuration from.
     * 
     * @return the configuration object.
     * 
     * @throws IllegalStateException If configuration could not be read due to missing file or invalid XML content in
     *                               the configuration file.
     **/
    private SystemProperties loadConfiguration(String propertiesConfig, ServletContext context) throws IllegalStateException{
        InputStream inStream = null;
        try {
            inStream = getStreamForLocation(propertiesConfig, context);
            
            JAXBContext jaxb = JAXBContext.newInstance(SystemProperties.class.getPackage().getName());
            return (SystemProperties) jaxb.createUnmarshaller().unmarshal(inStream);
        } catch (FileNotFoundException e) {
           throw new IllegalStateException("Could not find configuration file: " + propertiesConfig, e);
        } catch (JAXBException e) {
            throw new IllegalStateException("Error while reading file: " + propertiesConfig, e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    /**
     * Returns the InputStream to the supplied file location
     * 
     * @param location  File location, may contain file:, classpath: or war: prefix. If no prefix is used war location is assumed.
     * @param context  Servlet Context to read war resources from.
     * 
     * @throws FileNotFoundException If a file resource could not be found. 
     **/
    private InputStream getStreamForLocation(final String location, final ServletContext context) throws FileNotFoundException {
        String fileLocation = StringUtils.replaceVariables(location, new HashMap<String, Object>(), true);  
           if (fileLocation.startsWith(PREFIX_FILE)) {
              return new FileInputStream(fileLocation.substring(PREFIX_FILE.length()));
           } else if (fileLocation.startsWith(PREFIX_CLASSPATH)){ 
                  return getClass().getResourceAsStream(fileLocation.substring(PREFIX_CLASSPATH.length()));
           } else {
               if (fileLocation.startsWith(PREFIX_WAR)) {
                   fileLocation = fileLocation.substring(PREFIX_WAR.length());
               }
              return context.getResourceAsStream(fileLocation);
           }  
    }
    
    /**
     * Adds properties from the configuration to system properties.
     * 
     * @param config Configuration object.
     **/
    private void addProperties(final SystemProperties config) {     
        for(Property property : config.getProperty()) {
            String name = property.getName().trim();
            String value = property.getValue().trim();
            if(config.isOverrideProperties() || (System.getProperty(name) == null)) {
                System.setProperty(name, value);
            }
        }
       
    }
    
    /**
     * Adds properties from the properties file configured in the configuration object. 
     * 
     * @param config Configuration object.
     **/
    private void addPropertiesFromFile(final SystemProperties config, final ServletContext context) {
        String fileLocation = StringUtils.replaceVariables(config.getFile(), new HashMap<String, Object>(), true);
        InputStream inStream = null;
        try {
            inStream = getStreamForLocation(fileLocation, context);
            Properties propsFromFile = new Properties();
            propsFromFile.load(inStream);
            for (String prop : propsFromFile.stringPropertyNames()) {
                String propertyValue = propsFromFile.getProperty(prop);
                if (propertyValue != null && (config.isOverrideProperties() || (System.getProperty(prop) == null))) {
                    System.setProperty(prop, propertyValue);
                }
            }
        } catch (FileNotFoundException e) {
            if (!config.isIgnoreMissingFile()) { 
                throw new IllegalStateException("Could not find properties file: " + fileLocation, e);
            }
        } catch (IOException e) {
            if (!config.isIgnoreMissingFile()) { 
                throw new IllegalStateException("Error occurred when reading properties file: " + fileLocation, e);
            }
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
   
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
