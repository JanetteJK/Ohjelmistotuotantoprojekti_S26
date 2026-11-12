package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.QuizSession;
import entity.Card;
import entity.Quiz;

import java.util.List;

public class QuizController {
    QuizDao qd;
    private static final String PLACEHOLDER_CATEGORY = "default";

    public QuizController(QuizDao qd) {
        this.qd = qd;
    }

    public Quiz getQuiz(String category, int userId) {
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
