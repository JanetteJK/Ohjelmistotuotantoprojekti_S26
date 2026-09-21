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

        //todo: get current user credentials to find user id and save card to that user
        /*
        int userId = userDao.getCurrentUserId(currentUser);
        Card card = new Card(question, answer, userId);
        cardDao.saveCard(card);
        */
    }

}
