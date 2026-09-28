package dao;

import entity.User;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static datasource.MariaDBConnection.conn;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserDaoTest {

    @Test
    void registerUserCreatesUser() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        User user = new User("Veela", "Veela@student.com", "salasana", User.Role.student);

        UserDao.registerUser(user);

        verify(mockConnection).prepareStatement(
                "INSERT INTO users (username, passwd, email, role) VALUES (?, PASSWORD(?), ?, ?)"
        );

        verify(mockStatement).setString(1, "Veela");
        verify(mockStatement).setString(2, "salasana");
        verify(mockStatement).setString(3, "Veela@student.com");
        verify(mockStatement).setString(4, User.Role.student.toString());

        verify(mockStatement).executeUpdate();
    }

    @Test
    void registerUserDoesNothingWhenConnectionIsNull() {
        conn = null;

        User user = new User("Veela", "Veela@student.com", "salasana", User.Role.student);

        assertDoesNotThrow(() ->
                UserDao.registerUser(user));
    }

    @Test
    void logInUserLoggedIn() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        when(mockStatement.executeQuery())
                .thenReturn(mockResultSet);

        when(mockResultSet.next())
                .thenReturn(true);

        User user = new User("Veela", "Veela@student.com", "salasana", User.Role.student);

        boolean result = UserDao.logInUser(user);

        assertTrue(result);

        verify(mockConnection).prepareStatement(
                "SELECT user_id, username, passwd, role FROM users WHERE username = ? AND passwd = PASSWORD(?)"
        );

        verify(mockStatement).setString(1, "Veela");
        verify(mockStatement).setString(2, "salasana");
        verify(mockStatement).executeQuery();

        verify(mockResultSet).next();
    }

    @Test
    void loginUserDoesNothingWhenConnectionIsNull() {
        conn = null;

        User user = new User("Veela", "Veela@student.com", "salasana", User.Role.student);

        assertDoesNotThrow(() ->
                UserDao.logInUser(user));
    }

}