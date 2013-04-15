package org.kasource.web.websocket.impl.jetty;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jetty.websocket.WebSocket;
import org.kasource.web.websocket.impl.WebSocketClient;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class JettyWebSocketClient implements WebSocket, WebSocket.OnBinaryMessage, WebSocket.OnTextMessage, WebSocketClient {

    
    private JettyWebSocketManager manager;
    private Connection connection;
    private String id;
    private Map<String, String[]> connectionParameters;
    private ProtocolHandler<String> textProtocol;
    private ProtocolHandler<byte[]> binaryProtocol;
    
    public JettyWebSocketClient(JettyWebSocketManager manager, String clientId, Map<String, String[]> connectionParameters) {
        this.manager = manager;
        this.id = clientId;
        this.connectionParameters = connectionParameters;
    }
    
    @Override
    public void onMessage(String data) {
        manager.onWebSocketMessage(data, textProtocol, data);
    }

    @Override
    public void onMessage(byte[] data, int offset, int length) {
        byte[] message = new byte[length];
        System.arraycopy(message, 0, data, offset, length);
        manager.onWebSocketMessage(message, binaryProtocol, id);
       
        
    }

    @Override
    public void onClose(int closeCode, String message) {
      manager.unregisterClient(id);
        
    }

    @Override
    public void onOpen(Connection connection) {
       this.connection = connection;
       manager.registerClient(id, this, connectionParameters); 
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
  
    

}
