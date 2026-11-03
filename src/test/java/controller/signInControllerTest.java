package controller;

import dao.UserDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
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

    @Test
    void switchToCreateAccount_shouldLoadView() throws Exception {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(actionEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        createAccountController createController =
                mock(createAccountController.class);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(createController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToCreateAccount(actionEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToCreateCards_shouldLoadViewAndSetUser() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        ccController cardController = mock(ccController.class);
        User user = mock(User.class);

        controller.setCurrentUser(user);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(cardController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToCreateCards(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(cardController).setUser(user);
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToLibrary_shouldLoadViewAndGetCards() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        libraryController libraryController =
                mock(libraryController.class);
        User user = mock(User.class);

        controller.setCurrentUser(user);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(libraryController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToLibrary(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(libraryController).setUser(user);
            verify(libraryController).getAllCards();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToCreateQuiz_shouldLoadView() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToCreateQuiz(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToStudyMaterials_shouldLoadView() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToStudyMaterials(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToProfile_shouldLoadViewAndSetGreeting() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        signInController profileController =
                mock(signInController.class);

        User user = mock(User.class);
        when(user.getUserName()).thenReturn("Matti");

        controller.setCurrentUser(user);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(profileController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToProfile(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(profileController).setUserGreeting("Matti");
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    private Stage createTestStage() {
        Stage stage = new Stage();
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        return stage;
    }

    private Button createButtonOnStage(Stage stage) {
        Button button = new Button();
        stage.getScene().setRoot(button);
        return button;
    }

    private void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}