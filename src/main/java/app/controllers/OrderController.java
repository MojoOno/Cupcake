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

import java.util.List;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/basketpage", ctx -> ctx.render("basketPage.html"));
        app.post("/basketpage", ctx -> getUserBasket(ctx, connectionPool));

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
    public static void addToBasket(Context ctx, ConnectionPool connectionPool){

        int userId = ctx.sessionAttribute("currentUser");
        int bottomID = Integer.parseInt(ctx.formParam("bottomId"));
        int toppingId = Integer.parseInt(ctx.formParam("toppingId"));
        int quantity = Integer.parseInt(ctx.formParam("quantityId"));

        try {
            Bottom bottom = OrderMapper.getBottomById(bottomID, connectionPool);
            Topping topping = OrderMapper.getToppingById(toppingId, connectionPool);
            Cupcake cupcake = new Cupcake(bottom, topping);
            ProductLine productLine = new ProductLine(0,cupcake,quantity);
            OrderMapper.addProductLineToBasket(userId,productLine,connectionPool);
            ctx.redirect("/basketpage");

        } catch (DatabaseException e){
            ctx.attribute("message","Failed to add to cart, try again");
            ctx.render("index.html");
        }


    }
}

