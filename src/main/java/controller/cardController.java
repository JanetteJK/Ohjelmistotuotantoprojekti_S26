package controller;

import dao.*;
import javafx.fxml.FXML;
import entity.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.*;

public class cardController {

    CardDao cardDao = new CardDao();
    UserDao userDao = new UserDao();



    @FXML
    private TextArea questionBox;
    @FXML
    private TextArea answerBox;
    @FXML
    private Button submitCard;

    public String getQuestion() {
        return questionBox.getText();
    }

    public String getAnswer() {
        return answerBox.getText();
    }

    public void submitCard() {
        String question = getQuestion();
        String answer = getAnswer();
        String category = "default"; // Placeholder for category, todo: implement category selection logic
        //int userId = userDao.getCurrentUserId(currentUser);
        int userId = 1; // Placeholder for the current user's ID, todo: replace with actual logic to get the logged-in user's ID§
        Card card = new Card(question, answer, category ,userId);
        CardDao.addCard(card);
        questionBox.clear();
        answerBox.clear();
    }

}
