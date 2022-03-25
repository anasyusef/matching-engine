package com.matchingengine;

import java.util.*;

public class MatchingEngine implements IMatchingEngine {
    private final String name;
    private final SortedMap<Double, Queue<Order>> buyTree;
    private final SortedMap<Double, Queue<Order>> sellTree;

    public MatchingEngine(String name) {
        this.name = name;
        this.buyTree = new TreeMap<>(new BidLimitComparer());
        this.sellTree = new TreeMap<>(new AskLimitComparer());
    }

    @Override
    public double getBestBid() {
        return buyTree.firstKey();
    }

    @Override
    public double getBestAsk() {
        return sellTree.firstKey();
    }

    public String getName() {
        return name;
    }

    public void add(Order order) {
        if (order.getSide() == Side.BUY) {
            if (this.buyTree.containsKey(order.getPrice())) {
                Queue<Order> limitBidQueue = this.buyTree.get(order.getPrice());
                limitBidQueue.add(order);
            } else {
                Queue<Order> limitBidQueue = new ArrayDeque<>();
                limitBidQueue.add(order);
                this.buyTree.put(order.getPrice(), limitBidQueue);
            }
        } else {
            if (this.sellTree.containsKey(order.getPrice())) {
                Queue<Order> limitAskQueue = this.sellTree.get(order.getPrice());
                limitAskQueue.add(order);
            } else {
                Queue<Order> limitAskQueue = new ArrayDeque<>();
                limitAskQueue.add(order);
                this.sellTree.put(order.getPrice(), limitAskQueue);
            }
        }
    }

    @Override
    public void remove(Order order) {
        if (order.getSide() == Side.BUY) {
            _remove(buyTree, order);
        } else {
            _remove(sellTree, order);
        }
    }

    private void _remove(SortedMap<Double, Queue<Order>> treeMap, Order order) {
        if (treeMap.containsKey(order.getPrice())) {
            Queue<Order> q = treeMap.get(order.getPrice());
            boolean isRemoved = q.remove(order);
            if (isRemoved && q.isEmpty()) {
                _removeLimit(treeMap, order.getPrice());
            }
        }
    }

    private void _removeLimit(SortedMap<Double, Queue<Order>> treeMap, double price) {
        treeMap.remove(price);
    }

    public SortedMap<Double, Queue<Order>> getBuyTree() {
        return buyTree;
    }

    public SortedMap<Double, Queue<Order>> getSellTree() {
        return sellTree;
    }

    public long getMarketDepth() {
        // TODO - Implement
        return 0;
    }

    public double getMarketVolume() {
        // TODO - Implement
        return 0;
    }
}
