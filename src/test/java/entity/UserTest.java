package entity;
import org.junit.jupiter.api.Test;

import static entity.User.Role.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User u = new User("testiukko", "testiukko@email.fi", "testiukonsalasana", STUDENT);

    assertEquals("testiukko", u.getUserName());
    assertEquals("testiukko@email.fi", u.getEmail());
    assertEquals("testiukonsalasana", u.getPassword());
    assertEquals(STUDENT, u.getRole());

    u.setUserName("testiakka");
    u.setEmail("testiakka@email.fi");
    u.setPassword("testiakansalasana");
    u.setRole(TEACHER);
    assertEquals("testiakka", u.getUserName());
    assertEquals("testiakka@email.fi", u.getEmail());
    assertEquals("testiakansalasana", u.getPassword());
    assertEquals(TEACHER, u.getRole());

    u.setUserId(1);
    assertEquals(1, u.getUserId());
    }

}
