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
    public static void addProductLineToBasket(int orderId, ProductLine productLine, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO productline (order_id, bottom_id, topping_id, quantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productLine.getCupcake().getBottom().getBottomId());
            ps.setInt(3, productLine.getCupcake().getTopping().getToppingId());
            ps.setInt(4, productLine.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomsList = new ArrayList<>();
        String sql = "SELECT * FROM bottom";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("bottom_id");
                String name = rs.getString("name");
                float price = rs.getFloat("price");
                bottomsList.add(new Bottom(id, name, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return bottomsList;
    }

    public static List<Topping> getAllToppings (ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppingsList = new ArrayList<>();
        String sql = "SELECT * FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("topping_id");
                String name = rs.getString("name");
                float price = rs.getFloat("price");
                toppingsList.add(new Topping(id, name, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return toppingsList;
    }

    public static Bottom getBottomById(int bottomId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM bottom WHERE bottom_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bottomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("bottom_id");
                    String name = rs.getString("name");
                    float price = rs.getFloat("price");
                    return new Bottom(id, name, price);
                } else {
                    throw new DatabaseException("Bottom not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
    public static Topping getToppingById(int toppingId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM topping WHERE topping_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, toppingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("topping_id");
                    String name = rs.getString("name");
                    float price = rs.getFloat("price");
                    return new Topping(id, name, price);
                } else {
                    throw new DatabaseException("Topping not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
