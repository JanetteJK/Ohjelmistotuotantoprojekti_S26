package dao;

import entity.Card;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static datasource.MariaDBConnection.conn;
import static org.mockito.Mockito.*;

class CardDaoTest {

    @Test
    void addCardShouldInsertCard() throws Exception {
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

        CardDao.addCard(card);

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
}