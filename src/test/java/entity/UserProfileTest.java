package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    @Test
    public void testUserProfile() {

        UserProfile up = new UserProfile("Veela", 2);

        assertEquals("Veela", up.getCurrentUserName());
        assertEquals(2, up.getCurrentUserId());
    }
}
