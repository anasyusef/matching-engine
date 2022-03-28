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

    public void benchmarkAddOrdersDenseOrderBook(int num_orders) {
        // Tests matching engine with close prices and more depth on each level;
        double minPriceRange = 0.01;
        double maxPriceRange = 2;
        Order[] orders = generateOrders(num_orders, minPriceRange, maxPriceRange);
        long startTime = System.currentTimeMillis();
        MatchingEngine me = new MatchingEngine("LINK/USDT");
        for (Order order: orders) {
            me.add(order);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Adding " + num_orders + " orders using a dense order-book - Execution time: "
                + (endTime - startTime) + " milliseconds");
    }

    public static void sparseOrderBookTest(int orders) {
        // Tests matching engine with less depth on each level and more prices scattered around

    }

    public static void orderBookReportTest(int orders) {
        // Tests matching engine reporting more market depth. Benchmarks market depth
    }
}
