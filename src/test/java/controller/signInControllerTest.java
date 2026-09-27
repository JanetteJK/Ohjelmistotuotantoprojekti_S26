package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class signInControllerTest {

    private signInController controller;

    private TextField teacherUsername;
    private PasswordField teacherPassword;
    private Button teacherLoginButton;

    private TextField studentUsername;
    private PasswordField studentPassword;
    private Button studentLoginButton;

    private Hyperlink createAccountLink;

    @BeforeAll
    static void startJavaFxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            latch.countDown();
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX runtime did not start in time.");
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new signInController();

        teacherUsername = new TextField();
        teacherPassword = new PasswordField();
        teacherLoginButton = new Button();

        studentUsername = new TextField();
        studentPassword = new PasswordField();
        studentLoginButton = new Button();

        createAccountLink = new Hyperlink();

        setPrivateField(controller, "tUsername", teacherUsername);
        setPrivateField(controller, "tPassw", teacherPassword);
        setPrivateField(controller, "tOk", teacherLoginButton);

        setPrivateField(controller, "sUsername", studentUsername);
        setPrivateField(controller, "sPassw", studentPassword);
        setPrivateField(controller, "sOk", studentLoginButton);

        setPrivateField(controller, "createAccount", createAccountLink);
    }

    @Test
    void gettUsernameReturnsTeacherUsernameText() {
        teacherUsername.setText("teacherUser");

        String result = controller.gettUsername();

        assertEquals("teacherUser", result);
    }

    @Test
    void gettPasswReturnsTeacherPasswordText() {
        teacherPassword.setText("teacherPassword");

        String result = controller.gettPassw();

        assertEquals("teacherPassword", result);
    }

    @Test
    void getSUsernameReturnsStudentUsernameText() {
        studentUsername.setText("studentUser");

        String result = controller.getSUsername();

        assertEquals("studentUser", result);
    }

    @Test
    void getsPasswReturnsStudentPasswordText() {
        studentPassword.setText("studentPassword");

        String result = controller.getsPassw();

        assertEquals("studentPassword", result);
    }

    @Test
    void tLoginDoesNothingWhenEventSourceIsNotTeacherLoginButton() {
        Button otherButton = new Button();
        ActionEvent event = new ActionEvent(otherButton, null);

        assertDoesNotThrow(() -> controller.tLogin(event));
    }

    @Test
    void sLoginDoesNothingWhenEventSourceIsNotStudentLoginButton() {
        Button otherButton = new Button();
        ActionEvent event = new ActionEvent(otherButton, null);

        assertDoesNotThrow(() -> controller.sLogin(event));
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}