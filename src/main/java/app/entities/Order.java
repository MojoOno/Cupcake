package app.entities;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<ProductLine> productList;
    private float orderPrice;
    private boolean isPaid;

    public Order(int orderId, int userId, float orderPrice) {
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

    public float getOrderPrice() {
        return orderPrice;
    }
}