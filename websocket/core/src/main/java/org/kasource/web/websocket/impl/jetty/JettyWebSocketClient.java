package org.kasource.web.websocket.impl.jetty;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jetty.websocket.WebSocket;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class JettyWebSocketClient implements WebSocket, WebSocket.OnBinaryMessage, WebSocket.OnTextMessage, WebSocketClient {

    private String username;
    private WebSocketManager manager;
    private Connection connection;
    private String id;
    private Map<String, String[]> connectionParameters;
    private ProtocolHandler<String> textProtocol;
    private ProtocolHandler<byte[]> binaryProtocol;
    
    public JettyWebSocketClient(WebSocketManager manager, String username, String clientId, Map<String, String[]> connectionParameters) {
        this.manager = manager;
        this.username = username;
        this.id = clientId;
        this.connectionParameters = connectionParameters;
    }
    
    @Override
    public void onMessage(String data) {
        manager.onWebSocketMessage(this, data, textProtocol);
    }

    @Override
    public void onMessage(byte[] data, int offset, int length) {
        byte[] message = new byte[length];
        System.arraycopy(message, 0, data, offset, length);
        manager.onWebSocketMessage(this, message, binaryProtocol);
       
        
    }

    @Override
    public void onClose(int closeCode, String message) {
      manager.unregisterClient(this);
        
    }

    @Override
    public void onOpen(Connection connection) {
       this.connection = connection;
       manager.registerClient(this); 
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
     * @param textProtocol the textProtocol to set
     */
    public void setTextProtocol(ProtocolHandler<String> textProtocol) {
        this.textProtocol = textProtocol;
    }




    /**
     * @param binaryProtocol the binaryProtocol to set
     */
    public void setBinaryProtocol(ProtocolHandler<byte[]> binaryProtocol) {
        this.binaryProtocol = binaryProtocol;
    }

    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }



    /**
     * @return the connectionParameters
     */
    @Override
    public Map<String, String[]> getConnectionParameters() {
        return connectionParameters;
    }



    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }


}
