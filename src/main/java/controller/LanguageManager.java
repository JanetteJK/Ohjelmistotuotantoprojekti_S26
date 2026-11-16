package controller;

import dao.LanguageDao;
import entity.Language;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static Locale locale = new Locale("en", "EN");

    private static List<Language> languages = List.of();

    public static void initializeLanguages() {
        languages = List.copyOf(LanguageDao.getAllLanguages());
    }

    private LanguageManager(){}

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        LanguageManager.locale = locale;
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("messages_bundle", locale);
    }

    public static List<Language> getLanguages() {
        return languages;
    }
}
