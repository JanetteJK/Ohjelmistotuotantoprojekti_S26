package dao;

import java.sql.*;
import static datasource.MariaDBConnection.conn;
import entity.User;

public class UserDao {


    public static void saveUser(User u) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO users (username, passwd, email, role) VALUES (?, PASSWORD(?), ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getPassword());
            //stmt.setString(3, u.email);
            //stmt.setString(4, u.role);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
