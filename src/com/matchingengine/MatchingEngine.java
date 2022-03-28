package com.matchingengine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

public class MatchingEngine implements IMatchingEngine {
    private final String name;
    private final Map<Long, Order> orders;
    private final SortedMap<Double, Queue<Order>> buyTree;
    private final SortedMap<Double, Queue<Order>> sellTree;
    private int ordersExecuted;

    public MatchingEngine(String name) {
        this.name = name;
        this.orders = new HashMap<>();
        this.buyTree = new TreeMap<>(new BidLimitComparer());
        this.sellTree = new TreeMap<>(new AskLimitComparer());
        this.ordersExecuted = 0;
    }

    @Override
    public Double getBestBid() {
        if (buyTree.isEmpty()) return null;
        return buyTree.firstKey();
    }

    @Override
    public Double getBestAsk() {
        if (sellTree.isEmpty()) return null;
        return sellTree.firstKey();
    }

    public String getName() {
        return name;
    }

    public Order getOrder(long orderId) {
        return orders.get(orderId);
    }

    public List<Trade> add(Order order) {
        if (order.getSide() == Side.BUY) {
            _add(this.buyTree, order);
        } else {
            _add(this.sellTree, order);
        }

        return match(order);
    }

    private void _add(SortedMap<Double, Queue<Order>> treeMap, Order order) {
        if (treeMap.containsKey(order.getPrice())) {
            Queue<Order> limitAskQueue = treeMap.get(order.getPrice());
            limitAskQueue.add(order);
        } else {
            Queue<Order> limitAskQueue = new ArrayDeque<>();
            limitAskQueue.add(order);
            treeMap.put(order.getPrice(), limitAskQueue);
        }
        this.orders.put(order.getOrderId(), order);
    }

    private List<Trade> match(Order orderToMatch) {
        ArrayList<Trade> trades = new ArrayList<>();
        if (sellTree.isEmpty() || buyTree.isEmpty()) return trades;
        Double bestBid = getBestBid();
        Double bestAsk = getBestAsk();
        // Order is suitable for matching
        while ((bestBid != null && bestAsk != null) && (bestBid >= bestAsk) && !(buyTree.isEmpty() || sellTree.isEmpty())) {
            Queue<Order> buyQueue = buyTree.get(bestBid);
            Queue<Order> sellQueue = sellTree.get(bestAsk);
            Order peekBuy = buyQueue.peek();
            Order peekSell = sellQueue.peek();
            if (peekBuy != null && peekSell != null) {
                long makerOrderId;
                double executedSize;
                double executedPrice;
                if (orderToMatch.getSide() == Side.BUY) {
                    makerOrderId = peekSell.getOrderId();
                } else {
                    makerOrderId = peekBuy.getOrderId();
                }

                if (peekBuy.getSize() > peekSell.getSize()) {
                    peekBuy.setSize(peekBuy.getSize() - peekSell.getSize());
                    executedSize = peekSell.getSize();
                    sellQueue.poll();
                    _removeLimitIfEmpty(sellTree, sellQueue.isEmpty(), peekSell.getPrice());
                } else if (peekSell.getSize() > peekBuy.getSize()) {
                    peekSell.setSize(peekSell.getSize() - peekBuy.getSize());
                    executedSize = peekBuy.getSize();
                    buyQueue.poll();
                    _removeLimitIfEmpty(buyTree, buyQueue.isEmpty(), peekBuy.getPrice());
                } else {
                    executedSize = peekSell.getSize();
                    buyQueue.poll();
                    sellQueue.poll();
                    _removeLimitIfEmpty(buyTree, buyQueue.isEmpty(), peekBuy.getPrice());
                    _removeLimitIfEmpty(sellTree, sellQueue.isEmpty(), peekSell.getPrice());
                }
                if (orderToMatch.getSide() == Side.BUY) {
                    // We're matching with a sell order, so the price is the sell order's price
                    executedPrice = peekSell.getPrice();
                } else {
                    executedPrice = peekBuy.getPrice();
                }
                trades.add(new Trade(makerOrderId, orderToMatch.getOrderId(), executedPrice, executedSize));
                ordersExecuted++;
            }
            bestBid = getBestBid();
            bestAsk = getBestAsk();
        }
        return trades;
    }

    private void _removeLimitIfEmpty(SortedMap<Double, Queue<Order>> treeMap, boolean isEmpty, double limitPrice) {
        if (isEmpty) {
            treeMap.remove(limitPrice);
        }
    }

    public int getOrdersExecuted() {
        return ordersExecuted;
    }

    @Override
    public void remove(long orderId) {
        Order order = orders.get(orderId);
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
            orders.remove(order.getOrderId());
        }
    }

    private void _removeLimit(SortedMap<Double, Queue<Order>> treeMap, double price) {
        treeMap.remove(price);
    }

    public SortedMap<Double, Queue<Order>> getBuyOrders() {
        return buyTree;
    }

    public SortedMap<Double, Queue<Order>> getSellOrders() {
        return sellTree;
    }

    public Map<Long, Order> getOrders() {
        return orders;
    }

    public double getMarketDepthAtLimit(Side side, double limitPrice) {
        double totalSize = 0;
        if (side == Side.BUY) {
            Queue<Order> orders = this.buyTree.get(limitPrice);
            for (Order order: orders) {
                totalSize += order.getSize();
            }
        } else {
            Queue<Order> orders = this.sellTree.get(limitPrice);
            for (Order order: orders) {
                totalSize += order.getSize();
            }
        }
        return totalSize;
    }

    public List<Map<Double, Double>> getMarketDepth() {
        Map<Double, Double> buyMarketDepth = new HashMap<>();
        Map<Double, Double> sellMarketDepth = new HashMap<>();

        for (Double limitPrice: buyTree.keySet()) {
            buyMarketDepth.put(limitPrice, getMarketDepthAtLimit(Side.BUY, limitPrice));
        }

        for (Double limitPrice: sellTree.keySet()) {
            sellMarketDepth.put(limitPrice, getMarketDepthAtLimit(Side.SELL, limitPrice));
        }

        return new ArrayList<>(Arrays.asList(buyMarketDepth, sellMarketDepth));
    }
}
