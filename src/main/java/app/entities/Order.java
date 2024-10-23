package app.entities;

import java.util.List;

public class Order {
    int orderId;
    User user;
    List<ProductLine> productList;
    float orderPrice;
    boolean isPaid;

}
