package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {

    @Test
    public void testQuiz() {
        Quiz q = new Quiz(2, 5, 4, 1);

        assertEquals(2, q.getQuizId());
        assertEquals(5, q.getQuizLength());
        assertEquals(4, q.getQuizPoints());
        assertEquals(1, q.getUserId());

        q.setQuizId(3);
        q.setQuizLength(10);
        q.setQuizPoints(8);
        q.setUserId(2);
        assertEquals(3, q.getQuizId());
        assertEquals(10, q.getQuizLength());
        assertEquals(8, q.getQuizPoints());
        assertEquals(2, q.getUserId());
    }
}
