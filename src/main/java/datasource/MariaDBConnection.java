package datasource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class MariaDBConnection {
    public static Connection conn = null;
    private static final Logger msg = Logger.getLogger(MariaDBConnection.class.getName());

    private MariaDBConnection(){
        // Private constructor to prevent instantiation
    }

    public static Connection connect() {
        if (conn == null) {
            try {
                String host;
                Properties props = new Properties();

                props.load(new FileInputStream("config.properties"));

                String user = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                if (System.getenv("DOCKER") != null) {
                    host = "host.docker.internal";
                } else {
                    host = "localhost";
                }

                String url = "jdbc:mariadb://" + host
                        + ":3306/flashers_application";

                conn = DriverManager.getConnection(
                        url,
                        user,
                        password
                );

            } catch (SQLException _) {
                msg.info("Error!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return conn;
    }
}