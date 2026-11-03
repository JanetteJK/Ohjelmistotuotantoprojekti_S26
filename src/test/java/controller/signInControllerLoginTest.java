package controller;

import dao.UserDao;
import entity.User;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class signInControllerLoginTest {

    private signInController controller;
    private TextField teacherUsername;
    private PasswordField teacherPassword;
    private TextField studentUsername;
    private PasswordField studentPassword;
    private Button teacherLoginButton;
    private Button studentLoginButton;
    private ActionEvent teacherLoginEvent;
    private ActionEvent studentLoginEvent;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new signInController();

        teacherUsername = mock(TextField.class);
        teacherPassword = mock(PasswordField.class);
        studentUsername = mock(TextField.class);
        studentPassword = mock(PasswordField.class);
        teacherLoginButton = mock(Button.class);
        studentLoginButton = mock(Button.class);
        teacherLoginEvent = createLoginEvent(teacherLoginButton);
        studentLoginEvent = createLoginEvent(studentLoginButton);

        setField(controller, "tUsername", teacherUsername);
        setField(controller, "tPassw", teacherPassword);
        setField(controller, "sUsername", studentUsername);
        setField(controller, "sPassw", studentPassword);
        setField(controller, "tOk", teacherLoginButton);
        setField(controller, "sOk", studentLoginButton);
    }

    @Test
    void teacherLogin_whenCredentialsAreValid_shouldNavigateToHome() throws Exception {
        when(teacherUsername.getText()).thenReturn("teacher");
        when(teacherPassword.getText()).thenReturn("secret");
        Stage stage = mockStageFor(teacherLoginEvent);
        Parent home = mock(Parent.class);
        signInController homeController = mock(signInController.class);

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
             MockedConstruction<FXMLLoader> loaders = mockConstruction(
                     FXMLLoader.class,
                     (loader, context) -> {
                         when(loader.load()).thenReturn(home);
                         when(loader.getController()).thenReturn(homeController);
                     });
             MockedConstruction<Scene> scenes = mockConstruction(Scene.class)) {
            userDao.when(() -> UserDao.logInUser(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                assertEquals("teacher", user.getUserName());
                assertEquals("secret", user.getPassword());
                assertEquals("teacher@email.fi", user.getEmail());
                assertEquals(User.Role.teacher, user.getRole());
                return true;
            });

            runOnFxThread(() -> assertDoesNotThrow(
                    () -> controller.tLogin(teacherLoginEvent)));

            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
            verify(homeController).setUserGreeting("teacher");
            verify(homeController).setCurrentUser(any(User.class));
            userDao.verify(() -> UserDao.logInUser(any(User.class)), times(2));
        }
    }

    @Test
    void studentLogin_whenCredentialsAreValid_shouldNavigateToHome() throws Exception {
        when(studentUsername.getText()).thenReturn("student");
        when(studentPassword.getText()).thenReturn("secret");
        Stage stage = mockStageFor(studentLoginEvent);
        Parent home = mock(Parent.class);
        signInController homeController = mock(signInController.class);

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
             MockedConstruction<FXMLLoader> loaders = mockConstruction(
                     FXMLLoader.class,
                     (loader, context) -> {
                         when(loader.load()).thenReturn(home);
                         when(loader.getController()).thenReturn(homeController);
                     });
             MockedConstruction<Scene> scenes = mockConstruction(Scene.class)) {
            userDao.when(() -> UserDao.logInUser(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                assertEquals("student", user.getUserName());
                assertEquals("secret", user.getPassword());
                assertEquals("student@email.fi", user.getEmail());
                assertEquals(User.Role.student, user.getRole());
                return true;
            });

            runOnFxThread(() -> assertDoesNotThrow(
                    () -> controller.sLogin(studentLoginEvent)));

            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
            verify(homeController).setUserGreeting("student");
            verify(homeController).setCurrentUser(any(User.class));
            userDao.verify(() -> UserDao.logInUser(any(User.class)), times(2));
        }
    }

    @Test
    void teacherLogin_whenCredentialsAreInvalid_shouldShowWarningWithoutNavigation()
            throws Exception {
        when(teacherUsername.getText()).thenReturn("teacher");
        when(teacherPassword.getText()).thenReturn("wrong");

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
             MockedConstruction<Alert> alerts = mockConstruction(Alert.class)) {
            userDao.when(() -> UserDao.logInUser(any(User.class))).thenReturn(false);

            runOnFxThread(() -> assertDoesNotThrow(
                    () -> controller.tLogin(teacherLoginEvent)));

            Alert alert = alerts.constructed().get(0);
            verify(alert).setTitle(LanguageManager.getBundle().getString("ELEM_login_failed"));
            verify(alert).setHeaderText(LanguageManager.getBundle().getString("ELEM_login_failed"));
            verify(alert).setContentText(LanguageManager.getBundle().getString("ELEM_wrong"));
            verify(alert).showAndWait();
        }
    }

    @Test
    void studentLogin_whenCredentialsAreInvalid_shouldShowWarningWithoutNavigation()
            throws Exception {
        when(studentUsername.getText()).thenReturn("student");
        when(studentPassword.getText()).thenReturn("wrong");

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
             MockedConstruction<Alert> alerts = mockConstruction(Alert.class)) {
            userDao.when(() -> UserDao.logInUser(any(User.class))).thenReturn(false);

            runOnFxThread(() -> assertDoesNotThrow(
                    () -> controller.sLogin(studentLoginEvent)));

            Alert alert = alerts.constructed().get(0);
            verify(alert).setTitle(LanguageManager.getBundle().getString("ELEM_login_failed"));
            verify(alert).setHeaderText(LanguageManager.getBundle().getString("ELEM_login_failed"));
            verify(alert).setContentText(LanguageManager.getBundle().getString("ELEM_wrong"));
            verify(alert).showAndWait();
        }
    }

    private ActionEvent createLoginEvent(Button loginButton) {
        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(loginButton);
        return event;
    }

    private Stage mockStageFor(ActionEvent event) {
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);
        when(event.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);
        return stage;
    }

    private static void setField(Object target, String name, Object value)
            throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void runOnFxThread(ThrowingAction action) throws Exception {
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
        latch.await();

        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    @FunctionalInterface
    private interface ThrowingAction {
        void run() throws Exception;
    }
}
