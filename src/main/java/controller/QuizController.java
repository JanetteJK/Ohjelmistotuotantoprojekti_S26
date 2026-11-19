package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.QuizSession;
import entity.Card;
import entity.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class QuizController {
    QuizDao qd;
    private static final String PLACEHOLDER_CATEGORY = "default";
    @FXML
    ChoiceBox<String> categorySelection;
    @FXML
    Button startQuizButton;

    public QuizController() {
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

}
