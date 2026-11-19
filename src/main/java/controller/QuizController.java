package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.QuizSession;
import entity.Card;
import entity.Quiz;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.List;
import java.util.logging.Logger;

public class QuizController {
    private static final Logger msg = Logger.getLogger(QuizController.class.getName());
    QuizDao qd;
    CardDao cd;
    private List<String> categories;
    private static final String PLACEHOLDER_CATEGORY = "default";
    @FXML
    ChoiceBox<String> categorySelection;
    @FXML
    Button startQuizButton;

    public QuizController() {
        // Default constructor
    }

    public void initialize() {
        // Initialize the controller
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
}
