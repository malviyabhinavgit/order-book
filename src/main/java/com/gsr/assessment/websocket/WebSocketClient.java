package com.gsr.assessment.websocket;

import com.gsr.assessment.util.OrderBookUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebSocketClient {

    public static final String COINBASE_WS_URI = "wss://ws-feed.exchange.coinbase.com/";

    private final MessageHandler messageHandler;
    private final WebSocketClientEndpoint clientEndPoint;


    public void execute(String instrument) {
        try {
            clientEndPoint.addMessageHandler(messageHandler);
            String subscribeMessage = OrderBookUtil.subscribeMessage(instrument);
            clientEndPoint.sendMessage(subscribeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        clientEndPoint.close();
    }
}
