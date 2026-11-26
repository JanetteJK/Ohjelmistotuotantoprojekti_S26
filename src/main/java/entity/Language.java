package entity;

public class Language {
    private String lang;
    private String translation;
    private String message;


    public Language(String lang, String translation, String message) {
        this.lang = lang;
        this.translation = translation;
        this.message = message;
    }

    public String getLangCode() {
        return lang;
    }
    public void setLangCode(String lang) {
        this.lang = lang;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
