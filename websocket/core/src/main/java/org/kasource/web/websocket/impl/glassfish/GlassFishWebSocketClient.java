package org.kasource.web.websocket.impl.glassfish;

import java.util.Map;

import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;

import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

public class GlassFishWebSocketClient extends DefaultWebSocket implements WebSocketClient {

   private WebSocketClientConfig clientConfig;
   private org.kasource.web.websocket.protocol.ProtocolHandler<String> textProtocolHandler;
   private org.kasource.web.websocket.protocol.ProtocolHandler<byte[]> binaryProtocolHandler;
   
    public GlassFishWebSocketClient(
                                    ProtocolHandler protocolHandler, 
                                    WebSocketListener[] listeners,
                                    WebSocketClientConfig clientConfig) {
        super(protocolHandler, listeners);
        this.clientConfig = clientConfig;

    }

    @Override
    public void onClose(DataFrame frame) {
        super.onClose(frame); 
        clientConfig.getManager().unregisterClient(this);
    }

    @Override
    public void onConnect() {
        super.onConnect();
        clientConfig.getManager().registerClient(this);     
    }

    @Override
    public void onMessage(String text) {
        super.onMessage(text);
        clientConfig.getManager().onWebSocketMessage(this, text);
    }

    @Override
    public void onMessage(byte[] bytes) {
        super.onMessage(bytes);
        clientConfig.getManager().onWebSocketMessage(this, bytes);
    }

   
    public void sendMessageToSocket(String message) {
        super.send(message);
    }
    
    public void sendMessageToSocket(byte[] message) {
        super.send(message);
    }
    
    
    
    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return clientConfig.getUsername();
    }



    /**
     * @return the connectionParameters
     */
    @Override
    public Map<String, String[]> getConnectionParameters() {
        return clientConfig.getConnectionParameters();
    }



    /**
     * @return the id
     */
    @Override
    public String getId() {
        return clientConfig.getClientId();
    }

    @Override
    public String getUrl() {
        return clientConfig.getUrl();
    }
    
    @Override
    public String getSubProtocol() {
        return clientConfig.getSubProtocol();
    }

    /**
     * @return the textProtocolHandler
     */
    @Override
    public org.kasource.web.websocket.protocol.ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }



    /**
     * @param textProtocolHandler the textProtocolHandler to set
     */
    @Override
    public void setTextProtocolHandler(org.kasource.web.websocket.protocol.ProtocolHandler<String> textProtocolHandler) {
        this.textProtocolHandler = textProtocolHandler;
    }



    /**
     * @return the binaryProtocolHandler
     */
    @Override
    public org.kasource.web.websocket.protocol.ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }



    /**
     * @param binaryProtocolHandler the binaryProtocolHandler to set
     */
    @Override
    public void setBinaryProtocolHandler(org.kasource.web.websocket.protocol.ProtocolHandler<byte[]> binaryProtocolHandler) {
        this.binaryProtocolHandler = binaryProtocolHandler;
    }
    
    @Override
    public void sendBinaryMessageToSocket(Object message) {
        if(binaryProtocolHandler == null) {
            throw new IllegalStateException("No binary handler configured for client");
        }
        sendMessageToSocket(binaryProtocolHandler.toMessage(message));
        
    }



    @Override
    public void sendTextMessageToSocket(Object message) {
        if(textProtocolHandler == null) {
            throw new IllegalStateException("No text handler configured for client");
        }
        sendMessageToSocket(textProtocolHandler.toMessage(message));
        
    }

}
