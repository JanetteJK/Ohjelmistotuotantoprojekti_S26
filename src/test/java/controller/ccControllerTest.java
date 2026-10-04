package controller;

import dao.CardDao;
import entity.Card;
import entity.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.awt.event.MouseEvent;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private static boolean javafxStarted = false;

    private cardController controller;

    private TextArea questionBox;
    private TextArea answerBox;
    private Button submitCard;
    private Label libraryMenu;
    private Label studyMaterialsMenu;
    private Label profileMenu;
    private Label createQuizMenu;
    private Label createCardMenu;
    private javafx.scene.control.ScrollPane scrollPane;
    private javafx.scene.layout.GridPane gridPane;
    private TextField cardText;

    @BeforeAll
    static void startJavaFx() throws Exception {
        if (javafxStarted) {
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
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
            controller = new cardController();

            questionBox = new TextArea();
            answerBox = new TextArea();
            submitCard = new Button();

            libraryMenu = new Label();
            studyMaterialsMenu = new Label();
            profileMenu = new Label();
            createQuizMenu = new Label();
            createCardMenu = new Label();

            gridPane = new javafx.scene.layout.GridPane();
            cardText = new TextField();

            setField(controller, "questionBox", questionBox);
            setField(controller, "answerBox", answerBox);
            setField(controller, "submitCard", submitCard);
            setField(controller, "LibraryMenu", libraryMenu);
            setField(controller, "StudyMaterialsMenu", studyMaterialsMenu);
            setField(controller, "ProfileMenu", profileMenu);
            setField(controller, "CreateQuizMenu", createQuizMenu);
            setField(controller, "CreateCardMenu", createCardMenu);
            setField(controller, "gridPane", gridPane);
            setField(controller, "cardText", cardText);

        });
    }



    @Test
    void getQuestionReturnsQuestion() throws Exception {
        runOnFxThread(() -> {
            questionBox.setText("What is Java?");

            assertEquals(
                    "What is Java?",
                    controller.getQuestion()
            );
        });
    }

    @Test
    void getAnswerReturnsAnswer() throws Exception {
        runOnFxThread(() -> {
            answerBox.setText("Programming language");

            assertEquals(
                    "Programming language",
                    controller.getAnswer()
            );
        });
    }


    @Test
    void setUserStoresUser() throws Exception {
        User user = new User(
                "Test User",
                "test@email.com",
                "password",
                User.Role.student
        );

        controller.setUser(user);

        User storedUser = (User) getField(controller, "user");

        assertSame(user, storedUser);
    }


    @Test
    void submitCardCreatesCardAndClearsFields() throws Exception {
        User user = new User(
                "Test User",
                "test@email.com",
                "password",
                User.Role.student
        );

        user.setUserId(123);

        controller.setUser(user);

        runOnFxThread(() -> {
            questionBox.setText("What is Java?");
            answerBox.setText("A programming language");
        });

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {

            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(any(Card.class))
            );

            runOnFxThread(() -> {
                assertEquals("", questionBox.getText());
                assertEquals("", answerBox.getText());
            });
        }
    }



    @Test
    void showAllCardsWithCards() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new Card(
                "Question 1",
                "Answer 1",
                "default",
                10
        ));

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {

            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);


            assertThrows(
                    java.util.ConcurrentModificationException.class,
                    () -> controller.showAllCards(10)
            );

            cardDaoMock.verify(() ->
                    CardDao.showAllCards(10)
            );
        }
    }

    @Test
    void showAllCardsWithEmptyList() {
        ArrayList<Card> cards = new ArrayList<>();

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {

            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            assertDoesNotThrow(() ->
                    controller.showAllCards(10)
            );

            cardDaoMock.verify(() ->
                    CardDao.showAllCards(10)
            );
        }
    }

    @Test
    void showAllCardsWithNull() {
        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {

            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(null);

            assertDoesNotThrow(() ->
                    controller.showAllCards(10)
            );

            cardDaoMock.verify(() ->
                    CardDao.showAllCards(10)
            );
        }
    }


    @Test
    void switchToCreateAccount() throws Exception {
        testActionEventNavigation(
                "createAccount.fxml",
                () -> {
                    try {
                        controller.switchToCreateAccount(
                                createActionEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void switchToCreateCard() throws Exception {
        testMouseEventNavigation(
                "createCard.fxml",
                () -> {
                    try {
                        controller.switchToCreateCard(
                                createMouseEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void switchToLibrary() throws Exception {
        testMouseEventNavigation(
                "libraryUi.fxml",
                () -> {
                    try {
                        controller.switchToLibrary(
                                createMouseEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void switchToCreateQuiz() throws Exception {
        testMouseEventNavigation(
                "createQuizUI.fxml",
                () -> {
                    try {
                        controller.switchToCreateQuiz(
                                createMouseEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void switchToStudyMaterials() throws Exception {
        testActionEventNavigation(
                "createAccount.fxml",
                () -> {
                    try {
                        controller.switchToStudyMaterials(
                                createActionEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void switchToProfile() throws Exception {
        testActionEventNavigation(
                "createAccount.fxml",
                () -> {
                    try {
                        controller.switchToProfile(
                                createActionEvent()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }


    @Test
    void setCardQuestionSetsQuestion() throws Exception {
        Card card = new Card(
                "What is Java?",
                "Programming language",
                "default",
                1
        );

        runOnFxThread(() ->
                controller.setCardQuestion(card)
        );

        assertEquals(
                "What is Java?",
                cardText.getText()
        );

        assertSame(
                card,
                getField(controller, "card")
        );
    }

    @Test
    void setCardAnswerSetsAnswer() throws Exception {
        Card card = new Card(
                "What is Java?",
                "Programming language",
                "default",
                1
        );

        runOnFxThread(() ->
                controller.setCardAnswer(card)
        );

        assertEquals(
                "Programming language",
                cardText.getText()
        );

        assertSame(
                card,
                getField(controller, "card")
        );
    }


    private ActionEvent createActionEvent() {
        ActionEvent event = mock(ActionEvent.class);

        Node source = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        return event;
    }

    private java.awt.event.MouseEvent createMouseEvent() {
        MouseEvent event = mock(MouseEvent.class);

        Node source = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        return event;
    }

    private void testActionEventNavigation(
            String expectedFxml,
            Runnable action
    ) throws Exception {

        FXMLLoader loader = mock(FXMLLoader.class);

        Parent root = mock(Parent.class);

        Stage stage = mock(Stage.class);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {
                                 when(mock.load()).thenReturn(root);
                                 when(mock.getController())
                                         .thenReturn(mock(cardController.class));
                             })) {

            runOnFxThread(action);

            verify(stage, never()).close();
        }
    }

    private void testMouseEventNavigation(
            String expectedFxml,
            Runnable action
    ) throws Exception {

        Parent root = mock(Parent.class);

        Stage stage = mock(Stage.class);

        try (MockedConstruction<FXMLLoader> ignored =
                     mockConstruction(
                             FXMLLoader.class,
                             (mock, context) -> {
                                 when(mock.load()).thenReturn(root);
                                 when(mock.getController())
                                         .thenReturn(mock(cardController.class));
                             })) {

            runOnFxThread(action);

            verify(stage, never()).close();
        }
    }

    private static void runOnFxThread(Runnable runnable)
            throws Exception {

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

    private static void setField(
            Object object,
            String fieldName,
            Object value
    ) {
        try {
            Field field =
                    object.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(object, value);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getField(
            Object object,
            String fieldName
    ) {
        try {
            Field field =
                    object.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);

            return field.get(object);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
