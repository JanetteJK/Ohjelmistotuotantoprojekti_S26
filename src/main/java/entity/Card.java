package entity;

public class Card {
    private String question;
    private String answer;
    private int userId;


    public Card(String answer, String question, int userId) {
        this.question = question;
        this.answer = answer;
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

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
