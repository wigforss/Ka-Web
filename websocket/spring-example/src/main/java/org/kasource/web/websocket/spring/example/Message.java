package org.kasource.web.websocket.spring.example;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="message")
public class Message {
    private String subject;
    private String body;
    /**
     * @return the subject
     */
    @XmlElement
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the body
     */
    @XmlElement
    public String getBody() {
        return body;
    }
    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "Message subject: " + subject+"\n" + body;
    }
}
