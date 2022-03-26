package test;

import com.matchingengine.MatchingEngine;
import com.matchingengine.Order;
import com.matchingengine.Side;
import com.matchingengine.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

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
        assertEquals(matchingEngine.getBestBid(), bestBid);
    }

    @Test
    void getBestBidNull() {
        assertNull(matchingEngine.getBestBid());
    }

    @Test
    void getBestAsk() {
        double bestAsk = 35900.50;
        matchingEngine.add(new Order(99, Side.SELL, bestAsk, 0.5));
        assertEquals(matchingEngine.getBestAsk(), bestAsk);
    }

    @Test
    void getBestAskNull() {
        assertNull(matchingEngine.getBestAsk());
    }

    @Test
    void getName() {
        assertEquals(matchingEngine.getName(), "BTC/USDT");
    }

    @Test
    void addBuyDifferentLevelsFill() {
        setup();
        Order order = new Order(99, Side.BUY, 36100, 3);
        double initialOrderSize = 3;
        List<Trade> trades = matchingEngine.add(order);

        assertEquals(trades.size(), 4);

        Trade trade0 = trades.get(0);
        assertEquals(trade0.getSize(), 0.6);
        assertEquals(trade0.getMakerOrderId(), 7);
        assertEquals(trade0.getTakerOrderId(), order.getOrderId());
        assertEquals(trade0.getPrice(), 36000);

        Trade trade1 = trades.get(1);
        assertEquals(trade1.getSize(), 0.6);
        assertEquals(trade1.getMakerOrderId(), 11);
        assertEquals(trade1.getTakerOrderId(), order.getOrderId());
        assertEquals(trade1.getPrice(), 36010.56);

        Trade trade2 = trades.get(2);
        assertEquals(trade2.getSize(), 0.10);
        assertEquals(trade2.getMakerOrderId(), 12);
        assertEquals(trade2.getTakerOrderId(), order.getOrderId());
        assertEquals(trade2.getPrice(), 36015.58);

        Trade trade3 = trades.get(3);
        assertEquals(trade3.getSize(), 0.3);
        assertEquals(trade3.getMakerOrderId(), 10);
        assertEquals(trade3.getTakerOrderId(), order.getOrderId());
        assertEquals(trade3.getPrice(), 36050.56);

        assertEquals(order.getSize(),
                (initialOrderSize - trade3.getSize() - trade2.getSize() - trade1.getSize() - trade0.getSize()),
                0.000000000001);

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
        assertEquals(matchingEngine.getBuyOrders().size(), initialBuyOrders - 1);
        assertEquals(matchingEngine.getSellOrders().size(), initialSellOrders - 1);
    }

    @Test
    void getMarketDepth() {
    }

    @Test
    void getMarketVolume() {
    }
}