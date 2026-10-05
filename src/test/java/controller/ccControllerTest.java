package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import javafx.embed.swing.JFXPanel;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ccControllerTest {

    private ccController controller;

    @Mock
    private TextArea questionBox;

    @Mock
    private TextArea answerBox;

    @Mock
    private User user;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        controller = new ccController();

        setField(controller, "questionBox", questionBox);
        setField(controller, "answerBox", answerBox);
        setField(controller, "user", user);
    }

    @Test
    void getQuestion_shouldReturnQuestionBoxText() {
        when(questionBox.getText()).thenReturn("Question");

        assertEquals("Question", controller.getQuestion());

        verify(questionBox).getText();
    }

    @Test
    void getAnswer_shouldReturnAnswerBoxText() {
        when(answerBox.getText()).thenReturn("Answer");

        assertEquals("Answer", controller.getAnswer());

        verify(answerBox).getText();
    }

    @Test
    void setUser_shouldStoreUser() throws Exception {
        controller.setUser(user);

        Field field = ccController.class.getDeclaredField("user");
        field.setAccessible(true);

        assertSame(user, field.get(controller));
    }

    @Test
    void submitCard_shouldCreateAndAddCard() {
        when(questionBox.getText()).thenReturn("What is Java?");
        when(answerBox.getText()).thenReturn("Programming language");
        when(user.getUserId()).thenReturn(42);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(argThat(card ->
                            card.getQuestion().equals("What is Java?")
                                    && card.getAnswer().equals("Programming language")
                                    && card.getCategory().equals("default")
                                    && card.getUserId() == 42
                    ))
            );

            verify(questionBox).clear();
            verify(answerBox).clear();
        }
    }

    @Test
    void submitCard_shouldUseCorrectUserId() {
        when(questionBox.getText()).thenReturn("Q");
        when(answerBox.getText()).thenReturn("A");
        when(user.getUserId()).thenReturn(123);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(argThat(card ->
                            card.getUserId() == 123
                    ))
            );
        }
    }

    @Test
    void submitCard_shouldClearBothTextAreas() {
        when(questionBox.getText()).thenReturn("Q");
        when(answerBox.getText()).thenReturn("A");
        when(user.getUserId()).thenReturn(1);

        try (MockedStatic<CardDao> ignored =
                     mockStatic(CardDao.class)) {

            controller.submitCard();

            verify(questionBox).clear();
            verify(answerBox).clear();
        }
    }

    @Test
    void submitCard_shouldUseDefaultCategory() {
        when(questionBox.getText()).thenReturn("Q");
        when(answerBox.getText()).thenReturn("A");
        when(user.getUserId()).thenReturn(1);

        try (MockedStatic<CardDao> cardDaoMock =
                     mockStatic(CardDao.class)) {

            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(argThat(card ->
                            "default".equals(card.getCategory())
                    ))
            );
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