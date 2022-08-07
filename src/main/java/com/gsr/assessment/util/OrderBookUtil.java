package com.gsr.assessment.util;

import com.gsr.assessment.model.Order;
import com.gsr.assessment.model.OrderBook;
import com.gsr.assessment.model.OrderBookNotification;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderBookUtil {
    public static final String ASKS = "asks";
    public static final String BIDS = "bids";
    public static final String SNAPSHOT = "snapshot";
    public static final String L2_UPDATE = "l2update";
    public static final String CHANGES = "changes";
    public static final String BUY_UPDATE = "buy";
    public static final String SELL_UPDATE = "sell";
    public static final Integer NUMBER_OF_LEVELS = 10;
    public static final JSONParser parser = new JSONParser();

    public static String messageType(String response) {
        String responseType = null;
        // Sanity check - fail-fast
        if (response == null) {
            return null;
        }
        try {
            JSONObject jsonObject = (JSONObject) OrderBookUtil.parser.parse(response);
            responseType = (String) jsonObject.get("type");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return responseType;
    }


    @SuppressWarnings("unchecked")
    public static String subscribeMessage(String instrument) {
        // Sanity check - fail-fast
        if (instrument == null || instrument.trim().length() == 0) {
            log.error("Instrument name is required.");
            System.exit(1);
        }

        JSONObject simpleSubscribeMessage = new JSONObject();
        simpleSubscribeMessage.put("type", "subscribe");

        JSONArray productIds = new JSONArray();
        productIds.add(instrument);
        simpleSubscribeMessage.put("product_ids", productIds);

        JSONArray channels = new JSONArray();
        channels.add("level2");
        channels.add("heartbeat");
        JSONObject channelValue = new JSONObject();
        channelValue.put("name", "ticker");
        channelValue.put("product_ids", productIds);
        channels.add(channelValue);
        simpleSubscribeMessage.put("channels", channels);

        return simpleSubscribeMessage.toJSONString();
    }

    public static OrderBook buildOrderBookFromSnapshot(String snapshotResponse) {
        // Sanity check - fail-fast
        if (snapshotResponse == null || snapshotResponse.trim().isEmpty()) {
            return null;
        }

        Map<Float, Float> asks = null;
        Map<Float, Float> bids = null;
        try {
            Object obj = OrderBookUtil.parser.parse(snapshotResponse);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray asksList = (JSONArray) jsonObject.get(OrderBookUtil.ASKS);
            JSONArray bidsList = (JSONArray) jsonObject.get(OrderBookUtil.BIDS);
            asks = OrderBookUtil.getAllPrices(asksList);
            bids = OrderBookUtil.getAllPrices(bidsList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new OrderBook(asks, bids);
    }


    public static OrderBookNotification buildOrderBookNotificationFromL2Update(String l2UpdateResponse) {
        // Sanity check - fail-fast
        if (l2UpdateResponse == null || l2UpdateResponse.trim().isEmpty()) {
            return null;
        }

        OrderBookNotification buySellUpdates = null;
        try {
            Object obj = OrderBookUtil.parser.parse(l2UpdateResponse);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray changesList = (JSONArray) jsonObject.get(OrderBookUtil.CHANGES);
            buySellUpdates = OrderBookUtil.orderBookNotificationFromCollection(changesList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return buySellUpdates;
    }

    private static Map<Float, Float> getAllPrices(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }

        Map<Float, Float> prices = new HashMap<>();
        for (JSONArray priceAndSize : (Iterable<JSONArray>) jsonArray) {
            Float price = Float.parseFloat((String) (priceAndSize).get(0));
            Float size = Float.parseFloat((String) (priceAndSize).get(1));
            prices.put(price, size);
        }

        return prices;
    }


    private static OrderBookNotification orderBookNotificationFromCollection(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }

        List<Order> buyUpdates = new ArrayList<>();
        List<Order> sellUpdates = new ArrayList<>();
        for (JSONArray sidePriceSize : (Iterable<JSONArray>) jsonArray) {
            String side = (String) sidePriceSize.get(0);
            float price = Float.parseFloat((String) sidePriceSize.get(1));
            float size = Float.parseFloat((String) sidePriceSize.get(2));
            Order orderBookElement = new Order(price, size);
            if (OrderBookUtil.BUY_UPDATE.equals(side))
                buyUpdates.add(orderBookElement);
            else if (OrderBookUtil.SELL_UPDATE.equals(side))
                sellUpdates.add(orderBookElement);
            else
                log.error("Unsupported side value: {}", side);
        }
        return new OrderBookNotification(buyUpdates, sellUpdates);
    }
}
