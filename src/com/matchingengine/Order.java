package com.matchingengine;

public class Order {

    private final long orderId;
    private final Side side;
    private final double price;

    public Order(long orderId, Side side, double price) {
        this.orderId = orderId;
        this.side = side;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Side getSide() {
        return side;
    }

    public long getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", side=" + side +
                ", price=" + price +
                '}';
    }
}
