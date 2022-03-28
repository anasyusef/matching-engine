package bench;

import com.matchingengine.MatchingEngine;
import com.matchingengine.Order;
import com.matchingengine.Side;

import java.util.Random;

public class MatchingEnginePerformance {

    private static <T> T getRandom(T[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private Order[] generateOrders(int num_orders, double minPriceRange, double maxPriceRange) {
        Side[] sides = Side.values();
        Order[] orders = new Order[num_orders];
        for (int i = 0; i < num_orders; i++) {
            double priceToTwoDp = minPriceRange +
                    (double) Math.round((maxPriceRange - minPriceRange) * Math.random() * 100) / 100;
            double randomSize = (double) Math.round(Math.random() * 100) / 100;
            orders[i] = new Order(i, getRandom(sides), priceToTwoDp, randomSize);
        }
        return orders;
    }

    private Order[] generateOrders(int startRange, int endRange, double minPriceRange, double maxPriceRange, Side side) {
        Order[] orders = new Order[endRange - startRange];
        for (int i = 0; i < (endRange - startRange); i++) {
            double priceToTwoDp = minPriceRange +
                    (double) Math.round((maxPriceRange - minPriceRange) * Math.random() * 100) / 100;
            double randomSize = (double) Math.round(Math.random() * 100) / 100;
            orders[i] = new Order(startRange + i, side, priceToTwoDp, randomSize);
        }
        return orders;
    }

    /**
     * Benchmarks adding orders to a dense (prices are much closer to each other and there is more depth at each level)
     * order-book.
     * @param num_orders Number of orders to generate
     */
    public void benchmarkAddOrdersDenseOrderBook(int num_orders) {
        double minPriceRange = 0.01;
        double maxPriceRange = 1;
        long executionTime = benchmarkAddOrders(num_orders, minPriceRange, maxPriceRange);
        System.out.println("Adding " + num_orders + " orders using a dense order-book - Execution time: "
                + executionTime + " milliseconds");
    }

    /**
     * Benchmarks adding orders to a sparse
     * (prices are further apart from each other and there is less depth at each level) order-book
     * @param num_orders Number of orders to generate
     */
    public void benchmarkAddOrdersSparseOrderBook(int num_orders) {
        double minPriceRange = 1;
        double maxPriceRange = 100;
        long executionTime = benchmarkAddOrders(num_orders, minPriceRange, maxPriceRange);
        System.out.println("Adding " + num_orders + " orders using a sparse order-book - Execution time: "
                + executionTime + " milliseconds");
    }

    private long benchmarkAddOrders(int num_orders, double minPriceRange, double maxPriceRange) {
        Order[] orders = generateOrders(num_orders, minPriceRange, maxPriceRange);
        long startTime = System.currentTimeMillis();
        MatchingEngine me = new MatchingEngine("LINK/USDT");
        for (Order order: orders) {
            me.add(order);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public void benchmarkOrderBookMarketDepth(int num_orders) {
        double minPriceRange = 0.01;
        double maxPriceRange = 100;
        long startTime = System.currentTimeMillis();
        MatchingEngine me = new MatchingEngine("LINK/USDT");
        Order[] orders = generateOrders(num_orders, minPriceRange, maxPriceRange);
        for (Order order: orders) {
            me.add(order);
            me.getMarketDepth();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Getting market depth on " + num_orders + " orders - Execution time: "
                + (endTime - startTime)  + " milliseconds");
    }

    public void benchmakrOrderBookRemove(int num_orders) {
        double minPriceRangeBuy = 1;
        double maxPriceRangeBuy = 100;

        double minPriceRangeSell = 101;
        double maxPriceRangeSell = 200;

        MatchingEngine me = new MatchingEngine("LINK/USDT");
        Order[] buyOrders = generateOrders(0, num_orders / 2, minPriceRangeBuy, maxPriceRangeBuy, Side.BUY);
        Order[] sellOrders = generateOrders((num_orders / 2), num_orders, minPriceRangeSell, maxPriceRangeSell, Side.SELL);
        for (Order order: buyOrders) {
            me.add(order);
        }

        for (Order order: sellOrders) {
            me.add(order);
        }

        System.out.println("Buy levels: " + me.getMarketDepth().get(0).size() +
                " - Sell levels: " + me.getMarketDepth().get(1).size());

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < num_orders; i++) {
            me.remove(i);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Removing " + num_orders + " orders - Execution time: "
                + (endTime - startTime)  + " milliseconds");
    }
}
