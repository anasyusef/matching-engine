package com.matchingengine;

public class Order {

    private final long orderId;
    private final Side side;
    private final double price;
    private double size;

    public Order(long orderId, Side side, double price, double size) {
        this.orderId = orderId;
        this.side = side;
        this.price = price;
        this.size = size;
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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", side=" + side +
                ", price=" + price +
                ", size=" + size +
                '}';
    }
}
