package dao;

import entity.StudyMaterial;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static datasource.MariaDBConnection.conn;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class StudyMaterialDaoTest {

    @Test
    void uploadStudyMaterialUploadsStudyMaterial() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        StudyMaterial sm = new StudyMaterial("test.pdf", "test", 2);

        StudyMaterialDao.uploadStudyMaterial(sm);

        verify(mockConnection).prepareStatement(
                 "INSERT INTO study_materials (materials, category, user_id) VALUES (?, ?, ?)"
        );

        verify(mockStatement).setString(1, "test.pdf");
        verify(mockStatement).setString(2, "test");
        verify(mockStatement).setInt(3, 2);

        verify(mockStatement).executeUpdate();
    }

    @Test
    void uploadStudyMaterialDoesNothingWhenConnectionIsNull() {
        conn = null;

        StudyMaterial sm = new StudyMaterial("test.pdf", "test", 2);

        assertDoesNotThrow(() ->
                StudyMaterialDao.uploadStudyMaterial(sm));
    }

    @Test
    void uploadStudyMaterialShouldHandleSQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        conn = mockConnection;

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockStatement);

        doThrow(new SQLException("Test database error"))
                .when(mockStatement)
                .executeUpdate();

        StudyMaterial sm = new StudyMaterial(
                "test.pdf",
                "test",
                2
        );

        assertDoesNotThrow(() ->
                StudyMaterialDao.uploadStudyMaterial(sm)
        );

        verify(mockStatement).executeUpdate();
    }
}
