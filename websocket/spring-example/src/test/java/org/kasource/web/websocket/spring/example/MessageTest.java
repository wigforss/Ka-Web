package org.kasource.web.websocket.spring.example;

import java.net.URI;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.kasource.web.websocket.protocol.Base64ProtocolHandler;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

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
    
    /*
    @Test
    public void windowsNotificationTest() {
        String url = "http://db3.notify.live.net/throttledthirdparty/01.00/AAG466JKq0ULRZAnwXnhmrLOAgAAAAADAQAAAAQUZm52OjIzOEQ2NDJDRkI5MEVFMEQ";
        RestTemplate template = new RestTemplate();
        String msgTemplate =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<wp:Notification xmlns:wp=\"WPNotification\">" +
               "<wp:Toast>" +
               "<wp:Text1>%s</wp:Text1>" +
               "<wp:Text2>%s</wp:Text2>" +
               "</wp:Toast>" +
            "</wp:Notification>";
        
        String msg = String.format(msgTemplate,StringEscapeUtils.escapeXml("Swish"),
                    StringEscapeUtils.escapeXml("Mottagen betalning"));
        System.out.println(msg);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","text/xml; charset=UTF-8");
        headers.setContentLength(msg.getBytes().length);
        
        headers.set("X-MessageID", UUID.randomUUID().toString());
        headers.set("X-WindowsPhone-Target", "toast");
        headers.set("X-NotificationClass", "2");
        HttpEntity<String> entity = new HttpEntity<String>(msg, headers);
        template.postForLocation(url, entity);
        
    }
    */
}
