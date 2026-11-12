package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuizQuestionTest {

    @Test
    void quizQuestionShouldExposeQuestionAndOptions() {
        List<String> options = List.of("Paris", "Helsinki", "Rome");

        QuizQuestion quizQuestion = new QuizQuestion("What is the capital of Finland?", options);

        assertEquals("What is the capital of Finland?", quizQuestion.getQuestion());
        assertEquals(options, quizQuestion.getOptions());
    }

    @Test
    void quizQuestionShouldCopyAndProtectOptions() {
        List<String> options = new ArrayList<>(List.of("A", "B", "C"));
        QuizQuestion quizQuestion = new QuizQuestion("Question", options);

        options.add("D");

        assertEquals(List.of("A", "B", "C"), quizQuestion.getOptions());
        assertThrows(
                UnsupportedOperationException.class,
                () -> quizQuestion.getOptions().add("D")
        );
    }
}
