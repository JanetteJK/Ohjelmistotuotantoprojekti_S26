package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    void testCard() {
        Card c = new Card("What is love?", "Baby don't hurt me", "song", 2);

        assertEquals("What is love?", c.getQuestion());
        assertEquals("Baby don't hurt me", c.getAnswer());
        assertEquals("song", c.getCategory());
        assertEquals(2, c.getUserId());

        c.setQuestion("What is this test about?");
        c.setAnswer("Testing setters");
        c.setCategory("test");
        c.setUserId(1);
        assertEquals("What is this test about?", c.getQuestion());
        assertEquals("Testing setters", c.getAnswer());
        assertEquals("test", c.getCategory());
        assertEquals(1, c.getUserId());
    }
}
