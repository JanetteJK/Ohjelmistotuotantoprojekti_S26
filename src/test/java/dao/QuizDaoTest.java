package dao;

import entity.Quiz;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static datasource.MariaDBConnection.conn;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class QuizDaoTest {

    @Test
    void createQuizShouldUseNumberOfCardsInCategory() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(countStatement)
                .thenReturn(insertStatement);
        when(countStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(12);

        Quiz quiz = QuizDao.createQuiz("Geography", 4);

        assertEquals(12, quiz.getQuizLength());
        assertEquals(0, quiz.getQuizPoints());
        assertEquals(4, quiz.getUserId());
        verify(mockConnection).prepareStatement(
                "SELECT COUNT(*) FROM flashcards WHERE category = ? AND user_id = ?"
        );
        verify(countStatement).setString(1, "Geography");
        verify(countStatement).setInt(2, 4);
        verify(countStatement).executeQuery();
        verify(mockConnection).prepareStatement(
                "INSERT INTO quizzes (quiz_length, quiz_points, user_id) VALUES (?, ?, ?)"
        );
        verify(insertStatement).setInt(1, 12);
        verify(insertStatement).setInt(2, 0);
        verify(insertStatement).setInt(3, 4);
        verify(insertStatement).executeUpdate();
    }

    @Test
    void createQuizShouldReturnNullWhenCountQueryHasNoRows() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(countStatement);
        when(countStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertNull(QuizDao.createQuiz("Geography", 4));
        verify(mockResultSet).next();
        verify(mockConnection).prepareStatement(
                "SELECT COUNT(*) FROM flashcards WHERE category = ? AND user_id = ?"
        );
        verify(countStatement).executeQuery();
    }

    @Test
    void createQuizShouldReturnNullWhenConnectionIsNull() {
        conn = null;

        assertNull(QuizDao.createQuiz("Geography", 4));
    }

    @Test
    void createQuizShouldReturnNullWhenCountingCardsFails() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Test database error"));

        assertNull(QuizDao.createQuiz("Geography", 4));
    }

    @Test
    void submitQuizShouldInsertQuiz() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        Quiz quiz = new Quiz(0, 10, 8, 1);

        QuizDao.submitQuiz(quiz);

        verify(mockConnection).prepareStatement(
                "INSERT INTO quizzes (quiz_length, quiz_points, user_id) VALUES (?, ?, ?)"
        );
        verify(mockStatement).setInt(1, 10);
        verify(mockStatement).setInt(2, 8);
        verify(mockStatement).setInt(3, 1);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void submitQuizShouldDoNothingWhenConnectionIsNull() {
        conn = null;

        Quiz quiz = new Quiz(0, 10, 8, 1);

        assertDoesNotThrow(() -> QuizDao.submitQuiz(quiz));
    }

    @Test
    void submitQuizShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doThrow(new SQLException("Test database error"))
                .when(mockStatement).executeUpdate();

        Quiz quiz = new Quiz(0, 10, 8, 1);

        assertDoesNotThrow(() -> QuizDao.submitQuiz(quiz));
        verify(mockStatement).executeUpdate();
    }

    @Test
    void getQuizShouldReturnQuizFromDatabase() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("quiz_id")).thenReturn(7);
        when(mockResultSet.getInt("quiz_length")).thenReturn(10);
        when(mockResultSet.getInt("quiz_points")).thenReturn(8);
        when(mockResultSet.getInt("user_id")).thenReturn(1);

        Quiz quiz = QuizDao.getQuiz(7);

        assertEquals(7, quiz.getQuizId());
        assertEquals(10, quiz.getQuizLength());
        assertEquals(8, quiz.getQuizPoints());
        assertEquals(1, quiz.getUserId());
        verify(mockConnection).prepareStatement(
                "SELECT quiz_id, quiz_length, quiz_points, user_id "
                        + "FROM quizzes WHERE quiz_id = ?"
        );
        verify(mockStatement).setInt(1, 7);
        verify(mockStatement).executeQuery();
    }

    @Test
    void getQuizShouldReturnNullWhenQuizDoesNotExist() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertNull(QuizDao.getQuiz(7));
    }

    @Test
    void getQuizShouldReturnNullWhenConnectionIsNull() {
        conn = null;

        assertNull(QuizDao.getQuiz(7));
    }

    @Test
    void getQuizShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Test database error"));

        assertNull(QuizDao.getQuiz(7));
    }
}
