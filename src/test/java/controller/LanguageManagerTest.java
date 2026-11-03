package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LanguageManagerTest {

    private final Locale originalLocale = LanguageManager.getLocale();

    @AfterEach
    void restoreLocale() {
        LanguageManager.setLocale(originalLocale);
    }

    @Test
    void setLocaleAndGetLocaleShouldRoundTrip() {
        Locale finnish = new Locale("fi", "FI");

        LanguageManager.setLocale(finnish);

        assertEquals(finnish, LanguageManager.getLocale());
    }

    @Test
    void getBundleShouldLoadBundleForSelectedLocale() {
        LanguageManager.setLocale(new Locale("fi", "FI"));

        ResourceBundle bundle = LanguageManager.getBundle();

        assertNotNull(bundle);
        assertEquals("Kirjautuminen epäonnistui", bundle.getString("ELEM_login_failed"));
    }
}
