package dao;

import entity.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static datasource.MariaDBConnection.conn;

public class QuizDao {

    public static Quiz createQuiz(String category, int userId) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return null;
        }

        String sql = "SELECT COUNT(*) FROM flashcards WHERE category = ? AND user_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Quiz quiz = new Quiz(0, rs.getInt(1), 0, userId);
                submitQuiz(quiz);
                return quiz;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void submitQuiz(Quiz quiz) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO quizzes (quiz_length, quiz_points, user_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quiz.getQuizLength());
            stmt.setInt(2, quiz.getQuizPoints());
            stmt.setInt(3, quiz.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Quiz getQuiz(int quizId) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return null;
        }

        String sql = "SELECT quiz_id, quiz_length, quiz_points, user_id "
                + "FROM quizzes WHERE quiz_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quizId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Quiz(
                        rs.getInt("quiz_id"),
                        rs.getInt("quiz_length"),
                        rs.getInt("quiz_points"),
                        rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
