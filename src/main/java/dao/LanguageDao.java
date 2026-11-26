package dao;

import entity.Language;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static datasource.MariaDBConnection.conn;

public class LanguageDao {

    private LanguageDao () {
        // Private constructor to prevent instantiation
    }

    private static final Logger msg = Logger.getLogger(LanguageDao.class.getName());

    public static void chooseLanguage(Language lang) {
        if (conn == null) {
            return;
        }

        String sql = "SELECT lang, translated_lang, db_message FROM languages WHERE lang = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            msg.info("Language chosen: " + lang.getTranslation());

            stmt.setString(1, lang.getLangCode());

            stmt.executeQuery();

        } catch (SQLException e) {
            msg.info("Error:" + e.getMessage()
            );
        }

    }

    public static List<Language> getAllLanguages() {
        List<Language> languages = new ArrayList<>();

        if (conn == null) {
            return languages;
        }

        String sql = "SELECT lang, translated_lang, db_message FROM languages";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String langCode = rs.getString("lang");
                String translatedLang = rs.getString("translated_lang");
                String dbMessage = rs.getString("db_message");

                Language language = new Language(langCode, translatedLang, dbMessage);
                languages.add(language);
            }

            stmt.executeQuery();

        } catch (SQLException e) {
            msg.info("Error retrieving languages from the database: " + e.getMessage());
        }

        return languages;
    }
}
