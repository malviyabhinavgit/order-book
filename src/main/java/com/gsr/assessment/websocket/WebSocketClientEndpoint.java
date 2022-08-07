package com.gsr.assessment.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
@Slf4j
@ClientEndpoint
public class WebSocketClientEndpoint {

    Session userSession;
    private MessageHandler messageHandler;

    public WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            userSession = container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (userSession != null) {
            try {
                log.info("Closing WebSocket session");
                userSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        log.info("opening WebSocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        log.info("onClose: " + reason.toString());
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (this.messageHandler != null) {
            this.messageHandler.onMessage(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable ex) {
        log.info("WebSocket error session :{}, message:{}", session, ex.getMessage());
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
}