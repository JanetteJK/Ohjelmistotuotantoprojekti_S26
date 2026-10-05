package controller;

import dao.UserDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;

import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class signInControllerTest {

    private signInController controller;

    @Mock
    private TextField tUsername;

    @Mock
    private PasswordField tPassw;

    @Mock
    private TextField sUsername;

    @Mock
    private PasswordField sPassw;

    @Mock
    private Label nameTag;

    @Mock
    private Hyperlink createAccount;

    @Mock
    private ActionEvent event;

    @Mock
    private ActionEvent tEvent;

    @Mock
    private ActionEvent sEvent;

    @Mock
    private Button tOk;

    @Mock
    private Button sOk;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        controller = new signInController();

        when(tEvent.getSource()).thenReturn(tOk);
        when(sEvent.getSource()).thenReturn(sOk);

        setField(controller, "tUsername", tUsername);
        setField(controller, "tPassw", tPassw);
        setField(controller, "sUsername", sUsername);
        setField(controller, "sPassw", sPassw);
        setField(controller, "nameTag", nameTag);
        setField(controller, "createAccount", createAccount);
    }

    @Test
    void gettUsername_shouldReturnText() {
        when(tUsername.getText()).thenReturn("teacher");

        assertEquals(
                "teacher",
                controller.gettUsername()
        );
    }

    @Test
    void gettPassw_shouldReturnPassword() {
        when(tPassw.getText()).thenReturn("password");

        assertEquals(
                "password",
                controller.gettPassw()
        );
    }

    @Test
    void getSUsername_shouldReturnText() {
        when(sUsername.getText()).thenReturn("student");

        assertEquals(
                "student",
                controller.getSUsername()
        );
    }

    @Test
    void getsPassw_shouldReturnPassword() {
        when(sPassw.getText()).thenReturn("password");

        assertEquals(
                "password",
                controller.getsPassw()
        );
    }

    @Test
    void setCurrentUser_shouldStoreUser() throws Exception {
        User user = mock(User.class);

        controller.setCurrentUser(user);

        Field field =
                signInController.class.getDeclaredField("currentUser");

        field.setAccessible(true);

        assertSame(user, field.get(controller));
    }

    @Test
    void setUserGreeting_shouldSetLabel() {
        controller.setUserGreeting("Matti");

        verify(nameTag).setText("Matti");
    }

    @Test
    void getUserId_shouldReturnCurrentUserId() {
        User user = mock(User.class);

        when(user.getUserId()).thenReturn(55);

        controller.setCurrentUser(user);

        assertEquals(
                55,
                controller.getUserId()
        );
    }

    @Test
    void tLogin_whenSourceIsNotLoginButton_shouldDoNothing()
            throws Exception {

        ActionEvent event = mock(ActionEvent.class);

        when(event.getSource()).thenReturn(new Object());

        controller.tLogin(event);

        verifyNoInteractions(tUsername);
        verifyNoInteractions(tPassw);
    }

    @Test
    void sLogin_whenSourceIsNotLoginButton_shouldDoNothing()
            throws Exception {

        ActionEvent event = mock(ActionEvent.class);

        when(event.getSource()).thenReturn(new Object());

        controller.sLogin(event);

        verifyNoInteractions(sUsername);
        verifyNoInteractions(sPassw);
    }

    @Test
    void tLogin_shouldReadCredentialsWhenSourceIsButton() throws Exception {
        when(tUsername.getText()).thenReturn("teacher");
        when(tPassw.getText()).thenReturn("password");

        try (MockedStatic<UserDao> mockedUserDao = mockStatic(UserDao.class)) {
            mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(true);

            runOnFxThread(() -> {
                try {
                    controller.tLogin(tEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void sLogin_shouldReadCredentialsWhenSourceIsButton() throws Exception {
        when(sUsername.getText()).thenReturn("student");
        when(sPassw.getText()).thenReturn("password");

        try (MockedStatic<UserDao> mockedUserDao = mockStatic(UserDao.class)) {
            mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(true);

            runOnFxThread(() -> {
                try {
                    controller.sLogin(sEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void setCurrentUser_thenGetUserId_shouldWork() {
        User user = mock(User.class);

        when(user.getUserId()).thenReturn(999);

        controller.setCurrentUser(user);

        assertEquals(999, controller.getUserId());
    }

    private static void setField(
            Object target,
            String name,
            Object value
    ) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }


    private void runOnFxThread(Runnable action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable e) {
                error.set(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }
}