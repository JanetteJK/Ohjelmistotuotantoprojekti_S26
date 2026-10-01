package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class cardController {
    @FXML
    private Label cardText;

    public void setCardQuestion(String question) {
        cardText.setText(question);
    }

    public void setCardAnswer(String answer) {
        cardText.setText(answer);
    }
}
