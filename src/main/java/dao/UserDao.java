package dao;

import java.sql.*;
import static datasource.MariaDBConnection.conn;
import entity.User;

public class UserDao {


    public static void registerUser(User u) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO users (username, passwd, email, role) VALUES (?, PASSWORD(?), ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getRole().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logInUser(User u) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "SELECT * FROM users WHERE username = ? AND passwd = PASSWORD(?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getPassword());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
