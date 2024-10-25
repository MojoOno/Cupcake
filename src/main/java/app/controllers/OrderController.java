package app.controllers;

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
        app.get("/basketpage", ctx -> ctx.render("basketPage.html"));
        app.post("/basketpage", ctx -> getUserBasket(ctx, connectionPool));
        app.post("/add-to-basket", OrderController.addToBasket(connectionPool));

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
    public static Handler addToBasket (ConnectionPool connectionPool) {
        return ctx -> {
            int userId = Integer.parseInt(ctx.formParam("userId"));
            int bottomId = Integer.parseInt(ctx.formParam("kage"));
            int toppingId = Integer.parseInt(ctx.formParam("topping"));
            int quantity = Integer.parseInt(ctx.formParam("antal"));



            try {
                Bottom bottom = OrderMapper.getBottomById(bottomId, connectionPool);
                Topping topping = OrderMapper.getToppingById(toppingId, connectionPool);
                ProductLine productLine = new ProductLine(new Cupcake(bottom, topping), quantity);

                // Create a new order and get the order ID
                int orderId = OrderMapper.createOrder(userId, connectionPool);

                // Add the product line to the newly created order
                OrderMapper.addProductLineToBasket(orderId, productLine, connectionPool);



                ctx.json(Map.of("success", true));
            } catch (DatabaseException e) {
                ctx.json(Map.of("success", false, "message", "Failed to add to cart, try again"));
            }
        };
    }
    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        return OrderMapper.getAllBottoms(connectionPool);
    }
    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        return OrderMapper.getAllToppings(connectionPool);
    }
}

