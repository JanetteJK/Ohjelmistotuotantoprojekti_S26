package dao;

import datasource.MariaDBConnection;
import entity.Language;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LanguageDaoTest {

    private Connection originalConnection;
    private Connection connection;
    private PreparedStatement statement;

    private static final String SQL =
            "SELECT lang, translated_lang, db_message FROM languages WHERE lang = ?";

    @BeforeEach
    void setUp() {
        originalConnection = MariaDBConnection.conn;

        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
    }

    @AfterEach
    void tearDown() {
        MariaDBConnection.conn = originalConnection;
    }

    @Test
    void chooseLanguage_whenConnectionIsNull_shouldReturn() {
        MariaDBConnection.conn = null;

        Language language = new Language("FI", "Finnish", "Hei!");

        assertDoesNotThrow(() -> LanguageDao.chooseLanguage(language));
    }

    @Test
    void chooseLanguage_shouldExecuteQueryWithCorrectLanguageCode()
            throws SQLException {

        MariaDBConnection.conn = connection;

        when(connection.prepareStatement(SQL)).thenReturn(statement);

        Language language = new Language("FI", "Finnish", "Hei!");

        LanguageDao.chooseLanguage(language);

        verify(connection).prepareStatement(SQL);
        verify(statement).setString(1, "FI");
        verify(statement).executeQuery();
    }

    @Test
    void chooseLanguage_whenSQLExceptionOccurs_shouldNotThrowException()
            throws SQLException {

        MariaDBConnection.conn = connection;

        when(connection.prepareStatement(SQL))
                .thenThrow(new SQLException("Database error"));

        Language language = new Language("FI", "Finnish", "Hei!");

        assertDoesNotThrow(() -> LanguageDao.chooseLanguage(language));

        verify(connection).prepareStatement(SQL);
    }
}
