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
import io.javalin.*;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Map;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> showIndexPage(ctx, connectionPool));
        app.post("/addtobasket", ctx -> addToBasket(ctx, connectionPool));
        app.get("/basketpage", ctx -> showBasketPage(ctx, connectionPool));
        app.post("/basketpage", ctx -> getUserBasket(ctx, connectionPool));
        app.get("/orders", ctx -> ctx.render("orders.html"));
        app.post("/deleteorder", ctx -> deleteOrder(ctx, connectionPool));
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

    public static void showBasketPage(Context ctx, ConnectionPool connectionPool) {
        Order currentOrder = ctx.sessionAttribute("currentOrder");
        try {
            List<ProductLine> productLinesList = OrderMapper.getUserBasket(currentOrder, connectionPool);
            ctx.attribute("productLinesList", productLinesList);
            ctx.render("basketpage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, try again");
            ctx.render("basketpage.html");
        }
    }

    public static void createOrder (Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        try {
            Order order = OrderMapper.createOrder(currentUser, connectionPool);
            ctx.sessionAttribute("currentOrder", order);
            ctx.attribute("message", "Order created");
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong, Try again");
            ctx.render("index.html");
        }
    }

    public static void addToBasket (Context ctx, ConnectionPool connectionPool) {

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
        }catch(NumberFormatException e){
            ctx.attribute("message", "Please select a bottom, topping and quantity " + ctx.formParam("bottom") +" "+ctx.formParam("topping") +" " + ctx.formParam("quantity")+ " " + e.getMessage());

            OrderController.showIndexPage(ctx, connectionPool);
        }
    }

    public static List<ProductLine> getProductLineById(Order order, ConnectionPool connectionPool) throws DatabaseException {
        return null;
    }

    public static void getUserBasket(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");

        if(currentOrder == null || currentUser == null){
            ctx.attribute("message", "Basket is empty");
            ctx.render("basketpage.html");
        }

        try {

            List<ProductLine> productLinesList = OrderMapper.getUserBasket(currentOrder, connectionPool);
            if (productLinesList.isEmpty()) {
                ctx.attribute("message", "Basket is empty");
            }else {
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

    public static void getAllBottoms(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Bottom> bottomsList = OrderMapper.getAllBottoms(connectionPool);
            ctx.attribute("bottoms", bottomsList);
            ctx.render("index.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}