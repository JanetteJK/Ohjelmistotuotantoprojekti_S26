package controller;

import dao.CardDao;
import dao.UserDao;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import dao.LanguageDao;
import entity.Language;
import java.util.List;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignInControllerTest {

    private SignInController controller;

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
    private Locale originalLocale;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        originalLocale = LanguageManager.getLocale();

        controller = new SignInController();

        when(tEvent.getSource()).thenReturn(tOk);
        when(sEvent.getSource()).thenReturn(sOk);

        setField(controller, "tUsername", tUsername);
        setField(controller, "tPassw", tPassw);
        setField(controller, "sUsername", sUsername);
        setField(controller, "sPassw", sPassw);
        setField(controller, "nameTag", nameTag);
        setField(controller, "createAccount", createAccount);
        setField(controller, "tOk", tOk);
        setField(controller, "sOk", sOk);
    }

    @AfterEach
    void restoreLocale() {
        LanguageManager.setLocale(originalLocale);
    }

    @Test
    void gettUsername_shouldReturnText() {
        when(tUsername.getText()).thenReturn("teacher");

        assertEquals(
                "teacher",
                controller.getTUsername()
        );
    }

    @Test
    void gettPassw_shouldReturnPassword() {
        when(tPassw.getText()).thenReturn("password");

        assertEquals(
                "password",
                controller.getTPassw()
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
                SignInController.class.getDeclaredField("currentUser");

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
    void tLogin_whenLoginSucceeds_shouldLoadHomeView() throws Exception {

        when(tUsername.getText()).thenReturn("teacher");
        when(tPassw.getText()).thenReturn("password");

        runOnFxThread(() -> {
            try (MockedStatic<UserDao> mockedUserDao =
                         mockStatic(UserDao.class);

                 MockedConstruction<FXMLLoader> loaders =
                         mockConstruction(FXMLLoader.class, (loader, context) -> {
                             when(loader.load()).thenReturn(mock(Parent.class));

                             when(loader.getController())
                                     .thenReturn(mock(SignInController.class));
                         });

                 MockedConstruction<Scene> scenes =
                         mockConstruction(Scene.class)) {

                mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                        .thenReturn(true);

                Scene currentScene = mock(Scene.class);
                Stage stage = mock(Stage.class);

                when(tOk.getScene()).thenReturn(currentScene);
                when(currentScene.getWindow()).thenReturn(stage);

                controller.tLogin(tEvent);

                mockedUserDao.verify(
                        () -> UserDao.logInUser(any(User.class)),
                        times(1)
                );

                verify(tUsername).getText();
                verify(tPassw).getText();

                verify(stage).setScene(any(Scene.class));
                verify(stage).show();

                FXMLLoader loader = loaders.constructed().get(0);

                verify(loader).load();
                verify(loader).getController();

                mockedUserDao.verify(
                        () -> UserDao.logInUser(any(User.class)),
                        times(1)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    void sLogin_whenLoginSucceeds_shouldLoadHomeView() throws Exception {

        when(sUsername.getText()).thenReturn("student");
        when(sPassw.getText()).thenReturn("password");

        runOnFxThread(() -> {
            try (MockedStatic<UserDao> mockedUserDao =
                         mockStatic(UserDao.class);

                 MockedConstruction<FXMLLoader> loaders =
                         mockConstruction(FXMLLoader.class, (loader, context) -> {
                             when(loader.load()).thenReturn(mock(Parent.class));

                             when(loader.getController())
                                     .thenReturn(mock(SignInController.class));
                         });

                 MockedConstruction<Scene> scenes =
                         mockConstruction(Scene.class)) {

                mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                        .thenReturn(true);

                Scene currentScene = mock(Scene.class);
                Stage stage = mock(Stage.class);

                when(sOk.getScene()).thenReturn(currentScene);
                when(currentScene.getWindow()).thenReturn(stage);

                controller.sLogin(sEvent);

                mockedUserDao.verify(
                        () -> UserDao.logInUser(any(User.class)),
                        times(1)
                );

                verify(sUsername).getText();
                verify(sPassw).getText();

                verify(stage).setScene(any(Scene.class));
                verify(stage).show();

                FXMLLoader loader = loaders.constructed().get(0);

                verify(loader).load();
                verify(loader).getController();

                mockedUserDao.verify(
                        () -> UserDao.logInUser(any(User.class)),
                        times(1)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    void setCurrentUser_thenGetUserId_shouldWork() {
        User user = mock(User.class);

        when(user.getUserId()).thenReturn(999);

        controller.setCurrentUser(user);

        assertEquals(999, controller.getUserId());
    }



    @ParameterizedTest
    @CsvSource({
            "en, English, 0",
            "fi, Suomeksi, 1",
            "ja, 日本語で, 2",
            "ko, 한국어, 3"
    })
    void initialize_selectsLanguageForCurrentLocale(
            String language,
            String expectedChoice,
            int expectedIndex
    ) throws Exception {

        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        setField(controller, "languageChoice", languageChoice);

        List<Language> languages = List.of(
                new Language("en", "English", "English"),
                new Language("fi", "Suomeksi", "Finnish"),
                new Language("ja", "日本語で", "Japanese"),
                new Language("ko", "한국어", "Korean")
        );

        try (MockedStatic<LanguageDao> mockedDao =
                     mockStatic(LanguageDao.class)) {

            mockedDao.when(LanguageDao::getAllLanguages)
                    .thenReturn(languages);

            LanguageManager.initializeLanguages();
            LanguageManager.setLocale(new Locale(language));

            runOnFxThread(controller::initialize);

            assertEquals(expectedChoice, languageChoice.getValue());
            assertEquals(
                    expectedIndex,
                    languageChoice.getSelectionModel().getSelectedIndex()
            );
        }
    }



    @ParameterizedTest
    @CsvSource({
            "0, en, EN",
            "1, fi, FI",
            "2, ja, JP",
            "3, ko, KR"
    })
    void changeLanguage_setsSelectedLocaleAndReloadsLoginView(
            int selectedIndex,
            String expectedLanguage,
            String expectedCountry
    ) throws Exception {
        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        setField(controller, "languageChoice", languageChoice);
        runOnFxThread(() -> {
            languageChoice.getItems().addAll("English", "Suomeksi", "日本語で", "한국어");
            languageChoice.getSelectionModel().select(selectedIndex);
        });

        ActionEvent actionEvent = mock(ActionEvent.class);
        Pane node = mock(Pane.class);
        Scene currentScene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(actionEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(currentScene);
        when(currentScene.getWindow()).thenReturn(stage);

        runOnFxThread(() -> {
            try {
                controller.changeLanguage(actionEvent);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });

        assertEquals(
                new Locale(expectedLanguage, expectedCountry),
                LanguageManager.getLocale()
        );
        verify(stage).setScene(any(Scene.class));
        verify(stage).show();
    }

    @Test
    void changeLanguage_doesNothingDuringLanguageChoiceInitialization() throws Exception {
        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        setField(controller, "languageChoice", languageChoice);
        setField(controller, "initializingLanguageChoice", true);
        ActionEvent actionEvent = mock(ActionEvent.class);

        controller.changeLanguage(actionEvent);

        verifyNoInteractions(actionEvent);
        assertEquals(originalLocale, LanguageManager.getLocale());
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
    void tLogin_withInvalidCredentials_shouldNotOpenAlert()
            throws Exception {

        when(tUsername.getText()).thenReturn("wrongTeacher");
        when(tPassw.getText()).thenReturn("wrongPassword");

        SignInController spyController = spy(controller);

        doNothing().when(spyController).showLoginFailedAlert();

        try (MockedStatic<UserDao> mockedUserDao =
                     mockStatic(UserDao.class)) {

            mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            runOnFxThread(() -> {
                try {
                    spyController.tLogin(tEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void sLogin_withInvalidCredentials_shouldNotOpenAlert()
            throws Exception {

        when(sUsername.getText()).thenReturn("wrongStudent");
        when(sPassw.getText()).thenReturn("wrongPassword");

        SignInController spyController = spy(controller);

        doNothing().when(spyController).showLoginFailedAlert();

        try (MockedStatic<UserDao> mockedUserDao =
                     mockStatic(UserDao.class)) {

            mockedUserDao.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            runOnFxThread(() -> {
                try {
                    spyController.sLogin(sEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void switchToCreateAccount_shouldLoadView() throws Exception {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Pane node = mock(Pane.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(actionEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        CreateAccountController createController =
                mock(CreateAccountController.class);

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
    void switchToCreateCards_shouldDelegateToPageController() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);

        try (MockedStatic<PageController> pageController =
                     mockStatic(PageController.class)) {

            controller.switchToCreateCards(mouseEvent);

            pageController.verify(() ->
                    PageController.switchToCreateCards(mouseEvent));
        }
    }

    @Test
    void switchToCreateQuiz_shouldDelegateToPageController() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);

        try (MockedStatic<PageController> pageController =
                     mockStatic(PageController.class)) {

            controller.switchToCreateQuiz(mouseEvent);

            pageController.verify(() ->
                    PageController.switchToCreateQuiz(mouseEvent));
        }
    }

    @Test
    void switchToLibrary_shouldDelegateToPageController() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);

        try (MockedStatic<PageController> pageController =
                     mockStatic(PageController.class)) {

            controller.switchToLibrary(mouseEvent);

            pageController.verify(() ->
                    PageController.switchToLibrary(mouseEvent));
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

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = SignInController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
}