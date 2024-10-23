package app.persistence;

import app.entities.ProductLine;

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

    public static List<ProductLine> getUserBasket(int userId) {
        List<ProductLine> productLines = new ArrayList<>();
        String query = "SELECT pl.productline_id, t.topping_id, t.price AS topping_price, b.bottom_id, b.price AS bottom_price\n" +
                "FROM productline pl\n" +
                "JOIN orders o ON pl.order_id = o.order_id\n" +
                "JOIN topping t ON pl.topping_id = t.topping_id\n" +
                "JOIN bottom b ON pl.bottom_id = b.bottom_id\n" +
                "WHERE o.user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductLine productLine = new ProductLine(
                        rs.getInt("productline_id"),
                        rs.getInt("topping_id"),
                        rs.getDouble("topping_price"),
                        rs.getInt("bottom_id"),
                        rs.getDouble("bottom_price")
                );
                productLines.add(productLine);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return productLines;
    }
}
