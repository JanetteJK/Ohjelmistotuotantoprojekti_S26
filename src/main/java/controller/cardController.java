package controller;

import dao.*;
import javafx.fxml.FXML;
import entity.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class cardController {

    CardDao cardDao = new CardDao();
    UserDao userDao = new UserDao();
    private Stage stage;
    private Scene scene;
    private cardController cc;



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
        Card card = new Card(question, answer, category, userId);
        CardDao.addCard(card);
        questionBox.clear();
        answerBox.clear();
    }

    public void switchToCreateCards() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"));
        Parent root = fxmlLoader.load();
        cc = fxmlLoader.getController();
        stage = (Stage) submitCard.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
