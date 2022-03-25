package com.matchingengine;

public interface IMatchingEngine extends IReadOnlyMatchingEngine {
    void add(Order order);
    void remove(Order order);
}
