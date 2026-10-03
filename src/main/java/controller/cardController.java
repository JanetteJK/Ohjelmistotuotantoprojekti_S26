package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class cardController {
    @FXML
    private Label cardText;
    @FXML
    private Button showAnswerButton;

    private String answer;

    public void setCardQuestion(String question) {
        cardText.setText(question);
    }

    public void setCardAnswer(String answer) {
        this.answer = answer;
    }

    public void setCardAnswer(ActionEvent actionEvent) {
        cardText.setText(answer);
    }
}
