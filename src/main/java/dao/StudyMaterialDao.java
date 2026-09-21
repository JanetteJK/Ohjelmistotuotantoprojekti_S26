package dao;
import entity.StudyMaterial;

import java.sql.*;
import static datasource.MariaDBConnection.conn;

public class StudyMaterialDao {

    public static void uploadStudyMaterial(StudyMaterial sm) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO study_materials (materials, category, user_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, sm.getMaterials());
            stmt.setString(2, sm.getCategory());
            stmt.setInt(3, sm.getUserId());

            stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
