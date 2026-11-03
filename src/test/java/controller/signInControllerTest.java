package controller;

import entity.User;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class signInControllerTest {

    private signInController controller;
    private TextField teacherUsername;
    private PasswordField teacherPassword;
    private TextField studentUsername;
    private PasswordField studentPassword;

    @BeforeAll
    static void startJavaFxRuntime() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new signInController();
        teacherUsername = mock(TextField.class);
        teacherPassword = mock(PasswordField.class);
        studentUsername = mock(TextField.class);
        studentPassword = mock(PasswordField.class);

        setField(controller, "tUsername", teacherUsername);
        setField(controller, "tPassw", teacherPassword);
        setField(controller, "sUsername", studentUsername);
        setField(controller, "sPassw", studentPassword);
    }

    @Test
    void gettersReturnEnteredCredentials() {
        when(teacherUsername.getText()).thenReturn("teacher");
        when(teacherPassword.getText()).thenReturn("teacher-password");
        when(studentUsername.getText()).thenReturn("student");
        when(studentPassword.getText()).thenReturn("student-password");

        assertEquals("teacher", controller.gettUsername());
        assertEquals("teacher-password", controller.gettPassw());
        assertEquals("student", controller.getSUsername());
        assertEquals("student-password", controller.getsPassw());
    }

    @Test
    void initializePopulatesLanguageChoicesUsingCurrentLocale() throws Exception {
        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        setField(controller, "languageChoice", languageChoice);
        LanguageManager.setLocale(Locale.FRENCH);

        runOnFxThread(() -> controller.initialize());

        assertEquals(4, languageChoice.getItems().size());
        assertEquals("English", languageChoice.getValue());
    }

    @Test
    void initializeSelectsFinnishForFinnishLocale() throws Exception {
        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        setField(controller, "languageChoice", languageChoice);
        LanguageManager.setLocale(new Locale("fi", "FI"));

        runOnFxThread(() -> controller.initialize());

        assertEquals("Suomeksi", languageChoice.getValue());
    }

    @Test
    void initializeWithoutInjectedChoiceBoxDoesNothing() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(controller::initialize);
    }

    @Test
    void setCurrentUserAndGetUserIdUseTheSameUser() {
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(55);

        controller.setCurrentUser(user);

        assertSame(user, getField(controller, "currentUser"));
        assertEquals(55, controller.getUserId());
    }

    @Test
    void setUserGreetingUpdatesTheNameLabel() throws Exception {
        Label nameTag = mock(Label.class);
        setField(controller, "nameTag", nameTag);

        controller.setUserGreeting("Matti");

        verify(nameTag).setText("Matti");
    }

    @Test
    void teacherLoginIgnoresEventsFromOtherControls() throws Exception {
        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(new Object());

        controller.tLogin(event);

        verifyNoInteractions(teacherUsername, teacherPassword);
    }

    @Test
    void studentLoginIgnoresEventsFromOtherControls() throws Exception {
        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(new Object());

        controller.sLogin(event);

        verifyNoInteractions(studentUsername, studentPassword);
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Object getField(Object target, String name) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable throwable) {
                error.set(throwable);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX action did not finish in time");
        if (error.get() != null) {
            throw new AssertionError(error.get());
        }
    }
}
