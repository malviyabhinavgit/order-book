package com.gsr.assessment.config;

import com.gsr.assessment.service.OrderBookService;
import com.gsr.assessment.service.OrderBookServiceImpl;
import com.gsr.assessment.websocket.MessageHandler;
import com.gsr.assessment.websocket.WebSocketClient;
import com.gsr.assessment.websocket.WebSocketClientEndpoint;
import com.gsr.assessment.websocket.WebSocketMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class OrderBookConfig {

    @Bean
    public OrderBookService orderBookService() {
        return new OrderBookServiceImpl();
    }

    @Bean
    public MessageHandler messageHandler(OrderBookService orderBookService) {
        return new WebSocketMessageHandler(orderBookService);
    }

    @Bean
    public WebSocketClientEndpoint webSocketClientEndpoint() throws URISyntaxException {
        return new WebSocketClientEndpoint(new URI(WebSocketClient.COINBASE_WS_URI));
    }

    @Bean
    public WebSocketClient webSocketClient(WebSocketClientEndpoint webSocketClientEndpoint, MessageHandler messageHandler) throws URISyntaxException {
        return new WebSocketClient(messageHandler, webSocketClientEndpoint);
    }


}
