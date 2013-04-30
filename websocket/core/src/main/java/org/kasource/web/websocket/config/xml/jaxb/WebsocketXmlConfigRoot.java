//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.22 at 12:23:37 PM CEST 
//


package org.kasource.web.websocket.config.xml.jaxb;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://kasource.org/schema/websocket}orginWhitelist" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}authentication" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}textProtocolHandler" minOccurs="0"/>
 *         &lt;element ref="{http://kasource.org/schema/websocket}binaryProtocolHandler" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dynamicAddressing" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="clientIdGeneratorClass" type="{http://www.w3.org/2001/XMLSchema}string" use="optional" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orginWhitelist",
    "clientIdGenerator",
    "authentication",
    "textProtocolHandler",
    "binaryProtocolHandler"
})
@XmlRootElement(name = "websocket-config")
public class WebsocketXmlConfigRoot  {

    protected OrginWhitelistXmlConfig orginWhitelist;
    protected ClientIdGeneratorXmlConfig clientIdGenerator;
    protected AuthenticationXmlConfig authentication;
    protected ProtocolHandlerXmlConfig textProtocolHandler;
    protected ProtocolHandlerXmlConfig binaryProtocolHandler;
    @XmlAttribute
    protected boolean dynamicAddressing;


    /**
     * Gets the value of the orginWhitelist property.
     * 
     * @return
     *     possible object is
     *     {@link OrginWhitelistXmlConfig }
     *     
     */
    public OrginWhitelistXmlConfig getOrginWhitelist() {
        return orginWhitelist;
    }

    /**
     * Sets the value of the orginWhitelist property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrginWhitelistXmlConfig }
     *     
     */
    public void setOrginWhitelist(OrginWhitelistXmlConfig value) {
        this.orginWhitelist = value;
    }

    /**
     * Gets the value of the authentication property.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationXmlConfig }
     *     
     */
    public AuthenticationXmlConfig getAuthentication() {
        return authentication;
    }

    /**
     * Sets the value of the authentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationXmlConfig }
     *     
     */
    public void setAuthentication(AuthenticationXmlConfig value) {
        this.authentication = value;
    }

    /**
     * Gets the value of the textProtocolHandler property.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public ProtocolHandlerXmlConfig getTextProtocolHandler() {
        return textProtocolHandler;
    }

    /**
     * Sets the value of the textProtocolHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public void setTextProtocolHandler(ProtocolHandlerXmlConfig value) {
        this.textProtocolHandler = value;
    }

    /**
     * Gets the value of the binaryProtocolHandler property.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public ProtocolHandlerXmlConfig getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }

    /**
     * Sets the value of the binaryProtocolHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolHandlerXmlConfig }
     *     
     */
    public void setBinaryProtocolHandler(ProtocolHandlerXmlConfig value) {
        this.binaryProtocolHandler = value;
    }

    /**
     * Gets the value of the dynamicAddressing property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDynamicAddressing() {
        
        return dynamicAddressing;
        
    }

    /**
     * Sets the value of the dynamicAddressing property.
     * 
     * @param value
     *     allowed object is
     *     {@link boolean }
     *     
     */
    public void setDynamicAddressing(boolean value) {
        this.dynamicAddressing = value;
    }

  

    /**
     * @return the clientIdGenerator
     */
    public ClientIdGeneratorXmlConfig getClientIdGenerator() {
        return clientIdGenerator;
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGeneratorXmlConfig clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }

}
