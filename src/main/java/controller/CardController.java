package controller;

import dao.CardDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class CardController {
    @FXML
    private Label cardText;
    @FXML
    private Button showAnswerButton;
    @FXML
    private Button deleteCardButton;
    @FXML
    private Label cardCategory;

    private String answer;
    private String question;
    public User user;
    private boolean answerVisible = false;

    public void setCardQuestion(String question, String category) {
        this.question = question;
        cardText.setText(question);
        cardCategory.setText(category);
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

    public void deleteCard() {
        CardDao.deleteCard(this.cardText.getText());
    }

    @FXML
    void showDeleteAlert(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageManager.getBundle().getString("WARNING_delete_card_title"));
        alert.setHeaderText(LanguageManager.getBundle().getString("WARNING_delete_card_title"));
        alert.setContentText(LanguageManager.getBundle().getString("WARNING_delete_card_text"));
        if (alert.showAndWait().get() == ButtonType.OK) {
            CardDao.deleteCard(this.cardText.getText());
            PageController.switchToLibrary(actionEvent);
        }else {
            alert.close();
        }
    }
}
