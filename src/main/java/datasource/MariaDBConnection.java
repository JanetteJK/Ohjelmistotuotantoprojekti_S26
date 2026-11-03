package datasource;

import java.sql.*;

public class MariaDBConnection {
    public static Connection conn = null;

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
                System.out.println("Connection failed.");
                e.printStackTrace();
            }
        }
        return conn;
    }
}