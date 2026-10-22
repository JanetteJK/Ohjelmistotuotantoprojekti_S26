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

    private String question;

    private User user;

    CardDao cardDao;

    public void setCardQuestion(String question) {
        this.question = question;
        cardText.setText(question);
    }

    public void getCardAnswer(String answer) {
        this.answer = answer;
    }

    public void setCardAnswer(ActionEvent actionEvent) {
        if (showAnswerButton.getText().equals("Show Answer")) {
            showAnswerButton.setText("Hide Answer");
            cardText.setText(answer);

        }
        else {
            showAnswerButton.setText("Show Answer");
            cardText.setText(question);
        }
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void deleteCard(ActionEvent actionEvent) {
        cardDao.deleteCard(this.cardText.getText());
    }
}
