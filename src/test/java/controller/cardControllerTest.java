package controller;

import dao.CardDao;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class cardControllerTest {

    private static boolean javafxStarted = false;

    private cardController controller;

    private Label cardText;
    private Button showAnswerButton;
    private Button deleteCardButton;

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

            cardText = new Label();
            showAnswerButton = new Button();
            deleteCardButton = new Button();

            setField(controller, "cardText", cardText);
            setField(controller, "showAnswerButton", showAnswerButton);
            setField(controller, "deleteCardButton", deleteCardButton);
        });
    }

    @Test
    void setCardQuestionShouldUpdateCardTextLabel() throws Exception {
        runOnFxThread(() -> {
            controller.setCardQuestion("What is Java?");

            assertEquals(
                    "What is Java?",
                    cardText.getText()
            );
        });
    }

    @Test
    void setCardQuestionShouldAllowEmptyQuestion() throws Exception {
        runOnFxThread(() -> {
            controller.setCardQuestion("");

            assertEquals(
                    "",
                    cardText.getText()
            );
        });
    }

    @Test
    void setCardQuestionShouldAllowNullQuestion() throws Exception {
        runOnFxThread(() -> {
            controller.setCardQuestion(null);

            assertNull(cardText.getText());
        });
    }

    @Test
    void setCardAnswerStringShouldStoreAnswer() {
        controller.setCardAnswer("A programming language");

        assertEquals(
                "A programming language",
                getField(controller, "answer")
        );
    }

    @Test
    void setCardAnswerStringShouldAllowEmptyAnswer() {
        controller.setCardAnswer("");

        assertEquals(
                "",
                getField(controller, "answer")
        );
    }

    @Test
    void setCardAnswerStringShouldAllowNullAnswer() {
        controller.setCardAnswer((String) null);

        assertNull(getField(controller, "answer"));
    }

    @Test
    void setCardAnswerActionEventShouldDisplayStoredAnswer() throws Exception {
        controller.setCardAnswer("A programming language");

        runOnFxThread(() -> {
            controller.setCardAnswer(new ActionEvent());

            assertEquals(
                    "A programming language",
                    cardText.getText()
            );
        });
    }

    @Test
    void setCardAnswerActionEventShouldSetLabelToNullWhenAnswerIsNull() throws Exception {
        controller.setCardAnswer((String) null);

        runOnFxThread(() -> {
            controller.setCardAnswer(new ActionEvent());

            assertNull(cardText.getText());
        });
    }

    @Test
    void setUserShouldStoreUser() {
        User user = new User(
                "Test User",
                "test@example.com",
                "password",
                User.Role.student
        );

        controller.setUser(user);

        assertSame(
                user,
                getField(controller, "user")
        );
    }

    @Test
    void setUserShouldAllowNullUser() {
        controller.setUser(null);

        assertNull(getField(controller, "user"));
    }

    @Test
    void deleteCardShouldCallCardDaoDeleteCardWithCurrentQuestionText() throws Exception {
        runOnFxThread(() ->
                cardText.setText("What is Java?")
        );

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.deleteCard(new ActionEvent());

            cardDaoMock.verify(() ->
                    CardDao.deleteCard("What is Java?")
            );
        }
    }

    @Test
    void deleteCardShouldPassEmptyTextToCardDaoWhenLabelIsEmpty() throws Exception {
        runOnFxThread(() ->
                cardText.setText("")
        );

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.deleteCard(new ActionEvent());

            cardDaoMock.verify(() ->
                    CardDao.deleteCard("")
            );
        }
    }

    @Test
    void deleteCardShouldPassNullToCardDaoWhenLabelTextIsNull() throws Exception {
        runOnFxThread(() ->
                cardText.setText(null)
        );

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.deleteCard(new ActionEvent());

            cardDaoMock.verify(() ->
                    CardDao.deleteCard(null)
            );
        }
    }

    private static void runOnFxThread(Runnable runnable) throws Exception {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        RuntimeException[] exception = new RuntimeException[1];
        Error[] error = new Error[1];

        Platform.runLater(() -> {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                exception[0] = e;
            } catch (Error e) {
                error[0] = e;
            } finally {
                latch.countDown();
            }
        });

        assertTrue(
                latch.await(10, TimeUnit.SECONDS),
                "JavaFX operation timed out"
        );

        if (exception[0] != null) {
            throw exception[0];
        }

        if (error[0] != null) {
            throw error[0];
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