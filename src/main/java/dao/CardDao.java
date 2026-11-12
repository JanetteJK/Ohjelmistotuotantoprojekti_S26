package dao;
import entity.Card;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static datasource.MariaDBConnection.conn;


public class CardDao {
    


    private static final Logger msg = Logger.getLogger(CardDao.class.getName());

    public static void addCard(Card c) {
        if (conn == null) {
            return;
        }

        String sql = "INSERT INTO flashcards (question, answer, category, user_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, c.getQuestion());
            stmt.setString(2, c.getAnswer());
            stmt.setString(3, c.getCategory());
            stmt.setInt(4, c.getUserId());

            stmt.executeUpdate();

        } catch (SQLException _) {
            msg.info("Card already exists!");
        }

    }

    public static void modifyCard(Card c) {
        if (conn == null) {

            return;
        }

        String sql = "UPDATE flashcards SET question = ?, answer = ?, category = ? WHERE fc_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, c.getQuestion());
            stmt.setString(2, c.getAnswer());
            stmt.setString(3, c.getCategory());
            stmt.setInt(4, c.getFcId());

            stmt.executeUpdate();

        } catch (SQLException _) {
            msg.info("Card already exists!");
        }
    }

    public static ArrayList<Card> showAllCards(int userId) {

        ArrayList<Card> cardList = new ArrayList<>();

        if (conn == null) {
            return new ArrayList<>();
        }

        String sql = "SELECT question, answer, category FROM flashcards WHERE user_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String question = rs.getString("question");
                String answer = rs.getString("answer");
                String category = rs.getString("category");

                Card card = new Card(
                        question,
                        answer,
                        category,
                        userId
                );

                cardList.add(card);
            }

        } catch (SQLException _) {
            msg.info("No cards found!");
        }
        return cardList;
    }


    public static ArrayList<Card> getCardsBasedOnCategory(String cat, int userid) {
        ArrayList<Card> cardList = new ArrayList<>();

        if (conn == null) {
            return cardList;
        }

        String sql = "SELECT question, answer, category FROM flashcards WHERE user_id = ? AND category = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userid);
            stmt.setString(2, cat);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                Card card = new Card(
                        rs.getString("question"),
                        rs.getString("answer"),
                        rs.getString("category"),
                        userid
                );
                cardList.add(card);
            }

        } catch (SQLException _) {
            msg.info("No cards found!");
        }
        return cardList;
    }

    public static void deleteCard(String question) {
        if (conn == null) {
            return;
        }

        String sql = "DELETE FROM flashcards WHERE question = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, question);
            stmt.executeUpdate();

        } catch (SQLException _) {
            msg.info("Card does not exist!");
        }

    }

}
