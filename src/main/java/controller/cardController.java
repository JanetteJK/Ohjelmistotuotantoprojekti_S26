package controller;

import dao.CardDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class cardController {
    @FXML
    private Label cardText;
    @FXML
    private Button showAnswerButton;
    @FXML
    private Button deleteCardButton;

    private String answer;

    private User user;

    CardDao cardDao;

    public void setCardQuestion(String question) {
        cardText.setText(question);
    }

    public void setCardAnswer(String answer) {
        this.answer = answer;
    }

    public void setCardAnswer(ActionEvent actionEvent) {
        cardText.setText(answer);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void deleteCard(ActionEvent actionEvent) {
        cardDao.deleteCard(this.cardText.getText());
    }
}
