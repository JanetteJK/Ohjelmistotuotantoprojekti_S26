package entity;

public class Quiz {
    private int quizId;
    private int userId;
    private int quiz_length;
    private int quiz_points;

    public Quiz(int quizId, int quiz_length, int quiz_points, int userId) {
        this.quizId = quizId;
        this.userId = userId;
        this.quiz_length = quiz_length;
        this.quiz_points = quiz_points;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
     public int getQuizLength() {
        return quiz_length;
    }

    public void setQuizLength(int quiz_length) {
        this.quiz_length = quiz_length;
    }

    public int getQuizPoints() {
        return quiz_points;
    }

    public void setQuizPoints(int quiz_points) {
        this.quiz_points = quiz_points;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
