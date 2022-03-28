package test;

import com.matchingengine.MatchingEngine;
import com.matchingengine.Order;
import com.matchingengine.Side;
import com.matchingengine.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    private final static double DELTA = 0.000000000001;

    private final MatchingEngine matchingEngine = new MatchingEngine("BTC/USDT");

    void setup() {
        matchingEngine.add(new Order(1, Side.BUY, 35000, 1));
        matchingEngine.add(new Order(2, Side.BUY, 35000.54, 0.2));
        matchingEngine.add(new Order(3, Side.BUY, 35000.53, 0.1));
        matchingEngine.add(new Order(4, Side.BUY, 35000.56, 0.4));
        matchingEngine.add(new Order(5, Side.BUY, 35100.56, 0.5));
        matchingEngine.add(new Order(6, Side.BUY, 34900.58, 0.05));

        matchingEngine.add(new Order(7, Side.SELL, 36000, 0.6));
        matchingEngine.add(new Order(8, Side.SELL, 36100.54, 0.4));
        matchingEngine.add(new Order(9, Side.SELL, 36150.53, 0.7));
        matchingEngine.add(new Order(10, Side.SELL, 36050.56, 0.3));
        matchingEngine.add(new Order(11, Side.SELL, 36010.56, 0.6));
        matchingEngine.add(new Order(12, Side.SELL, 36015.58, 0.10));
    }

    @Test
    void getBestBid() {
        double bestBid = 35500.56;
        matchingEngine.add(new Order(99, Side.BUY, bestBid, 0.5));
        assertEquals(bestBid, matchingEngine.getBestBid());
    }

    @Test
    void getBestBidNull() {
        assertNull(matchingEngine.getBestBid());
    }

    @Test
    void getBestAsk() {
        double bestAsk = 35900.50;
        matchingEngine.add(new Order(99, Side.SELL, bestAsk, 0.5));
        assertEquals(bestAsk, matchingEngine.getBestAsk());
    }

    @Test
    void getBestAskNull() {
        assertNull(matchingEngine.getBestAsk());
    }

    @Test
    void getName() {
        assertEquals("BTC/USDT", matchingEngine.getName());
    }

    @Test
    void addBuyDifferentLevelsFill() {
        setup();
        Order order = new Order(99, Side.BUY, 36100, 3);
        double initialOrderSize = order.getSize();
        List<Trade> trades = matchingEngine.add(order);

        assertEquals(4, trades.size());

        Trade trade0 = trades.get(0);
        assertEquals(0.6, trade0.getSize());
        assertEquals(7, trade0.getMakerOrderId());
        assertEquals(order.getOrderId(), trade0.getTakerOrderId());
        assertEquals(36000, trade0.getPrice());

        Trade trade1 = trades.get(1);
        assertEquals(0.6, trade1.getSize());
        assertEquals(11, trade1.getMakerOrderId());
        assertEquals(order.getOrderId(), trade1.getTakerOrderId());
        assertEquals(36010.56, trade1.getPrice());

        Trade trade2 = trades.get(2);
        assertEquals(0.10, trade2.getSize());
        assertEquals(12, trade2.getMakerOrderId());
        assertEquals(order.getOrderId(), trade2.getTakerOrderId());
        assertEquals(36015.58, trade2.getPrice());

        Trade trade3 = trades.get(3);
        assertEquals(0.3, trade3.getSize());
        assertEquals(10, trade3.getMakerOrderId());
        assertEquals(order.getOrderId(), trade3.getTakerOrderId());
        assertEquals(36050.56, trade3.getPrice());

        assertEquals(initialOrderSize - trade3.getSize() - trade2.getSize() - trade1.getSize() -
                        trade0.getSize(),
                order.getSize(),
                DELTA);

    }

    @Test
    void addSellDifferentLevelsFill() {
        setup();
        Order order = new Order(99, Side.SELL, 35000.56, 0.6);
        List<Trade> trades = matchingEngine.add(order);
        assertEquals(trades.size(),  2);

        Trade trade0 = trades.get(0);
        assertEquals(0.5, trade0.getSize());
        assertEquals(5, trade0.getMakerOrderId());
        assertEquals(order.getOrderId(), trade0.getTakerOrderId());
        assertEquals(35100.56, trade0.getPrice());

        Trade trade1 = trades.get(1);
        assertEquals(0.1,  trade1.getSize(), DELTA);
        assertEquals(4, trade1.getMakerOrderId());
        assertEquals(order.getOrderId(), trade1.getTakerOrderId());
        assertEquals(35000.56, trade1.getPrice());


        assertEquals(0.3, matchingEngine.getOrder(4).getSize(), DELTA);
    }

    @Test
    void remove() {
        setup();
        int initialOrdersSize = matchingEngine.getOrders().size();
        int initialBuyOrders = matchingEngine.getBuyOrders().size();
        int initialSellOrders = matchingEngine.getSellOrders().size();
        matchingEngine.remove(4);
        matchingEngine.remove(7);
        assertEquals(initialOrdersSize - 2, matchingEngine.getOrders().size());
        assertEquals(initialBuyOrders - 1, matchingEngine.getBuyOrders().size());
        assertEquals(initialSellOrders - 1, matchingEngine.getSellOrders().size());
    }

    @Test
    void getMarketDepth() {
    }
}