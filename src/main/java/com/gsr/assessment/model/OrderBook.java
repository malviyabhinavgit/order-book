package com.gsr.assessment.model;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class OrderBook {
    private  SortedMap<Float, Float> asks;
    private  SortedMap<Float, Float> bids;

    public SortedMap<Float, Float> getAsks() {
        return asks;
    }

    public void setAsks(SortedMap<Float, Float> asks) {
        this.asks = asks;
    }

    public SortedMap<Float, Float> getBids() {
        return bids;
    }

    public void setBids(SortedMap<Float, Float> bids) {
        this.bids = bids;
    }

    public OrderBook(Map<Float, Float> asksPar, Map<Float, Float> bidsPar) {
        asks = new TreeMap<>(asksPar);
        bids = new TreeMap<>(Collections.reverseOrder());
        bids.putAll(bidsPar);
    }
}
