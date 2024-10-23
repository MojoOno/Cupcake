package app.persistence;

import app.Main;
import app.entities.Bottom;
import app.entities.Cupcake;
import app.entities.ProductLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderMapper {


    public static void createOrder(int userId, int orderTotal) {
    }

    public static void deleteOrder(int orderId) {
    }

    public static void updateOrder(int orderId, int orderTotal) {
    }

    public static void getOrdersByUser(int userId) {
    }

    public static void getAllOrders() {
    }

    public static void getOrderById(int orderId) {
    }

    public static List<ProductLine> getUserBasket(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<ProductLine> productLines = new ArrayList<>();
        String sql = "SELECT pl.productline_id, t.topping_id, t.topping_price AS topping_price, b.bottom_id, b.bottom_price AS bottom_price " +
                "FROM productline pl " +
                "JOIN orders o ON pl.order_id = o.order_id " +
                "JOIN topping t ON pl.topping_id = t.topping_id " +
                "JOIN bottom b ON pl.bottom_id = b.bottom_id " +
                "WHERE o.user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
            ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Topping topping = new Topping(rs.getInt("topping_id"), rs.getFloat("topping_price"));
                Bottom bottom = new Bottom(rs.getInt("bottom_id"), rs.getFloat("bottom_price"));
                Cupcake cupcake = new Cupcake(bottom, topping);
                ProductLine productLine = new ProductLine(rs.getInt("productline_id"), cupcake);
                productLines.add(productLine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productLines;
    }
}
