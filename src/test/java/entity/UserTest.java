package entity;
import org.junit.jupiter.api.Test;

import static entity.User.Role.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserGetters() {
        User u = new User("testiukko", "testiukko@email.fi", "testiukonsalasana", student);

    assertEquals("testiukko", u.getUserName());
    assertEquals("testiukko@email.fi", u.getEmail());
    assertEquals("testiukonsalasana", u.getPassword());
    assertEquals(student, u.getRole());
    }

}
