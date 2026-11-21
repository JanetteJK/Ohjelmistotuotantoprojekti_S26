package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

public class QuizController {
    private static final Logger msg = Logger.getLogger(QuizController.class.getName());
    QuizDao qd;
    CardDao cd;
    private List<String> categories;
    private static final String PLACEHOLDER_CATEGORY = "default";
    private String correctAnswer;
    private int score = 0;
    private int index = 0;
    private List<String> options = new java.util.ArrayList<>();
    private List<Card> quizCards;
    @FXML
    ChoiceBox<String> categorySelection;
    @FXML
    Button startQuizButton;
    @FXML
    GridPane cardSlot;
    @FXML
    Label questionSlot;
    @FXML
    RadioButton answer1;
    @FXML
    RadioButton answer2;
    @FXML
    RadioButton answer3;

    public QuizController() {
        // Default constructor
    }

    public void initialize() {

    }

    public Quiz createQuiz(String category, int userId) {
        if (category == null || category.isBlank()) {
            category = PLACEHOLDER_CATEGORY;
        }

        return QuizDao.createQuiz(category, userId);
    }

    public QuizSession openQuiz(String category, int userId) {
        if (category == null || category.isBlank()) {
            category = PLACEHOLDER_CATEGORY;
        }

        final List<Card> cards = CardDao.getCardsBasedOnCategory(category, userId);
        return new QuizSession(cards);
    }

    public void setCategories (List<String> categories) {
        this.categories = categories;
    }

    public void populateCategorySelection(List<String> categories) {
        if (categorySelection != null && categories != null) {
            categorySelection.getItems().clear();
            categorySelection.getItems().addAll(categories);
        }
    }
    @FXML
    public void setQuestion() {
        for (Card card : quizCards) {
            options.add(card.getAnswer());
        }
        if (index < quizCards.size()) {
            questionSlot.setText(quizCards.get(index).getQuestion());
            correctAnswer = quizCards.get(index).getAnswer();
            String random1 = options.get(new Random().nextInt(options.size()));
            String random2 = options.get(new Random().nextInt(options.size()));
            answer1.setText(correctAnswer);
            answer2.setText(random1);
            answer3.setText(random2);
        }
    }

    public void startQuiz(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/quizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        QuizController qc = fxmlLoader.getController();
        List<Card> cards = CardDao.getCardsBasedOnCategory(categorySelection.getValue(), 1);
        qc.quizCards = cards;
        qc.setQuestion();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void checkAnswer(javafx.event.ActionEvent actionEvent) {
    if (answer1.isSelected()) {
        if (answer1.getText().equals(correctAnswer)) {
            score++;
        }
        answer1.setSelected(false);
    }
    if (answer2.isSelected()) {
        if (answer2.getText().equals(correctAnswer)) {
            score++;
        }
        answer2.setSelected(false);
    }
    if (answer3.isSelected()) {
        if (answer3.getText().equals(correctAnswer)) {
            score++;
        }
        answer3.setSelected(false);
    }
    index++;
    setQuestion();
    }
}
