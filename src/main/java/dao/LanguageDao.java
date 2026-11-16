package dao;

import entity.Language;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import static datasource.MariaDBConnection.conn;

public class LanguageDao {

    private static final Logger msg = Logger.getLogger(LanguageDao.class.getName());

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
            msg.info("Error!");
        }

}

}
