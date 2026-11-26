package controller;


import javafx.fxml.FXML;
import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CcController {

    private static final Logger log = LoggerFactory.getLogger(CcController.class);
    private Stage stage;
    private Scene scene;
    List<Card> cards = new ArrayList<>();
    private User user;

    @FXML
    TextArea questionBox;
    @FXML
    TextArea answerBox;
    @FXML
    TextField categoryField;
    @FXML
    Label cardSentLabel;

    // cardCreation functions
    public String getQuestion() {
        return questionBox.getText();
    }

    public String getAnswer() {
        return answerBox.getText();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public void submitCard(){
        String question = getQuestion();
        String answer = getAnswer();
        String category = getCategory();
        Card cardS = new Card(question, answer, category, user.getUserId());
        CardDao.addCard(cardS);
        questionBox.clear();
        answerBox.clear();
        categoryField.clear();
    }

    //switch funtions

    public void switchToLibrary(MouseEvent actionEvent) throws IOException {
        PageController.switchToLibrary(actionEvent);
    }

    public void switchToCreateQuiz(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToCreateQuiz(actionEvent);
    }

    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToProfile(actionEvent);
    }

}
