package com.gsr.assessment.service;

import com.gsr.assessment.model.Order;
import com.gsr.assessment.model.OrderBook;
import com.gsr.assessment.model.OrderBookNotification;
import com.gsr.assessment.util.OrderBookUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class OrderBookServiceImpl implements OrderBookService {
    private OrderBook orderBook;

    @Override
    public void createOrderBook(Map<Float, Float> asksPar, Map<Float, Float> bidsPar) {
        orderBook = new OrderBook(asksPar, bidsPar);
    }
    @Override
    public void updateOrderBookForNotification(OrderBookNotification orderBookNotification) {
        processOrders(orderBookNotification.getBuyUpdates());
        processOrders(orderBookNotification.getSellUpdates());
    }

    private void processOrders(List<Order> orders) {
        orders.forEach(order -> {
            if (order.getSize() == 0) {
                this.orderBook.getBids().remove(order.getPrice());
            } else {
                this.orderBook.getBids().put(order.getPrice(), order.getSize());
            }
        });
    }

    @Override
    public List<List<Float>> buildOrderBookNLevels() {
        List<Float> asks = getNLevelData(this.orderBook.getAsks());
        List<Float> bids = getNLevelData(this.orderBook.getBids());
        return Arrays.asList(asks, bids);
    }

    private List<Float> getNLevelData(SortedMap<Float, Float> prices) {
        return prices.keySet().stream().limit(OrderBookUtil.NUMBER_OF_LEVELS).collect(Collectors.toList());
    }
}
