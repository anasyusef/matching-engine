package com.matchingengine;

import java.util.List;

public interface IMatchingEngine extends IReadOnlyMatchingEngine {
    List<Trade> add(Order order);

    void remove(long orderId);
}
