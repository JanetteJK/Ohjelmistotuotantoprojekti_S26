package datasource;

import dao.LanguageDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MariaDBConnection {
    public static Connection conn = null;
    private static final Logger msg = Logger.getLogger(MariaDBConnection.class.getName());

    public static Connection connect() {
        if (conn == null) {
            try {
                String host;

                if (System.getenv("DOCKER") != null) {
                    host = "host.docker.internal";
                } else {
                    host = "localhost";
                }

                String url = "jdbc:mariadb://" + host
                        + ":3306/flashers_application";

                conn = DriverManager.getConnection(
                        url,
                        "flashcard_team",
                        "salasana"
                );

            } catch (SQLException e) {
                msg.info("Error!");
            }
        }
        return conn;
    }
}