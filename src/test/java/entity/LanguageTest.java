package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void constructor_shouldSetAllFields() {
        Language language = new Language("FI", "Finnish", "Hei!");

        assertEquals("FI", language.getLangCode());
        assertEquals("Finnish", language.getTranslation());
        assertEquals("Hei!", language.getMessage());
    }

    @Test
    void setLangCode_shouldUpdateLangCode() {
        Language language = new Language("FI", "Finnish", "Hei!");

        language.setLangCode("EN");

        assertEquals("EN", language.getLangCode());
    }

    @Test
    void setLanguage_shouldUpdateLanguage() {
        Language language = new Language("FI", "Finnish", "Hei!");

        language.setTranslation("English");

        assertEquals("English", language.getTranslation());
    }

    @Test
    void setMessage_shouldUpdateMessage() {
        Language language = new Language("FI", "Finnish", "Hei!");

        language.setMessage("Hello!");

        assertEquals("Hello!", language.getMessage());
    }
}
