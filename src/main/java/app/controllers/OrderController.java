package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.entities.Bottom;
import app.entities.Cupcake;
import app.entities.Topping;
import app.persistence.OrderMapper;
import app.entities.ProductLine;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.*;

import io.javalin.http.Context;

import java.util.List;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> showIndexPage(ctx, connectionPool));
        app.post("/addtobasket", ctx -> addToBasket(ctx, connectionPool));
        app.get("/basketpage", ctx -> showBasketPage(ctx, connectionPool));
        app.post("/basketpage", ctx -> getUserBasket(ctx, connectionPool));
        app.get("/orders", ctx -> showOrdersPage(ctx, connectionPool));
        app.post("/deleteorder", ctx -> deleteOrder(ctx, connectionPool));
        app.post("/payorder", ctx -> payOrder(ctx, connectionPool));
    }

    public static void showIndexPage(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Bottom> bottomsList = OrderMapper.getAllBottoms(connectionPool);
            List<Topping> toppingsList = OrderMapper.getAllToppings(connectionPool);
            ctx.attribute("bottomsList", bottomsList);
            ctx.attribute("toppingsList", toppingsList);
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
            ctx.render("index.html");
        }
    }

    public static void addToBasket(Context ctx, ConnectionPool connectionPool) {

        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");


        try {
            if (currentOrder == null) {
                currentOrder = OrderMapper.createOrder(currentUser, connectionPool);
                ctx.sessionAttribute("currentOrder", currentOrder);
            }
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int toppingId = Integer.parseInt(ctx.formParam("topping"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            Bottom bottom = OrderMapper.getBottomById(bottomId, connectionPool);
            Topping topping = OrderMapper.getToppingById(toppingId, connectionPool);
            Cupcake cupcake = new Cupcake(bottom, topping);
            ProductLine productLine = new ProductLine(cupcake, quantity);
            productLine = OrderMapper.addToBasket(currentUser, currentOrder, productLine, connectionPool);
            ctx.attribute("message", "Cupcake added to basket");
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again" + e.getMessage());
            ctx.render("index.html");
        } catch (NumberFormatException e) {
            ctx.attribute("message", "Please select a bottom, topping and quantity " + ctx.formParam("bottom") + " " + ctx.formParam("topping") + " " + ctx.formParam("quantity") + " " + e.getMessage());

            OrderController.showIndexPage(ctx, connectionPool);
        }
    }

    public static void showBasketPage(Context ctx, ConnectionPool connectionPool) {
        Order currentOrder = ctx.sessionAttribute("currentOrder");
        if (currentOrder == null) {
            ctx.attribute("message", "Basket is empty");
            OrderController.showIndexPage(ctx, connectionPool);
            return;
        }
        try {
            List<ProductLine> productLinesList = OrderMapper.getUserBasket(currentOrder, connectionPool);
            currentOrder.setProductLineList(productLinesList);
            float totalPrice = 0;
            for (ProductLine productLine : productLinesList) {
                totalPrice += productLine.getTotalPrice();
            }
            ctx.attribute("orderPrice", totalPrice);
            ctx.attribute("productLinesList", productLinesList);
            ctx.render("basketpage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
            ctx.render("basketpage.html");
        }
    }

    public static void getUserBasket(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");

        if (currentOrder == null || currentUser == null) {
            ctx.attribute("message", "Basket is empty");
            ctx.render("basketpage.html");
        }

        try {

            List<ProductLine> productLinesList = OrderMapper.getUserBasket(currentOrder, connectionPool);
            if (productLinesList.isEmpty()) {
                ctx.attribute("message", "Basket is empty");
            } else {
                ctx.attribute("message", "Basket is not empty");

                float totalPrice = currentOrder.getOrderPrice();
                ctx.attribute("productLinesList", productLinesList);
                ctx.attribute("orderPrice", totalPrice);
            }
            ctx.render("basketpage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            ctx.render("basketpage.html");
        }
    }

    public static void showOrdersPage(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.attribute("message", "Please login to view orders");
            ctx.render("index.html");
        } else if (currentUser.isAdmin()) {
            getAllOrders(ctx, connectionPool);
        } else {
            getOrdersByUser(ctx, connectionPool);
        }

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

    private static void payOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");

        if (currentOrder == null || currentUser == null) {
            ctx.attribute("message", "Basket is empty");
            showIndexPage(ctx, connectionPool);
            return;
        }
        try {
            if (currentUser.pay(currentOrder.getOrderPrice())) {
                UserMapper.updateBalance(currentUser, connectionPool);
                OrderMapper.setOrderStatus(currentOrder.getOrderId(), connectionPool);
                ctx.attribute("message", "Order paid");
                ctx.sessionAttribute("currentOrder", null);
            } else {
                ctx.attribute("message", "Insufficient funds");
            }
            showIndexPage(ctx, connectionPool);
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            showIndexPage(ctx, connectionPool);
        }
    }

    public static void getOrdersByUser(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        try {
            List<Order> ordersList = OrderMapper.getOrdersByUserId(currentUser, connectionPool);
            ctx.attribute("ordersList", ordersList);
            ctx.attribute("currentUser", currentUser);
            ctx.render("orders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
            ctx.render("index.html");
        }
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        try {
            List<Order> ordersList = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("ordersList", ordersList);
            ctx.attribute("currentUser", currentUser);
            ctx.render("orders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
            ctx.render("index.html");
        }
    }


}