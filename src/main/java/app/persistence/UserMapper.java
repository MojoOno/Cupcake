package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper {

    public static void createUser(String userName, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (user_name, user_password) values (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, userName);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where user_name =? and user_password =?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                float balance = rs.getFloat("balance");
                boolean isAdmin = rs.getBoolean("is_admin");
                return new User(userId, username, password, balance, isAdmin);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static boolean isUserAdmin(int userId, ConnectionPool connectionPool) throws DatabaseException {
        {
            String sql = "SELECT is_admin FROM users WHERE user_id = ?";

            try (
                    Connection connection = connectionPool.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql)
            ) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("is_admin");
                } else {
                    throw new DatabaseException("User not found");
                }
            } catch (SQLException e) {
                throw new DatabaseException("Database error", e.getMessage());
            }
        }
    }

    public static void updateBalance(User user, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update users set balance = ? where user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setFloat(1, user.getBalance());
            ps.setInt(2, user.getUserId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af balance");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved opdatering af balance", e.getMessage());
        }
    }
}