package com.matchingengine;

import java.util.ArrayList;
import java.util.List;

public class Trader {
    private final long traderId;
    private final List<Order> history;
    private final List<Order> pendingOrders;

    public Trader(long traderId) {
        this.traderId = traderId;
        this.history = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
    }

    public long getTraderId() {
        return traderId;
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public List<Order> getHistory() {
        return history;
    }
}
