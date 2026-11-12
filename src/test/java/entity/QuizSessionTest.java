package entity;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    void nextQuestionShouldPrintAnswerOptionsToConsole() {
        QuizSession session = new QuizSession(cards());
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            QuizQuestion question = session.nextQuestion();
            String printedOptions = output.toString();

            assertNotNull(question);
            assertTrue(printedOptions.contains("Answer options:"));
            for (int i = 0; i < question.getOptions().size(); i++) {
                assertTrue(printedOptions.contains(
                        (i + 1) + ". " + question.getOptions().get(i)
                ));
            }
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void nextQuestionShouldReturnAnotherQuestionWhenCurrentQuestionIsNotStored() {
        QuizSession session = new QuizSession(cards());

        assertNotNull(session.nextQuestion());

        assertNotNull(session.nextQuestion());
    }

    @Test
    void answerShouldNotChangeScoreWhenCurrentQuestionIsNotStored() {
        QuizSession session = new QuizSession(cards());

        QuizQuestion question = session.nextQuestion();
        String correctAnswer = cards().stream()
                .filter(card -> card.getQuestion().equals(question.getQuestion()))
                .findFirst()
                .orElseThrow()
                .getAnswer();

        assertFalse(session.answer(correctAnswer));
        assertEquals(0, session.getScore());
        assertNotNull(session.nextQuestion());
    }

    @Test
    void invalidAnswerShouldNotAdvanceSessionOrChangeScore() {
        QuizSession session = new QuizSession(cards());

        session.nextQuestion();

        assertFalse(session.answer("Not an option"));
        assertEquals(0, session.getScore());
        assertNotNull(session.nextQuestion());
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
