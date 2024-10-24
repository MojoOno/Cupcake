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
        app.get("/orders", ctx -> ctx.render("orders.html"));
        //app.post("/orders", ctx -> setOrderStatus(ctx, connectionPool));
        app.post("/deleteorder", ctx -> deleteOrder(ctx, connectionPool));
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

    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        try {
            OrderMapper.deleteOrder(orderId, connectionPool);
            ctx.attribute("message", "Order deleted successfully");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Error deleting order: " + e.getMessage());
        }
        getAllOrders(ctx, connectionPool); // Refresh the orders page
    }

    public static void updateOrder(Context ctx, ConnectionPool connectionPool) {
    }

    public static void getOrdersByUser() {
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        boolean isAdmin = UserController.isUserAdmin(ctx, connectionPool);
        try {
            List<Order> orders;
            if (isAdmin) {
                orders = OrderMapper.getAllOrders(connectionPool);
            } else {
                orders = OrderMapper.getOrdersByUserId(currentUser.getUserId(), connectionPool);
            }
            ctx.attribute("orders", orders);
            ctx.attribute("currentUser", currentUser);
            ctx.render("orders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
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
            ctx.render("orders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            ctx.render("index.html");
        }
    }
}

