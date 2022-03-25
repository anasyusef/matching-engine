package com.matchingengine;

/**
 * Matching Engine - IMC Final Round
 * MatchingEngine -> (Trade)
 * Trade -> A trade made by a user, states if the order is buy or sell
 * */

public class Main {

    public static void main(String[] args) {
//        List<Order> orderList = new ArrayList<>();
//        Trader trader = new Trader(1);
        MatchingEngine matchingEngine = new MatchingEngine("BTC/USD");
        matchingEngine.add(new Order(1, Side.BUY, 35000));
        matchingEngine.add(new Order(2, Side.BUY, 38000));
        matchingEngine.add(new Order(3, Side.SELL, 34000));
        matchingEngine.add(new Order(4, Side.SELL, 32000));
        System.out.println(matchingEngine.getBuyTree());
        System.out.println(matchingEngine.getSellTree());
        System.out.println("Best bid: " + matchingEngine.getBestBid() + " Best ask: " + matchingEngine.getBestAsk());

//        matchingEngine.remove(trader, 4);

//        System.out.println(matchingEngine.getMarketDepth());
//        System.out.println(matchingEngine.getMarketVolume());
    }
}
