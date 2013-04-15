package org.kasource.web.websocket.impl.glassfish;

import java.util.Map;


import org.kasource.web.websocket.impl.WebSocketClient;


import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.WebSocketListener;

public class GlassFishWebSocketClient extends DefaultWebSocket implements WebSocketClient {


    private GlassFishWebSocketManager manager;
    private String id;
    private Map<String, String[]> connectionParameters;
    private org.kasource.web.websocket.protocol.ProtocolHandler<String> textProtocol;
    private org.kasource.web.websocket.protocol.ProtocolHandler<byte[]> binaryProtocol;
    
    public GlassFishWebSocketClient(GlassFishWebSocketManager manager, ProtocolHandler protocolHandler, WebSocketListener[] listeners, String clientId, Map<String, String[]> connectionParameters) {
        super(protocolHandler, listeners);
        this.connectionParameters = connectionParameters;
        this.manager = manager;
        this.id = clientId;
    }

    @Override
    public void onClose(DataFrame frame) {
        super.onClose(frame); 
        manager.unregisterClient(id);
    }

    @Override
    public void onConnect() {
        super.onConnect();
        manager.registerClient(id, this, connectionParameters);     
    }

    @Override
    public void onMessage(String text) {
        super.onMessage(text);
        manager.onWebSocketMessage(text, textProtocol, id);
    }

    @Override
    public void onMessage(byte[] bytes) {
        super.onMessage(bytes);
        manager.onWebSocketMessage(bytes, binaryProtocol, id);
    }

   
    public void sendMessageToSocket(String message) {
        super.send(message);
    }
    
    public void sendMessageToSocket(byte[] message) {
        super.send(message);
    }
    
    /**
     * @param textProtocol the textProtocol to set
     */
    public void setTextProtocol(org.kasource.web.websocket.protocol.ProtocolHandler<String> textProtocol) {
        this.textProtocol = textProtocol;
    }


    /**
     * @param binaryProtocol the binaryProtocol to set
     */
    public void setBinaryProtocol(org.kasource.web.websocket.protocol.ProtocolHandler<byte[]> binaryProtocol) {
        this.binaryProtocol = binaryProtocol;
    }

}
