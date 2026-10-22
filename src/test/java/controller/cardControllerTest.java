package controller;

import dao.CardDao;
import entity.User;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class cardControllerTest {

    private cardController controller;
    private AutoCloseable mocks;

    @Mock
    private Label cardText;

    @Mock
    private Button showAnswerButton;

    @Mock
    private Button deleteCardButton;

    @Mock
    private CardDao cardDao;

    @Mock
    private User user;

    @Mock
    private ActionEvent event;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);

        controller = new cardController();

        setPrivateField(controller, "cardText", cardText);
        setPrivateField(controller, "showAnswerButton", showAnswerButton);
        setPrivateField(controller, "deleteCardButton", deleteCardButton);
        setPrivateField(controller, "cardDao", cardDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void setCardQuestion_shouldSetLabelText() {
        controller.setCardQuestion("What is Java?");

        verify(cardText).setText("What is Java?");
    }

    @Test
    void setCardAnswer_shouldStoreAnswer() throws Exception {
        controller.getCardAnswer("Java is a programming language.");

        Field answerField =
                cardController.class.getDeclaredField("answer");

        answerField.setAccessible(true);

        assertEquals(
                "Java is a programming language.",
                answerField.get(controller)
        );
    }

    @Test
    void setCardAnswer_shouldShowAnswerWhenButtonSaysShowAnswer() {
        when(showAnswerButton.getText()).thenReturn("Show Answer");

        controller.getCardAnswer("Java is a programming language.");
        controller.setCardAnswer(event);

        verify(showAnswerButton).setText("Hide Answer");
        verify(cardText).setText("Java is a programming language.");
    }


    @Test
    void setUser_shouldStoreUser() throws Exception {
        controller.setUser(user);

        Field userField =
                cardController.class.getDeclaredField("user");

        userField.setAccessible(true);

        assertSame(user, userField.get(controller));
    }

    @Test
    void deleteCard_shouldCallDaoWithCurrentQuestion() {
        when(cardText.getText()).thenReturn("What is Java?");

        try (MockedStatic<CardDao> mockedCardDao = mockStatic(CardDao.class)) {

            controller.deleteCard(event);

            mockedCardDao.verify(
                    () -> CardDao.deleteCard("What is Java?")
            );
        }
    }



    private static void setPrivateField(
            Object target,
            String fieldName,
            Object value
    ) throws Exception {

        Field field =
                target.getClass().getDeclaredField(fieldName);

        field.setAccessible(true);
        field.set(target, value);
    }
}