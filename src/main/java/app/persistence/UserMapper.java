package app.persistence;

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
        )
        {
            ps.setString(1, userName);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static void getAllUsers() {
    }

    public static void login(String username, String password) {
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
    }

