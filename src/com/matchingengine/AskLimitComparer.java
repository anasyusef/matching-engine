package com.matchingengine;

import java.util.Comparator;

public class AskLimitComparer implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return java.lang.Double.compare(o2, o1);
    }
}
