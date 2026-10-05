package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import javafx.embed.swing.JFXPanel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class libraryControllerTest {

    private libraryController controller;

    @Mock
    private User user;

    @Mock
    private GridPane grid;

    @Mock
    private ScrollPane scrollPane;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        controller = new libraryController();

        setField(controller, "user", user);
        setField(controller, "grid", grid);
        setField(controller, "scrollPane", scrollPane);
    }

    @Test
    void setUser_shouldStoreUser() throws Exception {
        controller.setUser(user);

        Field field =
                libraryController.class.getDeclaredField("user");

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
    void getAllCards_shouldHandleNullCardList() {
        when(user.getUserId()).thenReturn(42);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            cardDaoMock.when(() -> CardDao.showAllCards(42))
                    .thenReturn(null);

            /*
             * Huom:
             * controllerin nykyinen koodi ei käsittele null-listaa.
             * Tämä testi dokumentoi nykyisen käytöksen.
             */
            assertThrows(
                    NullPointerException.class,
                    () -> controller.getAllCards()
            );
        }
    }

    @Test
    void initialize_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                controller.initialize(null, null)
        );
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