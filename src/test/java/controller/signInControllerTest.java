package controller;

import dao.CardDao;
import dao.UserDao;
import entity.Card;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
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

    private static boolean javaFxStarted = false;

    @BeforeAll
    static void startJavaFx() throws Exception {
        if (!javaFxStarted) {
            CountDownLatch latch = new CountDownLatch(1);

            try {
                Platform.startup(latch::countDown);
            } catch (IllegalStateException e) {
                // JavaFX was already started
                latch.countDown();
            }

            assertTrue(
                    latch.await(10, TimeUnit.SECONDS),
                    "JavaFX toolkit did not start"
            );

            javaFxStarted = true;
        }
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
            scroll = new ScrollPane();

            setField(controller, "tUsername", tUsername);
            setField(controller, "tPassw", tPassw);
            setField(controller, "tOk", tOk);

            setField(controller, "sUsername", sUsername);
            setField(controller, "sPassw", sPassw);
            setField(controller, "sOk", sOk);

            setField(controller, "createAccount", createAccount);
            setField(controller, "nameTag", nameTag);
            setField(controller, "grid", grid);
            setField(controller, "scroll", scroll);

            controller.setCurrentUser(null);
        });
    }


    @Test
    @Order(1)
    @DisplayName("gettUsername returns username")
    void gettUsername() throws Exception {
        runOnFxThread(() -> tUsername.setText("teacher"));

        assertEquals("teacher", controller.gettUsername());
    }

    @Test
    @Order(2)
    @DisplayName("gettPassw returns password")
    void gettPassw() throws Exception {
        runOnFxThread(() -> tPassw.setText("password123"));

        assertEquals("password123", controller.gettPassw());
    }

    @Test
    @Order(3)
    @DisplayName("getSUsername returns student username")
    void getSUsername() throws Exception {
        runOnFxThread(() -> sUsername.setText("student"));

        assertEquals("student", controller.getSUsername());
    }

    @Test
    @Order(4)
    @DisplayName("getsPassw returns student password")
    void getsPassw() throws Exception {
        runOnFxThread(() -> sPassw.setText("studentPassword"));

        assertEquals(
                "studentPassword",
                controller.getsPassw()
        );
    }


    @Test
    @Order(5)
    @DisplayName("setCurrentUser sets current user")
    void setCurrentUser() throws Exception {

        User user = new User(
                "Matti",
                "matti@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(10);

        controller.setCurrentUser(user);

        Field field = signInController.class
                .getDeclaredField("currentUser");

        field.setAccessible(true);

        assertSame(
                user,
                field.get(null)
        );
    }


    @Test
    @Order(6)
    @DisplayName("setUserGreeting sets label")
    void setUserGreeting() throws Exception {

        controller.setUserGreeting("Matti");

        assertEquals(
                "Matti",
                getLabelText()
        );
    }

    @Test
    @Order(7)
    @DisplayName("setUserGreeting can replace existing value")
    void setUserGreetingReplace() throws Exception {

        controller.setUserGreeting("Matti");
        controller.setUserGreeting("Teemu");

        assertEquals(
                "Teemu",
                getLabelText()
        );
    }


    @Test
    @Order(8)
    @DisplayName("getAllCards gets cards for correct user")
    void getAllCards() throws Exception {

        User user = new User(
                "student",
                "student@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(123);

        Card card1 = new Card(
                "Question 1",
                "Answer 1",
                "Math",
                123
        );

        Card card2 = new Card(
                "Question 2",
                "Answer 2",
                "Physics",
                123
        );

        ArrayList<Card> result = new ArrayList<>();
        result.add(card1);
        result.add(card2);

        try (MockedStatic<CardDao> mocked = mockStatic(CardDao.class)) {

            mocked.when(() -> CardDao.showAllCards(123))
                    .thenReturn(result);

            controller.getAllCards(user);

            mocked.verify(
                    () -> CardDao.showAllCards(123)
            );

            List<Card> actual = getCards();

            assertEquals(2, actual.size());
            assertSame(card1, actual.get(0));
            assertSame(card2, actual.get(1));
        }
    }

    @Test
    @Order(9)
    @DisplayName("getAllCards handles empty result")
    void getAllCardsEmpty() throws Exception {

        User user = new User(
                "student",
                "student@email.fi",
                "password",
                User.Role.student
        );

        user.setUserId(50);

        try (MockedStatic<CardDao> mocked = mockStatic(CardDao.class)) {

            mocked.when(() -> CardDao.showAllCards(50))
                    .thenReturn(new ArrayList<>());

            controller.getAllCards(user);

            assertTrue(getCards().isEmpty());
        }
    }


    @Test
    @Order(10)
    @DisplayName("addCardsToLibrary with no cards does nothing")
    void addCardsToLibraryEmpty() throws Exception {

        controller.addCardsToLibrary();

        assertEquals(
                0,
                grid.getChildren().size()
        );
    }

    @Test
    @Order(11)
    @DisplayName("addCardsToLibrary adds one card")
    void addCardsToLibraryOneCard() throws Exception {

        Card card = new Card(
                "What is Java?",
                "Programming language",
                "Programming",
                1
        );

        getCards().add(card);

        controller.addCardsToLibrary();

        assertEquals(
                1,
                grid.getChildren().size()
        );
    }

    @Test
    @Order(12)
    @DisplayName("addCardsToLibrary adds multiple cards")
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

        controller.addCardsToLibrary();

        assertEquals(
                5,
                grid.getChildren().size()
        );
    }

    @Test
    @Order(13)
    @DisplayName("addCardsToLibrary starts new row after third column")
    void addCardsToLibraryCreatesNewRow() throws Exception {

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

        controller.addCardsToLibrary();

        assertEquals(
                4,
                grid.getChildren().size()
        );


        assertEquals(
                1,
                GridPane.getRowIndex(
                        grid.getChildren().get(3)
                )
        );
    }



    @Test
    @Order(14)
    @DisplayName("teacher login sends correct user to DAO")
    void teacherLoginCreatesCorrectUser() throws Exception {

        runOnFxThread(() -> {
            tUsername.setText("teacher123");
            tPassw.setText("secret");
        });

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(tOk);

        try (MockedStatic<UserDao> mocked =
                     mockStatic(UserDao.class)) {

            mocked.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            /*
             * We only want to test that the correct User object
             * is constructed and passed to UserDao.
             */
            assertDoesNotThrow(() -> {
                try {
                    controller.tLogin(event);
                } catch (Exception e) {
                    // Alert/FXML related JavaFX behavior is outside
                    // the scope of this unit test.
                }
            });

            mocked.verify(
                    () -> UserDao.logInUser(argThat(user ->
                            user.getUserName().equals("teacher123")
                                    && user.getPassword().equals("secret")
                                    && user.getEmail().equals("teacher@email.fi")
                                    && user.getRole() == User.Role.teacher
                    ))
            );
        }
    }


    @Test
    @Order(15)
    @DisplayName("student login sends correct user to DAO")
    void studentLoginCreatesCorrectUser() throws Exception {

        runOnFxThread(() -> {
            sUsername.setText("student123");
            sPassw.setText("secret");
        });

        ActionEvent event = mock(ActionEvent.class);
        when(event.getSource()).thenReturn(sOk);

        try (MockedStatic<UserDao> mocked =
                     mockStatic(UserDao.class)) {

            mocked.when(() -> UserDao.logInUser(any(User.class)))
                    .thenReturn(false);

            assertDoesNotThrow(() -> {
                try {
                    controller.sLogin(event);
                } catch (Exception e) {
                    // Alert/FXML related JavaFX behavior is outside
                    // the scope of this unit test.
                }
            });

            mocked.verify(
                    () -> UserDao.logInUser(argThat(user ->
                            user.getUserName().equals("student123")
                                    && user.getPassword().equals("secret")
                                    && user.getEmail().equals("student@email.fi")
                                    && user.getRole() == User.Role.student
                    ))
            );
        }
    }



    @Test
    @Order(16)
    @DisplayName("teacher login ignores unrelated event source")
    void teacherLoginIgnoresWrongSource() throws Exception {

        Button anotherButton = new Button();

        ActionEvent event = mock(ActionEvent.class);

        when(event.getSource())
                .thenReturn(anotherButton);

        try (MockedStatic<UserDao> mocked = mockStatic(UserDao.class)) {

            controller.tLogin(event);

            mocked.verifyNoInteractions();
        }
    }

    @Test
    @Order(17)
    @DisplayName("student login ignores unrelated event source")
    void studentLoginIgnoresWrongSource() throws Exception {

        Button anotherButton = new Button();

        ActionEvent event = mock(ActionEvent.class);

        when(event.getSource())
                .thenReturn(anotherButton);

        try (MockedStatic<UserDao> mocked = mockStatic(UserDao.class)) {

            controller.sLogin(event);

            mocked.verifyNoInteractions();
        }
    }




    @SuppressWarnings("unchecked")
    private List<Card> getCards() throws Exception {

        Field field = signInController.class
                .getDeclaredField("cards");

        field.setAccessible(true);

        return (List<Card>) field.get(controller);
    }

    private String getLabelText() throws Exception {

        final String[] result = new String[1];

        runOnFxThread(() ->
                result[0] = nameTag.getText()
        );

        return result[0];
    }

    private static void setField(
            Object object,
            String fieldName,
            Object value
    ) {

        try {
            Field field = object
                    .getClass()
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

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX operation timed out"
        );
    }
}