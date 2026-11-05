package entity;

public class Language {
    private String lang_code;
    private String language;
    private String message;


    public Language(String lang_code, String language, String message) {
        this.lang_code = lang_code;
        this.language = language;
        this.message = message;
    }

    public String getLangCode() {
        return lang_code;
    }
    public void setLangCode(String lang_code) {
        this.lang_code = lang_code;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
