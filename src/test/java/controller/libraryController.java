package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class libraryControllerTest {

    private static boolean javafxStarted = false;

    private libraryController controller;

    private ScrollPane scrollPane;
    private GridPane grid;

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
            controller = new libraryController();

            scrollPane = new ScrollPane();
            grid = new GridPane();

            setField(controller, "scrollPane", scrollPane);
            setField(controller, "grid", grid);
        });
    }

    @Test
    void setUserShouldStoreUserInsideController() {
        User user = createTestUser(123);

        controller.setUser(user);

        assertSame(
                user,
                getField(controller, "user")
        );
    }

    @Test
    void setUserShouldAllowReplacingExistingUser() {
        User firstUser = createTestUser(1);
        User secondUser = createTestUser(2);

        controller.setUser(firstUser);
        controller.setUser(secondUser);

        assertSame(
                secondUser,
                getField(controller, "user")
        );
    }

    @Test
    void setUserShouldAllowNullUser() {
        User user = createTestUser(123);

        controller.setUser(user);
        controller.setUser(null);

        assertNull(getField(controller, "user"));
    }

    @Test
    void getAllCardsShouldCallDaoWithCurrentUserId() throws Exception {
        User user = createTestUser(55);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(55))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            cardDaoMock.verify(() ->
                    CardDao.showAllCards(55)
            );
        }
    }

    @Test
    void getAllCardsShouldRenderOneCardIntoGrid() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(
                "Question 1",
                "Answer 1",
                "default",
                10
        ));

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            assertEquals(
                    1,
                    grid.getChildren().size()
            );

            Node renderedCard = grid.getChildren().getFirst();

            assertEquals(
                    0,
                    GridPane.getColumnIndex(renderedCard)
            );

            assertEquals(
                    0,
                    GridPane.getRowIndex(renderedCard)
            );
        }
    }

    @Test
    void getAllCardsShouldRenderMultipleCardsIntoGrid() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Question 1", "Answer 1", "default", 10));
        cards.add(new Card("Question 2", "Answer 2", "default", 10));
        cards.add(new Card("Question 3", "Answer 3", "default", 10));

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            assertEquals(
                    3,
                    grid.getChildren().size()
            );
        }
    }

    @Test
    void getAllCardsShouldPlaceCardsInTwoColumnLayout() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Question 1", "Answer 1", "default", 10));
        cards.add(new Card("Question 2", "Answer 2", "default", 10));
        cards.add(new Card("Question 3", "Answer 3", "default", 10));

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            Node firstCard = grid.getChildren().get(0);
            Node secondCard = grid.getChildren().get(1);
            Node thirdCard = grid.getChildren().get(2);

            assertEquals(0, GridPane.getColumnIndex(firstCard));
            assertEquals(0, GridPane.getRowIndex(firstCard));

            assertEquals(1, GridPane.getColumnIndex(secondCard));
            assertEquals(0, GridPane.getRowIndex(secondCard));

            assertEquals(0, GridPane.getColumnIndex(thirdCard));
            assertEquals(1, GridPane.getRowIndex(thirdCard));
        }
    }

    @Test
    void getAllCardsShouldPassQuestionAndAnswerToCardController() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        Card card = new Card(
                "What is Java?",
                "A programming language",
                "default",
                10
        );

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);

        cardController cardControllerMock = mock(cardController.class);

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored =
                        mockConstruction(
                                FXMLLoader.class,
                                (mock, context) -> {
                                    when(mock.load()).thenReturn(new AnchorPane());
                                    when(mock.getController()).thenReturn(cardControllerMock);
                                }
                        )
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            verify(cardControllerMock).setCardQuestion("What is Java?");
            verify(cardControllerMock).setCardAnswer("A programming language");
        }
    }

    @Test
    void getAllCardsShouldHandleEmptyCardListWithoutAddingNodes() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            assertTrue(
                    grid.getChildren().isEmpty()
            );
        }
    }

    @Test
    void getAllCardsShouldStoreCardsReturnedByDao() throws Exception {
        User user = createTestUser(10);
        controller.setUser(user);

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Question", "Answer", "default", 10));

        try (
                MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class);
                MockedConstruction<FXMLLoader> ignored = mockCardFxmlLoading()
        ) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(cards);

            runOnFxThread(() ->
                    controller.getAllCards()
            );

            assertSame(
                    cards,
                    getField(controller, "cards")
            );
        }
    }

    @Test
    void getAllCardsShouldThrowNullPointerExceptionWhenUserIsNotSet() {
        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            assertThrows(
                    NullPointerException.class,
                    () -> controller.getAllCards()
            );

            cardDaoMock.verifyNoInteractions();
        }
    }

    @Test
    void getAllCardsShouldThrowNullPointerExceptionWhenDaoReturnsNull() {
        User user = createTestUser(10);
        controller.setUser(user);

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            cardDaoMock
                    .when(() -> CardDao.showAllCards(10))
                    .thenReturn(null);

            assertThrows(
                    NullPointerException.class,
                    () -> controller.getAllCards()
            );
        }
    }

    @Test
    void initializeShouldNotThrowException() {
        assertDoesNotThrow(() ->
                controller.initialize(null, null)
        );
    }

    @Test
    void switchToCreateCardsShouldChangeSceneAndShowStage() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        MouseEvent event = mock(MouseEvent.class);
        Node source = mock(Node.class);
        Scene oldScene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(oldScene);
        when(oldScene.getWindow()).thenReturn(stage);

        AnchorPane root = new AnchorPane();
        ccController ccControllerMock = mock(ccController.class);

        try (
                MockedConstruction<FXMLLoader> ignored =
                        mockConstruction(
                                FXMLLoader.class,
                                (mock, context) -> {
                                    when(mock.load()).thenReturn(root);
                                    when(mock.getController()).thenReturn(ccControllerMock);
                                }
                        )
        ) {
            runOnFxThread(() -> {
                try {
                    controller.switchToCreateCards(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToCreateCardsShouldPassCurrentUserToCreateCardController() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        MouseEvent event = mock(MouseEvent.class);
        Node source = mock(Node.class);
        Scene oldScene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(oldScene);
        when(oldScene.getWindow()).thenReturn(stage);

        AnchorPane root = new AnchorPane();
        ccController ccControllerMock = mock(ccController.class);

        try (
                MockedConstruction<FXMLLoader> ignored =
                        mockConstruction(
                                FXMLLoader.class,
                                (mock, context) -> {
                                    when(mock.load()).thenReturn(root);
                                    when(mock.getController()).thenReturn(ccControllerMock);
                                }
                        )
        ) {
            runOnFxThread(() -> {
                try {
                    controller.switchToCreateCards(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            verify(ccControllerMock).setUser(user);
        }
    }

    @Test
    void switchToCreateCardsShouldThrowWhenEventSourceIsNull() {
        MouseEvent event = mock(MouseEvent.class);

        when(event.getSource()).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> controller.switchToCreateCards(event)
        );
    }

    private static MockedConstruction<FXMLLoader> mockCardFxmlLoading() {
        return mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    AnchorPane cardRoot = new AnchorPane();
                    cardController cardControllerMock = mock(cardController.class);

                    when(mock.load()).thenReturn(cardRoot);
                    when(mock.getController()).thenReturn(cardControllerMock);
                }
        );
    }

    private static User createTestUser(int userId) {
        User user = new User(
                "Test User",
                "test@example.com",
                "password",
                User.Role.student
        );

        user.setUserId(userId);

        return user;
    }

    private static void runOnFxThread(Runnable runnable) throws Exception {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> throwable = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                throwable.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX operation timed out"
        );

        if (throwable.get() != null) {
            if (throwable.get() instanceof Exception exception) {
                throw exception;
            }

            if (throwable.get() instanceof Error error) {
                throw error;
            }

            throw new RuntimeException(throwable.get());
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

    private static Object getField(
            Object object,
            String fieldName
    ) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}