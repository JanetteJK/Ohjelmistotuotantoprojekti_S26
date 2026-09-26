package datasource;

import java.sql.*;

public class MariaDBConnection {

    public static Connection conn = null;

    public static Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/flashers_application",
                        "flashcard_team",
                        "salasana"
                );
            } catch (SQLException e) {
                System.out.println("Connection failed.");
                e.printStackTrace();
            }
            return conn;
        }return conn;
    }
}

