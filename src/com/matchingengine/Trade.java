package com.matchingengine;

public class Trade {
    private final long makerOrderId;
    private final long takerOrderId;
    private final double price;

    private final double size;


    public Trade(long makerOrderId, long takerOrderId, double price, double size) {
        this.makerOrderId = makerOrderId;
        this.takerOrderId = takerOrderId;
        this.price = price;
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public long getMakerOrderId() {
        return makerOrderId;
    }

    public long getTakerOrderId() {
        return takerOrderId;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "makerOrderId=" + makerOrderId +
                ", takerOrderId=" + takerOrderId +
                ", price=" + price +
                ", size=" + size +
                '}';
    }
}
