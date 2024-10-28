package app.persistence;

import app.entities.*;
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

    public static ProductLine addToBasket(User user, Order order, ProductLine productLine, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO productline (total_price, bottom_id, topping_id, quantity, order_id) VALUES (?, ?, ?, ?, ?)";
        ProductLine newProductLine = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productLine.getCupcake().getPrice());
            ps.setInt(2, productLine.getCupcake().getBottom().getBottomId());
            ps.setInt(3, productLine.getCupcake().getTopping().getToppingId());
            ps.setInt(4, productLine.getQuantity());
            ps.setInt(5, order.getOrderId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int productLineId = rs.getInt(1);
                Cupcake newCupcake = new Cupcake(productLine.getCupcake().getBottom(), productLine.getCupcake().getTopping());
                newProductLine = new ProductLine(productLineId, newCupcake, productLine.getQuantity(), productLine.getTotalPrice());
            } else { throw new DatabaseException("Error adding to basket");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding to basket", e.getMessage());
        }
        return newProductLine;
    }

    public static Order createOrder(User user, ConnectionPool connectionPool) throws DatabaseException {
        String sql ="INSERT INTO orders (order_price, user_id) VALUES (?, ?)";
        Order newOrder = null;
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ) { ps.setFloat(1, 0);
            ps.setInt(2, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int newOrderId = rs.getInt(1);
                newOrder = new Order(newOrderId, user.getUserId(), 0);
            } else {
                throw new DatabaseException("Error creating order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating order", e.getMessage());
        }
        return newOrder;
    }


    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order", e.getMessage());
        }
    }

    public static void updateOrder(int orderId, String newStatus, ConnectionPool connectionPool) throws DatabaseException {
    }

    public static List<Order> getOrdersByUserId(Order order, ConnectionPool connectionPool) throws DatabaseException {
        order = null;
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, order.getUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int user_id = rs.getInt("user_id");
                float order_price = rs.getFloat("order_price");
                order = new Order(order_id, user_id, order_price);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred with the database, try again", e.getMessage());
        }
        return orderList;
    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Order order = new Order(rs.getInt("order_id"), rs.getInt("user_id"), rs.getFloat("order_price"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred with the database, try again", e.getMessage());
        }
        return orderList;


    }

    public static List<ProductLine> getProductLineById(Order order, ConnectionPool connectionPool) throws DatabaseException {
        return null;
    }

    public static List<ProductLine> getUserBasket(Order order, ConnectionPool connectionPool) throws DatabaseException {
        List<ProductLine> productLinesList = new ArrayList<>();
        String sql = "SELECT pl.productline_id, pl.quantity, pl.total_price, t.topping_name, t.topping_price,  b.bottom_name, b.bottom_price " +
                "FROM productline pl " +
                "JOIN orders o ON pl.order_id = o.order_id " +
                "JOIN topping t ON pl.topping_id = t.topping_id " +
                "JOIN bottom b ON pl.bottom_id = b.bottom_id " +
                "WHERE pl.order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,order.getOrderId() );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productLineId = rs.getInt("productline_id");
                String toppingName = rs.getString("topping_name");
                float toppingPrice = rs.getFloat("topping_price");
                String bottomName = rs.getString("bottom_name");
                float bottomPrice = rs.getFloat("bottom_price");
                int quantity = rs.getInt("quantity");
                float totalPrice = rs.getFloat("total_price");
                Topping topping = new Topping(toppingName, toppingPrice);
                Bottom bottom = new Bottom(bottomName, bottomPrice);
                Cupcake cupcake = new Cupcake(bottom, topping);
                ProductLine productLine = new ProductLine(productLineId, cupcake, quantity, totalPrice);
                productLinesList.add(productLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred with the database, try again", e.getMessage());
        }

        return productLinesList;
    }
    public static void setOrderStatus(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET paid_status = true WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error setting order to paid");
            }
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred with the database, try again", e.getMessage());
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
                String name = rs.getString("bottom_name");
                float price = rs.getFloat("bottom_price");
                bottomsList.add(new Bottom(id, name, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("error getting bottoms " + e.getMessage());
        }

        return bottomsList;
    }

    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppingsList = new ArrayList<>();
        String sql = "SELECT * FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("topping_id");
                String name = rs.getString("topping_name");
                float price = rs.getFloat("topping_price");
                toppingsList.add(new Topping(id, name, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("error getting toppings " + e.getMessage());
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
                    String name = rs.getString("bottom_name");
                    float price = rs.getFloat("bottom_price");
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
                    String name = rs.getString("topping_name");
                    float price = rs.getFloat("topping_price");
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
