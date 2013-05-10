package org.kasource.web.websocket.impl.jetty;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jetty.websocket.WebSocket;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;


public class JettyWebSocketClient implements WebSocket, WebSocket.OnBinaryMessage, WebSocket.OnTextMessage, WebSocketClient {

    private WebSocketClientConfig clientConfig;
    private Connection connection;
    private ProtocolHandler<String> textProtocolHandler;
    private ProtocolHandler<byte[]> binaryProtocolHandler;
    
    public JettyWebSocketClient( WebSocketClientConfig clientConfig) {
        this.clientConfig = clientConfig;
       
    }
    
    @Override
    public void onMessage(String data) {
        clientConfig.getManager().onWebSocketMessage(this, data);
    }

    @Override
    public void onMessage(byte[] data, int offset, int length) {
        byte[] message = new byte[length];
        System.arraycopy(message, 0, data, offset, length);
        clientConfig.getManager().onWebSocketMessage(this, message);
       
        
    }

    @Override
    public void onClose(int closeCode, String message) {
        clientConfig.getManager().unregisterClient(this);
        
    }

    @Override
    public void onOpen(Connection connection) {
       this.connection = connection;
       clientConfig.getManager().registerClient(this); 
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void sendMessageToSocket(String message) {
        try {
            getConnection().sendMessage(message);
        } catch (IOException e) {
            
        }
        
    }

    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            getConnection().sendMessage(message, 0, message.length);
        } catch (IOException e) {
           
        }
        
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
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }



    /**
     * @param textProtocolHandler the textProtocolHandler to set
     */
    @Override
    public void setTextProtocolHandler(ProtocolHandler<String> textProtocolHandler) {
        this.textProtocolHandler = textProtocolHandler;
    }



    /**
     * @return the binaryProtocolHandler
     */
    @Override
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }



    /**
     * @param binaryProtocolHandler the binaryProtocolHandler to set
     */
    @Override
    public void setBinaryProtocolHandler(ProtocolHandler<byte[]> binaryProtocolHandler) {
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
