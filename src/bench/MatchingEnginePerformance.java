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

    public static void orderBookReportTest(int orders) {
        // Tests matching engine reporting more market depth. Benchmarks market depth
    }
}
