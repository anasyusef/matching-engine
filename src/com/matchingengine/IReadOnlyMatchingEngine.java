package com.matchingengine;

public interface IReadOnlyMatchingEngine {
    public double getBestBid();
    public double getBestAsk();

    public String getName();
}
