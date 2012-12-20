package org.kasource.web.websocket.impl.glassfish;

import java.util.Map;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocketListener;
import org.kasource.web.websocket.impl.WebSocketClient;

public class GlassFishWebSocketClient extends DefaultWebSocket implements WebSocketClient {


    private GlassFishWebSocketManager manager;
    private String id;
    private Map<String, String[]> connectionParameters;
    
    public GlassFishWebSocketClient(GlassFishWebSocketManager manager, ProtocolHandler protocolHandler, HttpRequestPacket request, WebSocketListener[] listeners, String clientId, Map<String, String[]> connectionParameters) {
        super(protocolHandler, request, listeners);
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
        manager.onWebSocketMessage(text, id);
    }

    @Override
    public void onMessage(byte[] bytes) {
        super.onMessage(bytes);
        manager.onWebSocketMessage(bytes, id);
    }

   
    public void sendMessageToSocket(String message) {
        super.send(message);
    }
    
    public void sendMessageToSocket(byte[] message) {
        super.send(message);
    }

}
