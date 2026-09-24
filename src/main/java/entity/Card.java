package entity;

public class Card {
    private String question;
    private String answer;
    private String category;
    private int userId;
    private int fcId;


    public Card(String question, String answer, String category, int userId) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFcId() { return fcId; }
    public void setFcId(int fcId) { this.fcId = fcId; }
}
