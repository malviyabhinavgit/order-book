package com.gsr.assessment.service;

import com.gsr.assessment.model.OrderBookNotification;

import java.util.List;
import java.util.Map;

public interface OrderBookService {
    public void createOrderBook(Map<Float, Float> asksPar, Map<Float, Float> bidsPar);

    public void updateOrderBookForNotification(OrderBookNotification orderBookNotification);

    public List<List<Float>> buildOrderBookNLevels();

}
