package controller;

import dao.UserDao;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SignInControllerTest {

    private signInController controller;

    private TextField tUsername;
    private PasswordField tPassw;
    private Button tOk;

    private TextField sUsername;
    private PasswordField sPassw;
    private Button sOk;

    private Hyperlink createAccount;
    private Label nameTag;

    @BeforeAll
    static void startJavaFx() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            latch.countDown();
        }

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX did not start within the timeout"
        );
    }

    @BeforeEach
    void setUp() throws Exception {
        runOnFxThread(() -> {
            controller = new signInController();

            tUsername = new TextField();
            tPassw = new PasswordField();
            tOk = new Button();

            sUsername = new TextField();
            sPassw = new PasswordField();
            sOk = new Button();

            createAccount = new Hyperlink();
            nameTag = new Label();

            setField(controller, "tUsername", tUsername);
            setField(controller, "tPassw", tPassw);
            setField(controller, "tOk", tOk);

            setField(controller, "sUsername", sUsername);
            setField(controller, "sPassw", sPassw);
            setField(controller, "sOk", sOk);

            setField(controller, "createAccount", createAccount);
            setField(controller, "nameTag", nameTag);
        });
    }

    @Test
    void gettUsernameReturnsTeacherUsernameText() throws Exception {
        runOnFxThread(() -> tUsername.setText("teacherUser"));

        assertEquals("teacherUser", controller.gettUsername());
    }

    @Test
    void gettPasswReturnsTeacherPasswordText() throws Exception {
        runOnFxThread(() -> tPassw.setText("teacherPassword"));

        assertEquals("teacherPassword", controller.gettPassw());
    }

    @Test
    void getSUsernameReturnsStudentUsernameText() throws Exception {
        runOnFxThread(() -> sUsername.setText("studentUser"));

        assertEquals("studentUser", controller.getSUsername());
    }

    @Test
    void getsPasswReturnsStudentPasswordText() throws Exception {
        runOnFxThread(() -> sPassw.setText("studentPassword"));

        assertEquals("studentPassword", controller.getsPassw());
    }

    @Test
    void setCurrentUserStoresGivenUser() throws Exception {
        User user = new User(
                "Matti",
                "matti@example.test",
                "password",
                User.Role.student
        );

        controller.setCurrentUser(user);

        Field field = signInController.class.getDeclaredField("currentUser");
        field.setAccessible(true);

        assertSame(user, field.get(controller));
    }

    @Test
    void setUserGreetingUpdatesNameTagText() throws Exception {
        runOnFxThread(() -> controller.setUserGreeting("Welcome Matti"));

        assertEquals("Welcome Matti", nameTag.getText());
    }

    @Test
    void getUserIdReturnsCurrentUserId() {
        User user = new User(
                "Matti",
                "matti@example.test",
                "password",
                User.Role.student
        );
        user.setUserId(123);

        controller.setCurrentUser(user);

        assertEquals(123, controller.getUserId());
    }

    @Test
    void tLoginWithWrongSourceDoesNothing() throws Exception {
        ActionEvent event = mock(ActionEvent.class);
        Button wrongButton = new Button();

        when(event.getSource()).thenReturn(wrongButton);

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class)) {
            controller.tLogin(event);

            userDao.verifyNoInteractions();
        }
    }

    @Test
    void sLoginWithWrongSourceDoesNothing() throws Exception {
        ActionEvent event = mock(ActionEvent.class);
        Button wrongButton = new Button();

        when(event.getSource()).thenReturn(wrongButton);

        try (MockedStatic<UserDao> userDao = mockStatic(UserDao.class)) {
            controller.sLogin(event);

            userDao.verifyNoInteractions();
        }
    }

    @Test
    void tLoginFailureShowsWarningAlert() throws Exception {
        runOnFxThread(() -> {
            tUsername.setText("teacher");
            tPassw.setText("wrongPassword");
        });

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(tOk);

        try (
                MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
                MockedConstruction<Alert> alerts = mockConstruction(
                        Alert.class,
                        (mock, context) -> when(mock.showAndWait())
                                .thenReturn(Optional.empty())
                )
        ) {
            userDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            runOnFxThread(() -> {
                try {
                    controller.tLogin(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            userDao.verify(() -> UserDao.logInUser(argThat(user ->
                    user.getUserName().equals("teacher")
                            && user.getPassword().equals("wrongPassword")
                            && user.getEmail().equals("teacher@email.fi")
                            && user.getRole() == User.Role.teacher
            )));

            assertEquals(1, alerts.constructed().size());
            verify(alerts.constructed().get(0)).setTitle("Login failed");
            verify(alerts.constructed().get(0)).setHeaderText("Login failed!");
            verify(alerts.constructed().get(0))
                    .setContentText("Wrong password or username.");
            verify(alerts.constructed().get(0)).showAndWait();
        }
    }

    @Test
    void sLoginFailureShowsWarningAlert() throws Exception {
        runOnFxThread(() -> {
            sUsername.setText("student");
            sPassw.setText("wrongPassword");
        });

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(sOk);

        try (
                MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
                MockedConstruction<Alert> alerts = mockConstruction(
                        Alert.class,
                        (mock, context) -> when(mock.showAndWait())
                                .thenReturn(Optional.empty())
                )
        ) {
            userDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            runOnFxThread(() -> {
                try {
                    controller.sLogin(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            userDao.verify(() -> UserDao.logInUser(argThat(user ->
                    user.getUserName().equals("student")
                            && user.getPassword().equals("wrongPassword")
                            && user.getEmail().equals("student@email.fi")
                            && user.getRole() == User.Role.student
            )));

            assertEquals(1, alerts.constructed().size());
            verify(alerts.constructed().get(0)).setTitle("Login failed");
            verify(alerts.constructed().get(0)).setHeaderText("Login failed!");
            verify(alerts.constructed().get(0))
                    .setContentText("Wrong password or username.");
            verify(alerts.constructed().get(0)).showAndWait();
        }
    }

    @Test
    void tLoginSuccessSetsCurrentUserAndSwitchesScene() throws Exception {
        runOnFxThread(() -> {
            tUsername.setText("teacher");
            tPassw.setText("correctPassword");
        });

        Stage stage = new Stage();
        Scene oldScene = new Scene(new AnchorPane(tOk));
        stage.setScene(oldScene);

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(tOk);

        Parent loadedRoot = new AnchorPane();
        signInController loadedController = mock(signInController.class);

        try (
                MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
                MockedConstruction<FXMLLoader> ignored = mockConstruction(
                        FXMLLoader.class,
                        (mock, context) -> {
                            when(mock.load()).thenReturn(loadedRoot);
                            when(mock.getController()).thenReturn(loadedController);
                        }
                )
        ) {
            userDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(true);

            runOnFxThread(() -> {
                try {
                    controller.tLogin(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            userDao.verify(() -> UserDao.logInUser(argThat(user ->
                    user.getUserName().equals("teacher")
                            && user.getPassword().equals("correctPassword")
                            && user.getEmail().equals("teacher@email.fi")
                            && user.getRole() == User.Role.teacher
            )));

            Field currentUserField =
                    signInController.class.getDeclaredField("currentUser");
            currentUserField.setAccessible(true);

            User currentUser = (User) currentUserField.get(controller);

            assertEquals("teacher", currentUser.getUserName());
            assertEquals("teacher@email.fi", currentUser.getEmail());
            assertEquals(User.Role.teacher, currentUser.getRole());

            verify(loadedController).setUserGreeting("teacher");
            verify(loadedController).setCurrentUser(currentUser);

            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }

    @Test
    void sLoginSuccessSetsCurrentUserAndSwitchesScene() throws Exception {
        runOnFxThread(() -> {
            sUsername.setText("student");
            sPassw.setText("correctPassword");
        });

        Stage stage = new Stage();
        Scene oldScene = new Scene(new AnchorPane(sOk));
        stage.setScene(oldScene);

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(sOk);

        Parent loadedRoot = new AnchorPane();
        signInController loadedController = mock(signInController.class);

        try (
                MockedStatic<UserDao> userDao = mockStatic(UserDao.class);
                MockedConstruction<FXMLLoader> ignored = mockConstruction(
                        FXMLLoader.class,
                        (mock, context) -> {
                            when(mock.load()).thenReturn(loadedRoot);
                            when(mock.getController()).thenReturn(loadedController);
                        }
                )
        ) {
            userDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(true);

            runOnFxThread(() -> {
                try {
                    controller.sLogin(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            userDao.verify(() -> UserDao.logInUser(argThat(user ->
                    user.getUserName().equals("student")
                            && user.getPassword().equals("correctPassword")
                            && user.getEmail().equals("student@email.fi")
                            && user.getRole() == User.Role.student
            )));

            Field currentUserField =
                    signInController.class.getDeclaredField("currentUser");
            currentUserField.setAccessible(true);

            User currentUser = (User) currentUserField.get(controller);

            assertEquals("student", currentUser.getUserName());
            assertEquals("student@email.fi", currentUser.getEmail());
            assertEquals(User.Role.student, currentUser.getRole());

            verify(loadedController).setUserGreeting("student");
            verify(loadedController).setCurrentUser(currentUser);

            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }

    @Test
    void switchToCreateAccountLoadsCreateAccountScene() throws Exception {
        Stage stage = new Stage();
        Button source = new Button();
        stage.setScene(new Scene(new AnchorPane(source)));

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(source);

        Parent loadedRoot = new AnchorPane();
        createAccountController createAccountControllerMock =
                mock(createAccountController.class);

        try (MockedConstruction<FXMLLoader> ignored = mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    when(mock.load()).thenReturn(loadedRoot);
                    when(mock.getController()).thenReturn(createAccountControllerMock);
                }
        )) {
            runOnFxThread(() -> {
                try {
                    controller.switchToCreateAccount(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }

    @Test
    void switchToCreateCardsPassesCurrentUserAndLoadsCreateCardScene()
            throws Exception {
        User user = new User(
                "Student",
                "student@example.test",
                "password",
                User.Role.student
        );
        controller.setCurrentUser(user);

        Stage stage = new Stage();
        Button source = new Button();
        stage.setScene(new Scene(new AnchorPane(source)));

        MouseEvent event = mock(MouseEvent.class);
        when(event.getSource()).thenReturn(source);

        Parent loadedRoot = new AnchorPane();
        ccController cardControllerMock = mock(ccController.class);

        try (MockedConstruction<FXMLLoader> ignored = mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    when(mock.load()).thenReturn(loadedRoot);
                    when(mock.getController()).thenReturn(cardControllerMock);
                }
        )) {
            runOnFxThread(() -> {
                try {
                    controller.switchToCreateCards(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            verify(cardControllerMock).setUser(user);
            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }

    @Test
    void switchToLibraryPassesCurrentUserLoadsCardsAndSwitchesScene()
            throws Exception {
        User user = new User(
                "Student",
                "student@example.test",
                "password",
                User.Role.student
        );
        controller.setCurrentUser(user);

        Stage stage = new Stage();
        Button source = new Button();
        stage.setScene(new Scene(new AnchorPane(source)));

        MouseEvent event = mock(MouseEvent.class);
        when(event.getSource()).thenReturn(source);

        Parent loadedRoot = new AnchorPane();
        libraryController libraryControllerMock = mock(libraryController.class);

        try (MockedConstruction<FXMLLoader> ignored = mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    when(mock.load()).thenReturn(loadedRoot);
                    when(mock.getController()).thenReturn(libraryControllerMock);
                }
        )) {
            runOnFxThread(() -> {
                try {
                    controller.switchToLibrary(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            verify(libraryControllerMock).setUser(user);
            verify(libraryControllerMock).getAllCards();
            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }


    @Test
    void switchToProfileSetsGreetingAndSwitchesScene() throws Exception {
        User user = new User(
                "Matti",
                "matti@example.test",
                "password",
                User.Role.student
        );
        controller.setCurrentUser(user);

        Stage stage = new Stage();
        Button source = new Button();
        stage.setScene(new Scene(new AnchorPane(source)));

        MouseEvent event = mock(MouseEvent.class);
        when(event.getSource()).thenReturn(source);

        Parent loadedRoot = new AnchorPane();
        signInController loadedController = mock(signInController.class);

        try (MockedConstruction<FXMLLoader> ignored = mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    when(mock.load()).thenReturn(loadedRoot);
                    when(mock.getController()).thenReturn(loadedController);
                }
        )) {
            runOnFxThread(() -> {
                try {
                    controller.switchToProfile(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            verify(loadedController).setUserGreeting("Matti");
            assertSame(loadedRoot, stage.getScene().getRoot());
        }
    }

    private static void setField(
            Object object,
            String fieldName,
            Object value
    ) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void runOnFxThread(Runnable runnable) throws Exception {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        RuntimeException[] thrown = new RuntimeException[1];

        Platform.runLater(() -> {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                thrown[0] = e;
            } finally {
                latch.countDown();
            }
        });

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX operation timed out"
        );

        if (thrown[0] != null) {
            throw thrown[0];
        }
    }
}