package controller;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static Locale locale = new Locale("en", "EN");

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
}
