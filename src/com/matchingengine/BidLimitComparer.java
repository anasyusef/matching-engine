package com.matchingengine;

import java.util.Comparator;

public class BidLimitComparer implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}
