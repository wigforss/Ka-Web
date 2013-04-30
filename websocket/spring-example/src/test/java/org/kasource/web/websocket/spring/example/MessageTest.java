package org.kasource.web.websocket.spring.example;

import org.junit.Test;
import org.kasource.web.websocket.protocol.Base64ProtocolHandler;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;

public class MessageTest {

    @Test
    public void json() {
        Message message = new Message();
        message.setSubject("The subject");
        message.setBody("Body of message");
        TextProtocolHandler json = new JsonProtocolHandler();
        System.out.println(json.toMessage(message));
    }
    
    @Test
    public void xml() {
        Message message = new Message();
        message.setSubject("The subject");
        message.setBody("Body of message");
        TextProtocolHandler xml = new XmlProtocolHandler();
        System.out.println(xml.toMessage(message));
        
        System.out.println(xml.toObject("<message><body>Body of message</body><subject>The subject</subject></message>", Message.class));
    }
    
    @Test
    public void base64() {
        TextProtocolHandler base64 = new Base64ProtocolHandler();
        String message = base64.toMessage("Hej".getBytes()); //"SGVq";
      
        byte[] base64Bytes = base64.toObject(message, byte[].class);
        System.out.println(new String(base64Bytes));
        
    }
}
