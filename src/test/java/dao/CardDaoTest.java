package dao;

import entity.Card;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static datasource.MariaDBConnection.conn;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CardDaoTest {

    @Test
    void addCardShouldInsertCard() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        Card card = new Card("Who is Kiri?", "Kiri is my cat.", "Animals", 1);

        CardDao.addCard(card);

        verify(mockConnection).prepareStatement(
                "INSERT INTO flashcards (question, answer, category, user_id) VALUES (?, ?, ?, ?)"
        );

        verify(mockStatement).setString(1, "Who is Kiri?");
        verify(mockStatement).setString(2, "Kiri is my cat.");
        verify(mockStatement).setString(3, "Animals");
        verify(mockStatement).setInt(4, 1);

        verify(mockStatement).executeUpdate();
    }

    @Test
    void addCardShouldDoNothingWhenConnectionIsNull() {

        conn = null;

        Card card = new Card("Who is Kiri?", "Kiri is my cat.", "Animals", 1);

        assertDoesNotThrow(() ->
                CardDao.addCard(card));

    }

    @Test
    void addCardShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        doThrow(new SQLException("Test database error"))
                .when(mockStatement)
                .executeUpdate();

        Card c = new Card("Who is Kiri?", "Kiri is my cat.", "Animals", 1);

        assertDoesNotThrow(() ->
                CardDao.addCard(c)
        );

        verify(mockStatement).executeUpdate();
    }

    @Test
    void modifyCardShouldUpdateCard() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        Card card = new Card("Who is Veela?", "Veela is my dog", "Animals", 1);

        card.setFcId(5);

        CardDao.modifyCard(card);

        verify(mockConnection).prepareStatement(
                "UPDATE flashcards SET question = ?, answer = ?, category = ? WHERE fc_id = ?"
        );

        verify(mockStatement).setString(1, "Who is Veela?");
        verify(mockStatement).setString(2, "Veela is my dog");
        verify(mockStatement).setString(3, "Animals");
        verify(mockStatement).setInt(4, 5);

        verify(mockStatement).executeUpdate();
    }

    @Test
    void modifyCardShouldDoNothingWhenConnectionIsNull() {

        conn = null;

        Card card = new Card("Who is Veela?", "Veela is my dog", "Animals", 1);

        assertDoesNotThrow(() ->
                CardDao.modifyCard(card));

    }

    @Test
    void modifyCardShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        doThrow(new SQLException("Test database error"))
                .when(mockStatement)
                .executeUpdate();

        Card c = new Card("Who is Kiri?", "Kiri is my cat.", "Animals", 1);

        assertDoesNotThrow(() ->
                CardDao.modifyCard(c)
        );

        verify(mockStatement).executeUpdate();
    }

    @Test
    void showAllCardsShouldShowAllCards() throws Exception {

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        when(mockStatement.executeQuery())
                .thenReturn(mockResultSet);

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(mockResultSet.getString("question"))
                .thenReturn("Question 1")
                .thenReturn("Question 2");

        when(mockResultSet.getString("answer"))
                .thenReturn("Answer 1")
                .thenReturn("Answer 2");

        when(mockResultSet.getString("category"))
                .thenReturn("Test")
                .thenReturn("Test");

        CardDao.showAllCards(1);

        verify(mockConnection).prepareStatement(
                "SELECT question, answer, category FROM flashcards WHERE user_id = ?"
        );

        verify(mockStatement).setInt(1, 1);

        verify(mockStatement).executeQuery();

        verify(mockResultSet, times(3)).next();

        verify(mockResultSet, times(2))
                .getString("question");

        verify(mockResultSet, times(2))
                .getString("answer");

        verify(mockResultSet, times(2))
                .getString("category");
    }

    @Test
    void showAllCardsShouldDoNothingWhenConnectionIsNull() {
        conn = null;

        Card card = new Card("Test question", "Test answer", "Animals", 1);

        assertDoesNotThrow(() ->
                CardDao.showAllCards(1));
    }

    @Test
    void showAllCardsShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Test database error"));

        Card card = new Card(
                "Question",
                "Answer",
                "Animals",
                5
        );

        assertDoesNotThrow(() ->
                CardDao.showAllCards(1)
        );
    }

    @Test
    void showAllCardsBasedOnCategoryShouldShowCardsBasedOnCategories() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        when(mockStatement.executeQuery())
                .thenReturn(mockResultSet);

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(mockResultSet.getString("category"))
                .thenReturn("Animals")
                .thenReturn("Animals");

        when(mockResultSet.getString("question"))
                .thenReturn("Who is Kiri?")
                .thenReturn("Who is Veela?");

        when(mockResultSet.getString("answer"))
                .thenReturn("My cat.")
                .thenReturn("My dog");

        Card card = new Card(
                "Test question",
                "Test answer",
                "Animals",
                5
        );

        CardDao.showCardsBasedOnCategory(card);

        verify(mockConnection).prepareStatement(
                "SELECT question, answer, category FROM flashcards WHERE user_id = ? AND category = ?"
        );

        verify(mockStatement).setInt(1, 5);
        verify(mockStatement).setString(2, "Animals");

        verify(mockStatement).executeQuery();

        verify(mockResultSet, times(3)).next();

        verify(mockResultSet, times(2)).getString("question");
        verify(mockResultSet, times(2)).getString("answer");
        verify(mockResultSet, times(2)).getString("category");
    }

    @Test
    void showCardsBasedOnCategoryShouldDoNothingWhenConnectionIsNull() {
        conn = null;

        Card card = new Card("Test question", "Test answer", "Animals", 5);

        assertDoesNotThrow(() ->
                CardDao.showCardsBasedOnCategory(card));
    }

    @Test
    void showCardsBasedOnCategoryShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Test database error"));

        Card card = new Card(
                "Question",
                "Answer",
                "Animals",
                5
        );

        assertDoesNotThrow(() ->
                CardDao.showCardsBasedOnCategory(card)
        );
    }

}