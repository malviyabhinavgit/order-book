package com.gsr.assessment.websocket;

import com.gsr.assessment.model.OrderBook;
import com.gsr.assessment.model.OrderBookNotification;
import com.gsr.assessment.service.OrderBookService;
import com.gsr.assessment.util.OrderBookUtil;
import com.gsr.assessment.util.NLevelOrderBookLogger;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WebSocketMessageHandler implements MessageHandler{
    private final OrderBookService orderBookService;

    @Override
    public void onMessage(String message) {
        String messageType = OrderBookUtil.messageType(message);
        if (OrderBookUtil.SNAPSHOT.equals(messageType)) {
            handleSnapshotMessage(message);
        } else if (OrderBookUtil.L2_UPDATE.equals(messageType)) {
            handleUpdateMessages(message);
        }
    }

    private void handleUpdateMessages(String message) {
        OrderBookNotification orderBookNotification = OrderBookUtil.buildOrderBookNotificationFromL2Update(message);
        if (orderBookService != null) {
            orderBookService.updateOrderBookForNotification(orderBookNotification);
            List<List<Float>> orderBookNLevels = orderBookService.buildOrderBookNLevels();
            NLevelOrderBookLogger.display(orderBookNLevels.get(0), orderBookNLevels.get(1));
        }
    }

    private void handleSnapshotMessage(String message) {
        OrderBook orderBook = OrderBookUtil.buildOrderBookFromSnapshot(message);
        orderBookService.createOrderBook(orderBook.getAsks(), orderBook.getBids());
        List<List<Float>> orderBookNLevels = orderBookService.buildOrderBookNLevels();
        NLevelOrderBookLogger.display(orderBookNLevels.get(0), orderBookNLevels.get(1));
    }
}
