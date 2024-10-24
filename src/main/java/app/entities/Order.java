package app.entities;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<ProductLine> productList;
    private Double orderPrice;
    private boolean isPaid;

    public Order(int orderId, int userId, Double orderPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderPrice = orderPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }
}