package controller;

import dao.CardDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CardController {
    @FXML
    private Label cardText;
    @FXML
    private Button showAnswerButton;
    @FXML
    private Button deleteCardButton;

    private String answer;
    private String question;
    public User user;
    private boolean answerVisible = false;

    public void setCardQuestion(String question) {
        this.question = question;
        cardText.setText(question);
    }

    public void getCardAnswer(String answer) {
        this.answer = answer;
    }

    public void setCardAnswer(ActionEvent actionEvent) {
        var bundle = LanguageManager.getBundle();
        if (answerVisible) {
            answerVisible = false;
            showAnswerButton.setText(bundle.getString("ELEM_show_answer"));
            cardText.setText(question);
        } else {
            answerVisible = true;
            showAnswerButton.setText(bundle.getString("ELEM_hide_answer"));
            cardText.setText(answer);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void deleteCard(ActionEvent actionEvent) {
        CardDao.deleteCard(this.cardText.getText());
    }
}
