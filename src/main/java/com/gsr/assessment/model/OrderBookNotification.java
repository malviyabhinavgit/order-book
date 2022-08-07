package com.gsr.assessment.model;

import java.util.List;

public class OrderBookNotification {
    private final List<Order> buyUpdates;
    private final List<Order> sellUpdates;

    public OrderBookNotification(List<Order> buyUpdatesPar, List<Order> sellUpdatesPar) {
        buyUpdates = buyUpdatesPar;
        sellUpdates = sellUpdatesPar;
    }

    public List<Order> getBuyUpdates() {
        return buyUpdates;
    }

    public List<Order> getSellUpdates() {
        return sellUpdates;
    }
}
