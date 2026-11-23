package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.Card;
import entity.Quiz;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class QuizControllerTest {

    @Test
    void createQuizShouldCreateQuizForCategoryAndUser() {
        Quiz expectedQuiz = new Quiz(1, 3, 0, 7);
        QuizController controller = new QuizController();

        try (MockedStatic<QuizDao> quizDao = org.mockito.Mockito.mockStatic(QuizDao.class)) {
            quizDao.when(() -> QuizDao.createQuiz("Geography", 7))
                    .thenReturn(expectedQuiz);

            assertSame(expectedQuiz, controller.createQuiz("Geography", 7));

            quizDao.verify(() -> QuizDao.createQuiz("Geography", 7));
        }
    }

    @Test
    void createQuizShouldUseDefaultCategoryWhenCategoryIsBlank() {
        QuizController controller = new QuizController();

        try (MockedStatic<QuizDao> quizDao = org.mockito.Mockito.mockStatic(QuizDao.class)) {
            quizDao.when(() -> QuizDao.createQuiz("default", 7))
                    .thenReturn(null);

            controller.createQuiz("  ", 7);

            quizDao.verify(() -> QuizDao.createQuiz("default", 7));
        }
    }

    @Test
    void createQuizShouldUseDefaultCategoryWhenCategoryIsNull() {
        QuizController controller = new QuizController();

        try (MockedStatic<QuizDao> quizDao = org.mockito.Mockito.mockStatic(QuizDao.class)) {
            quizDao.when(() -> QuizDao.createQuiz("default", 7))
                    .thenReturn(null);

            controller.createQuiz(null, 7);

            quizDao.verify(() -> QuizDao.createQuiz("default", 7));
        }
    }

    @Test
    void openQuizShouldUseDefaultCategoryWhenCategoryIsBlank() {
        QuizController controller = new QuizController();

        try (MockedStatic<CardDao> cardDao = org.mockito.Mockito.mockStatic(CardDao.class)) {
            cardDao.when(() -> CardDao.getCardsBasedOnCategory("default", 7))
                    .thenReturn(List.of());

            assertNotNull(controller.openQuiz("", 7));

            cardDao.verify(() -> CardDao.getCardsBasedOnCategory("default", 7));
        }
    }
}
