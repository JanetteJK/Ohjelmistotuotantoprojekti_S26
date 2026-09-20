package dao;
import entity.Card;
import java.sql.*;
import static datasource.MariaDBConnection.conn;


public class CardDao {

    public static void addCard(Card c) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO flashcards (question, answer, user_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, c.getQuestion());
            stmt.setString(2, c.getAnswer());
            stmt.setInt(3, c.getUserId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
