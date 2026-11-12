package dao;

import entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static datasource.MariaDBConnection.conn;

public class UserDao {


    public static void registerUser(User u) {
        if (conn == null) {
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

        } catch (SQLException _) {
            Logger.getLogger(UserDao.class.getName()).info("User already exists!");
        }
    }

    public static boolean logInUser(User u) {
        if (conn == null) {
            return false;
        }

        String sql = "SELECT user_id, username, passwd, role FROM users WHERE username = ? AND passwd = PASSWORD(?) AND role = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getRole().name());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("user_id");
                u.setUserId(userId);
                return true;
            }

        } catch (SQLException _) {
            Logger.getLogger(UserDao.class.getName()).info("User does not exist!");
        }
        return false;

    }

}

