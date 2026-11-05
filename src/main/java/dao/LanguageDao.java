package dao;

import entity.Language;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static datasource.MariaDBConnection.conn;

public class LanguageDao {

    public static void chooseLanguage(Language lang) {
        if (conn == null) {
            return;
        }

        String sql = "SELECT lang, translated_lang, db_message FROM languages WHERE lang = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            System.out.println("Language chosen: " + lang.getLanguage());

            stmt.setString(1, lang.getLangCode());

            stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

}

}
