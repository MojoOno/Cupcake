package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.persistence.OrderMapper;
import app.entities.ProductLine;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.*;

import io.javalin.http.Context;

import java.util.List;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/basketpage", ctx -> ctx.render("basketPage.html"));
        app.post("/basketpage", ctx -> getUserBasket(ctx, connectionPool));
        app.post("/orders", ctx -> setOrderStatus(ctx, connectionPool));
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

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        if (UserController.isUserAdmin(ctx, connectionPool)) {
            try {
                List<Order> orderList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("orders", orderList);
                ctx.render("allorderspage.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Something went wrong, Try again");
                ctx.render("index.html");
            }
        }else {
            ctx.attribute("message", "You are not authorized to view this page");
            ctx.render("index.html");
        }



    }

    public static void getOrderById() {
    }

    public static void setOrderStatus(Context ctx, ConnectionPool connectionPool) {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        String status = ctx.formParam("status");
        try {
            OrderMapper.setOrderStatus(orderId, connectionPool);
            ctx.attribute("message", "Order status updated");
            ctx.render("allorderspage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            ctx.render("allorderspage.html");
        }
    }
}

