package app.controllers;

import app.persistence.OrderMapper;
import app.entities.ProductLine;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.*;

import io.javalin.http.Context;

import java.util.List;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/basketpage", ctx -> getUserBasket(ctx, connectionPool));
    }

    public static void getUserBasket(Context ctx, ConnectionPool connectionPool) {
        int userId = ctx.sessionAttribute("currentUser");
        try {
            List<ProductLine> basket = OrderMapper.getUserBasket(userId, connectionPool);
            double totalPrice = basket.stream()
                                        .mapToDouble(pl -> pl.getCupcake().getPrice() * pl.getQuantity())
                                        .sum();
            ctx.attribute("basket", basket);
            ctx.attribute("totalPrice", totalPrice);
            ctx.render("basketPage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            ctx.render("basketPage.html");
        }
    }

    public static void createOrder (){
    }

    public static void deleteOrder(){
    }

    public static void updateOrder(){
    }

    public static void getOrdersByUser() {
    }

    public static void getAllOrders() {
    }

    public static void getOrderById() {
    }
}

