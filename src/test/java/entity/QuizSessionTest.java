package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizSessionTest {

    @Test
    void nextQuestionShouldReturnQuestionWithThreeOptions() {
        QuizSession session = new QuizSession(cards());

        QuizQuestion question = session.nextQuestion();

        assertNotNull(question);
        assertEquals(3, question.getOptions().size());
        assertTrue(question.getOptions().contains("Answer 1")
                || question.getOptions().contains("Answer 2")
                || question.getOptions().contains("Answer 3"));
        assertFalse(session.isFinished());
    }

    @Test
    void nextQuestionShouldNotReturnAnotherQuestionBeforeAnswering() {
        QuizSession session = new QuizSession(cards());

        assertNotNull(session.nextQuestion());

        assertNull(session.nextQuestion());
    }

    @Test
    void correctAnswerShouldIncreaseScoreAndAdvanceSession() {
        QuizSession session = new QuizSession(cards());

        QuizQuestion question = session.nextQuestion();
        String correctAnswer = cards().stream()
                .filter(card -> card.getQuestion().equals(question.getQuestion()))
                .findFirst()
                .orElseThrow()
                .getAnswer();

        assertTrue(session.answer(correctAnswer));
        assertEquals(1, session.getScore());
        assertNotNull(session.nextQuestion());
    }

    @Test
    void invalidAnswerShouldNotAdvanceSessionOrChangeScore() {
        QuizSession session = new QuizSession(cards());

        session.nextQuestion();

        assertFalse(session.answer("Not an option"));
        assertEquals(0, session.getScore());
        assertNull(session.nextQuestion());
    }

    @Test
    void emptySessionShouldBeFinished() {
        QuizSession session = new QuizSession(List.of());

        assertTrue(session.isFinished());
        assertNull(session.nextQuestion());
        assertEquals(0, session.getScore());
    }

    private static List<Card> cards() {
        return List.of(
                new Card("Question 1", "Answer 1", "Category", 1),
                new Card("Question 2", "Answer 2", "Category", 1),
                new Card("Question 3", "Answer 3", "Category", 1)
        );
    }
}
