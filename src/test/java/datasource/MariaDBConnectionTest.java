package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MariaDBConnectionTest {

    @BeforeEach
    void setUp() {
        MariaDBConnection.conn = null;
    }

    @AfterEach
    void clearConnection() {
        MariaDBConnection.conn = null;
    }

    @Test
    void connectShouldReturnExistingConnectionWithoutOpeningAnotherConnection() {
        Connection existingConnection = mock(Connection.class);
        MariaDBConnection.conn = existingConnection;

        assertSame(existingConnection, MariaDBConnection.connect());
    }

    @Test
    void connectShouldCreateConnectionWhenConnectionIsNull() throws Exception {
        Connection newConnection = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManager =
                     mockStatic(DriverManager.class)) {

            driverManager.when(() -> DriverManager.getConnection(
                    anyString(), anyString(), anyString()
            )).thenReturn(newConnection);

            Connection result = MariaDBConnection.connect();

            assertSame(newConnection, result);
        }
    }

    @Test
    void connectShouldReturnNullWhenSqlExceptionOccurs() throws Exception {
        SQLException exception = new SQLException("Connection failed");

        try (MockedStatic<DriverManager> driverManager =
                     mockStatic(DriverManager.class)) {

            driverManager.when(() -> DriverManager.getConnection(
                    anyString(), anyString(), anyString()
            )).thenThrow(exception);

            Connection result = MariaDBConnection.connect();

            assertNull(result);
        }
    }
}