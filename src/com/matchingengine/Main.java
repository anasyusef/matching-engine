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
        String[][] orders = new String[][]{
                {"150", "5", "buy"},
                {"190", "1", "sell"},
                {"200", "1", "sell"},
                {"100", "9", "buy"},
                {"140", "8", "sell"},
                {"210", "4", "buy"},
                {"210", "5", "sell"},
                {"120", "4", "buy"},
                {"120.5", "4.1", "buy"},
                {"120.5", "5.2", "buy"},
                {"120.5", "6.2", "buy"},
                {"120.6", "6.2", "sell"},
                {"120.4", "6.2", "sell"},
                {"120.7", "6.2", "sell"},
        };

        for(int i = 0; i < orders.length; i++) {
            System.out.println(matchingEngine.add(new Order(i,
                    orders[i][2].equals("buy") ? Side.BUY : Side.SELL, Double.parseDouble(orders[i][0]),
                    Double.parseDouble(orders[i][1]))));
            System.out.println(matchingEngine.getMarketDepth());
        }
    }
}
