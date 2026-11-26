
package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import javafx.embed.swing.JFXPanel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryControllerTest {

    private LibraryController controller;

    @Mock
    private User user;

    @Mock
    private GridPane grid;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        controller = new LibraryController();

        setField(controller, "user", user);
        setField(controller, "grid", grid);
    }

    @Test
    void setUser_shouldStoreUser() throws Exception {
        controller.setUser(user);

        Field field =
                LibraryController.class.getDeclaredField("user");

        field.setAccessible(true);

        assertSame(user, field.get(controller));
    }

    @Test
    void getAllCards_shouldRequestCardsForCurrentUser() {
        when(user.getUserId()).thenReturn(42);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(new ArrayList<>());

            controller.getAllCards();

            cardDaoMock.verify(() ->
                    CardDao.showAllCards(42)
            );
        }
    }

    @Test
    void getAllCards_shouldHandleEmptyCardList() {
        when(user.getUserId()).thenReturn(42);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(new ArrayList<>());

            assertDoesNotThrow(() ->
                    controller.getAllCards()
            );

            verifyNoInteractions(grid);
        }
    }

    @Test
    void getAllCards_shouldLoadCardsAndAddThemToGrid() throws Exception {
        when(user.getUserId()).thenReturn(42);

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        when(card1.getQuestion()).thenReturn("Question 1");
        when(card1.getCategory()).thenReturn("Category 1");
        when(card1.getAnswer()).thenReturn("Answer 1");

        when(card2.getQuestion()).thenReturn("Question 2");
        when(card2.getCategory()).thenReturn("Category 2");
        when(card2.getAnswer()).thenReturn("Answer 2");

        AnchorPane pane1 = new AnchorPane();
        AnchorPane pane2 = new AnchorPane();

        CardController cardController1 = mock(CardController.class);
        CardController cardController2 = mock(CardController.class);

        AtomicInteger index = new AtomicInteger();

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class);

             MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(
                             FXMLLoader.class,
                             (loader, context) -> {
                                 int current = index.getAndIncrement();

                                 when(loader.load()).thenReturn(
                                         current == 0 ? pane1 : pane2
                                 );

                                 when(loader.getController()).thenReturn(
                                         current == 0
                                                 ? cardController1
                                                 : cardController2
                                 );
                             })) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(List.of(card1, card2));

            controller.getAllCards();

            assertEquals(2, loaders.constructed().size());

            verify(cardController1).setCardQuestion(
                    "Question 1", "Category 1"
            );
            verify(cardController1).getCardAnswer("Answer 1");

            verify(cardController2).setCardQuestion(
                    "Question 2", "Category 2"
            );
            verify(cardController2).getCardAnswer("Answer 2");

            verify(grid).add(pane1, 0, 0);
            verify(grid).add(pane2, 0, 1);
        }
    }

    @Test
    void getAllCards_shouldMoveToNextRowAfterTwoCards() {
        when(user.getUserId()).thenReturn(42);

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);

        List<Card> cards = List.of(card1, card2, card3);

        AnchorPane pane1 = new AnchorPane();
        AnchorPane pane2 = new AnchorPane();
        AnchorPane pane3 = new AnchorPane();

        AtomicInteger index = new AtomicInteger();

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class);

             MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(
                             FXMLLoader.class,
                             (loader, context) -> {
                                 int current = index.getAndIncrement();

                                 when(loader.load()).thenReturn(
                                         List.of(pane1, pane2, pane3).get(current)
                                 );

                                 when(loader.getController()).thenReturn(
                                         mock(CardController.class)
                                 );
                             })) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(cards);

            controller.getAllCards();

            verify(grid).add(pane1, 0, 0);
            verify(grid).add(pane2, 0, 1);
            verify(grid).add(pane3, 0, 2);
        }
    }

    @Test
    void getAllCards_shouldHandleFxmlIOException() throws Exception {
        when(user.getUserId()).thenReturn(42);

        Card card = mock(Card.class);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class);

             MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(
                             FXMLLoader.class,
                             (loader, context) ->
                                     when(loader.load()).thenThrow(
                                             new IOException("FXML error")
                                     )
                     )) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(List.of(card));

            assertDoesNotThrow(() -> controller.getAllCards());

            verifyNoInteractions(grid);
        }
    }

    @Test
    void initialize_shouldRunWithoutExceptions() {
        assertDoesNotThrow(() ->
                controller.initialize(null, null)
        );
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
    void switchToProfile_shouldDelegateToPageController() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);

        try (MockedStatic<PageController> pageController =
                     mockStatic(PageController.class)) {

            controller.switchToProfile(mouseEvent);

            pageController.verify(() ->
                    PageController.switchToProfile(mouseEvent));
        }
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
}
