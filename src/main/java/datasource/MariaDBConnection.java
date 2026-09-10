package datasource;

import java.sql.*;

public class MariaDBConnection {

    public static Connection conn;

    public static void connect() {
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
    }
}

