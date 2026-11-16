package dao;
import entity.StudyMaterial;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import static datasource.MariaDBConnection.conn;

public class StudyMaterialDao {

    private static final Logger msg = Logger.getLogger(StudyMaterialDao.class.getName());

    public static void uploadStudyMaterial(StudyMaterial sm) {
        if (conn == null) {
            System.out.println("Connection is null!");
            return;
        }

        String sql = "INSERT INTO study_materials (materials, category, user_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, sm.getMaterial());
            stmt.setString(2, sm.getCategory());
            stmt.setInt(3, sm.getUserId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            msg.info("Error!");
        }
    }
}
