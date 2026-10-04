package controller;

import dao.CardDao;
import dao.UserDao;
import entity.Card;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.awt.ScrollPane;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    private GridPane grid;
    private ScrollPane scroll;

    private static boolean javafxStarted = false;


    @BeforeAll
    static void startJavaFx() throws Exception {

        if (javafxStarted) {
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // JavaFX was already started
            latch.countDown();
        }

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX did not start"
        );

        javafxStarted = true;
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
            grid = new GridPane();

            setField(controller, "tUsername", tUsername);
            setField(controller, "tPassw", tPassw);
            setField(controller, "tOk", tOk);

            setField(controller, "sUsername", sUsername);
            setField(controller, "sPassw", sPassw);
            setField(controller, "sOk", sOk);

            setField(controller, "createAccount", createAccount);
            setField(controller, "nameTag", nameTag);
            setField(controller, "grid", grid);

            controller.setCurrentUser(null);
        });
    }


    @Test
    @Order(1)
    void gettUsername() throws Exception {

        runOnFxThread(() ->
                tUsername.setText("teacher")
        );

        assertEquals(
                "teacher",
                controller.gettUsername()
        );
    }


    @Test
    @Order(2)
    void gettPassw() throws Exception {

        runOnFxThread(() ->
                tPassw.setText("password")
        );

        assertEquals(
                "password",
                controller.gettPassw()
        );
    }


    @Test
    @Order(3)
    void getSUsername() throws Exception {

        runOnFxThread(() ->
                sUsername.setText("student")
        );

        assertEquals(
                "student",
                controller.getSUsername()
        );
    }


    @Test
    @Order(4)
    void getsPassw() throws Exception {

        runOnFxThread(() ->
                sPassw.setText("password")
        );

        assertEquals(
                "password",
                controller.getsPassw()
        );
    }


    @Test
    @Order(5)
    void setCurrentUser() throws Exception {

        User user = new User(
                "Matti",
                "matti@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(123);

        controller.setCurrentUser(user);

        Field field =
                signInController.class.getDeclaredField("currentUser");

        field.setAccessible(true);

        assertSame(
                user,
                field.get(null)
        );
    }


    @Test
    @Order(6)
    void setUserGreeting() throws Exception {

        controller.setUserGreeting("Hello Matti");

        assertEquals(
                "Hello Matti",
                nameTag.getText()
        );
    }


    @Test
    @Order(7)
    void getAllCards() throws Exception {

        User user = new User(
                "student",
                "student@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(42);

        Card card1 = new Card(
                "Question 1",
                "Answer 1",
                "Math",
                42
        );

        Card card2 = new Card(
                "Question 2",
                "Answer 2",
                "Physics",
                42
        );

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        try (MockedStatic<CardDao> mocked =
                     mockStatic(CardDao.class)) {

            mocked.when(() ->
                    CardDao.showAllCards(42)
            ).thenReturn(cards);

            controller.getAllCards(user);

            mocked.verify(() ->
                    CardDao.showAllCards(42)
            );

            List<Card> actual =
                    getCards();

            assertEquals(2, actual.size());
            assertSame(card1, actual.get(0));
            assertSame(card2, actual.get(1));
        }
    }


    @Test
    @Order(8)
    void getAllCardsEmpty() throws Exception {

        User user = new User(
                "student",
                "student@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(99);

        try (MockedStatic<CardDao> mocked =
                     mockStatic(CardDao.class)) {

            mocked.when(() ->
                    CardDao.showAllCards(99)
            ).thenReturn(new ArrayList<>());

            controller.getAllCards(user);

            assertTrue(
                    getCards().isEmpty()
            );
        }
    }


    @Test
    @Order(9)
    void addCardsToLibraryEmpty() throws Exception {

        controller.addCardsToLibrary();

        assertEquals(
                0,
                grid.getChildren().size()
        );
    }


    @Test
    @Order(10)
    void addCardsToLibraryMultipleCards() throws Exception {

        for (int i = 0; i < 5; i++) {

            getCards().add(
                    new Card(
                            "Question " + i,
                            "Answer " + i,
                            "Category",
                            1
                    )
            );
        }

        List<AnchorPane> panes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            panes.add(new AnchorPane());
        }

        cardController mockedCardController =
                mock(cardController.class);

        AtomicInteger counter =
                new AtomicInteger(0);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 int index =
                                         counter.getAndIncrement();

                                 when(mock.load())
                                         .thenReturn(panes.get(index));

                                 when(mock.getController())
                                         .thenReturn(
                                                 mockedCardController
                                         );
                             })) {

            controller.addCardsToLibrary();

            assertEquals(
                    5,
                    grid.getChildren().size()
            );

            verify(mockedCardController, times(5))
                    .setCardQuestion(any(Card.class));
        }
    }


    @Test
    @Order(11)
    void addCardsToLibraryStartsNewRow() throws Exception {

        for (int i = 0; i < 4; i++) {

            getCards().add(
                    new Card(
                            "Question " + i,
                            "Answer " + i,
                            "Category",
                            1
                    )
            );
        }

        List<AnchorPane> panes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            panes.add(new AnchorPane());
        }

        cardController mockedCardController =
                mock(cardController.class);

        AtomicInteger counter =
                new AtomicInteger(0);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 int index =
                                         counter.getAndIncrement();

                                 when(mock.load())
                                         .thenReturn(panes.get(index));

                                 when(mock.getController())
                                         .thenReturn(
                                                 mockedCardController
                                         );
                             })) {

            controller.addCardsToLibrary();

            assertEquals(
                    4,
                    grid.getChildren().size()
            );

            assertEquals(
                    0,
                    GridPane.getRowIndex(
                            grid.getChildren().get(0)
                    )
            );

            assertEquals(
                    0,
                    GridPane.getRowIndex(
                            grid.getChildren().get(2)
                    )
            );

            assertEquals(
                    1,
                    GridPane.getRowIndex(
                            grid.getChildren().get(3)
                    )
            );
        }
    }


    @Test
    @Order(12)
    void teacherLoginFailure() throws Exception {

        runOnFxThread(() -> {
            tUsername.setText("teacher");
            tPassw.setText("wrong");
        });

        ActionEvent event =
                mock(ActionEvent.class);

        when(event.getSource())
                .thenReturn(tOk);

        try (
                MockedStatic<UserDao> userDao =
                        mockStatic(UserDao.class);

                MockedConstruction<Alert> alerts =
                        mockConstruction(
                                Alert.class,
                                (mock, context) -> {

                                    when(mock.showAndWait())
                                            .thenReturn(
                                                    Optional.empty()
                                            );
                                })
        ) {

            userDao.when(() ->
                    UserDao.logInUser(any(User.class))
            ).thenReturn(false);

            controller.tLogin(event);

            userDao.verify(() ->
                    UserDao.logInUser(
                            argThat(user ->
                                    user.getUserName()
                                            .equals("teacher")
                                            &&
                                            user.getPassword()
                                                    .equals("wrong")
                                            &&
                                            user.getEmail()
                                                    .equals("teacher@email.fi")
                                            &&
                                            user.getRole()
                                                    == User.Role.teacher
                            )
                    )
            );

            assertEquals(
                    1,
                    alerts.constructed().size()
            );

            verify(
                    alerts.constructed().get(0)
            ).showAndWait();
        }
    }


    @Test
    @Order(13)
    void studentLoginFailure() throws Exception {

        runOnFxThread(() -> {
            sUsername.setText("student");
            sPassw.setText("wrong");
        });

        ActionEvent event =
                mock(ActionEvent.class);

        when(event.getSource())
                .thenReturn(sOk);

        try (
                MockedStatic<UserDao> userDao =
                        mockStatic(UserDao.class);

                MockedConstruction<Alert> alerts =
                        mockConstruction(
                                Alert.class,
                                (mock, context) -> {

                                    when(mock.showAndWait())
                                            .thenReturn(
                                                    Optional.empty()
                                            );
                                })
        ) {

            userDao.when(() ->
                    UserDao.logInUser(any(User.class))
            ).thenReturn(false);

            controller.sLogin(event);

            userDao.verify(() ->
                    UserDao.logInUser(
                            argThat(user ->
                                    user.getUserName()
                                            .equals("student")
                                            &&
                                            user.getPassword()
                                                    .equals("wrong")
                                            &&
                                            user.getEmail()
                                                    .equals("student@email.fi")
                                            &&
                                            user.getRole()
                                                    == User.Role.student
                            )
                    )
            );

            assertEquals(
                    1,
                    alerts.constructed().size()
            );

            verify(
                    alerts.constructed().get(0)
            ).showAndWait();
        }
    }



    @Test
    @Order(14)
    void teacherLoginWrongSource() throws Exception {

        ActionEvent event =
                mock(ActionEvent.class);

        Button otherButton =
                new Button();

        when(event.getSource())
                .thenReturn(otherButton);

        try (MockedStatic<UserDao> userDao =
                     mockStatic(UserDao.class)) {

            controller.tLogin(event);

            userDao.verifyNoInteractions();
        }
    }


    @Test
    @Order(15)
    void studentLoginWrongSource() throws Exception {

        ActionEvent event =
                mock(ActionEvent.class);

        Button otherButton =
                new Button();

        when(event.getSource())
                .thenReturn(otherButton);

        try (MockedStatic<UserDao> userDao =
                     mockStatic(UserDao.class)) {

            controller.sLogin(event);

            userDao.verifyNoInteractions();
        }
    }


    @Test
    @Order(16)
    void switchToCreateAccount() throws Exception {

        ActionEvent event =
                mock(ActionEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        createAccountController accountController =
                mock(createAccountController.class);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);

                                 when(mock.getController())
                                         .thenReturn(
                                                 accountController
                                         );
                             })) {

            controller.switchToCreateAccount(event);

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }


    @Test
    @Order(17)
    void switchToCreateCards() throws Exception {

        User user =
                new User(
                        "Matti",
                        "matti@email.fi",
                        "password",
                        User.Role.student
                );

        controller.setCurrentUser(user);

        MouseEvent event =
                mock(MouseEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        cardController cardControllerMock =
                mock(cardController.class);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);

                                 when(mock.getController())
                                         .thenReturn(
                                                 cardControllerMock
                                         );
                             })) {

            controller.switchToCreateCards(event);

            verify(cardControllerMock)
                    .setUser(user);

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }


    @Test
    @Order(18)
    void switchToLibrary() throws Exception {

        MouseEvent event =
                mock(MouseEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);
                             })) {

            controller.switchToLibrary(event);

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }


    @Test
    @Order(19)
    void switchToCreateQuiz() throws Exception {

        MouseEvent event =
                mock(MouseEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);
                             })) {

            controller.switchToCreateQuiz(event);

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }


    @Test
    @Order(20)
    void switchToStudyMaterials() throws Exception {

        MouseEvent event =
                mock(MouseEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);
                             })) {

            controller.switchToStudyMaterials(event);

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }


    @Test
    @Order(21)
    void switchToProfile() throws Exception {

        User user =
                new User(
                        "Matti",
                        "matti@email.fi",
                        "password",
                        User.Role.student
                );

        controller.setCurrentUser(user);

        MouseEvent event =
                mock(MouseEvent.class);

        Node source =
                mock(Node.class);

        Scene oldScene =
                mock(Scene.class);

        Stage stage =
                mock(Stage.class);

        when(event.getSource())
                .thenReturn(source);

        when(source.getScene())
                .thenReturn(oldScene);

        when(oldScene.getWindow())
                .thenReturn(stage);

        AnchorPane root =
                new AnchorPane();

        signInController profileController =
                mock(signInController.class);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {

                                 when(mock.load())
                                         .thenReturn(root);

                                 when(mock.getController())
                                         .thenReturn(
                                                 profileController
                                         );
                             })) {

            controller.switchToProfile(event);

            verify(profileController)
                    .setUserGreeting("Matti");

            verify(stage)
                    .setScene(any(Scene.class));

            verify(stage)
                    .show();
        }
    }



    @SuppressWarnings("unchecked")
    private List<Card> getCards() throws Exception {

        Field field =
                signInController.class
                        .getDeclaredField("cards");

        field.setAccessible(true);

        return (List<Card>) field.get(controller);
    }


    private void setField(
            Object object,
            String fieldName,
            Object value
    ) {

        try {

            Field field =
                    object.getClass()
                            .getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(object, value);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void runOnFxThread(
            Runnable runnable
    ) throws Exception {

        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch =
                new CountDownLatch(1);

        Platform.runLater(() -> {

            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(
                latch.await(
                        10,
                        TimeUnit.SECONDS
                ),
                "JavaFX operation timed out"
        );
    }
}
