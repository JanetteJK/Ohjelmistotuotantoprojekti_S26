package dao;

import java.sql.*;
import static datasource.MariaDBConnection.conn;

import datasource.MariaDBConnection;
import entity.User;

public class UserDao {



    public static void registerUser(User u) {
        Connection conn = MariaDBConnection.connect();

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

    public static boolean logInUser(User u) {
        Connection conn = MariaDBConnection.connect();

        if (conn == null) {
            System.out.println("Connection is null!");
            return false;
        }

        String sql = "SELECT user_id, username, passwd, role FROM users WHERE username = ? AND passwd = PASSWORD(?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            //System.out.println("Username: " + u.getUserName());
            stmt.setString(1, u.getUserName());
            //System.out.println("Password: " + u.getPassword());
            stmt.setString(2, u.getPassword());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("user_id");
                u.setUserId(userId);

                System.out.println("Login successful!");
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Role: " + rs.getString("role"));
                System.out.println("UserId: " + userId);

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Login failed!");
        return false;

    }

}

