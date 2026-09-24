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
    }
}
