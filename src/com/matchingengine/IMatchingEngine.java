package com.matchingengine;

public interface IMatchingEngine extends IReadOnlyMatchingEngine {
    public void add(Order order);
    public void remove(long orderId);
}
