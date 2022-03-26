package com.matchingengine;


public interface IReadOnlyMatchingEngine {
    Double getBestBid();
    Double getBestAsk();

    String getName();
}
