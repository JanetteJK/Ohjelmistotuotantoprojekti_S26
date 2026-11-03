package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class MariaDBConnectionTest {

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
}
