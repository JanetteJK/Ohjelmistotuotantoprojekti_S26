package controller;

import entity.Card;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class cardControllerTest {

    private cardController controller;
    private TextArea questionBox;
    private TextArea answerBox;
    private Button submitCard;
    private TextField cardText;

    @BeforeAll
    static void startJavaFxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            latch.countDown();
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX runtime did not start in time.");
        }
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new cardController();
        questionBox = new TextArea();
        answerBox = new TextArea();
        submitCard = new Button();
        cardText = new TextField();

        setPrivateField(controller, "questionBox", questionBox);
        setPrivateField(controller, "answerBox", answerBox);
        setPrivateField(controller, "submitCard", submitCard);
        setPrivateField(controller, "cardText", cardText);
    }

    @Test
    void getQuestion() {
        controller.questionBox.setText("What is the capital of France?");
        String question = controller.getQuestion();
        assertEquals("What is the capital of France?", question);
    }

    @Test
    void getAnswer() {
        controller.answerBox.setText("Paris");
        String answer = controller.getAnswer();
        assertEquals("Paris", answer);
    }

    @Test
    void setCardQuestion() {
        Card card = new Card("What is the capital of France?", "Paris", "Geography", 1);
        controller.setCardQuestion(card);
        assertEquals("What is the capital of France?", card.getQuestion());
    }

    @Test
    void setCardAnswer() {
        Card card = new Card("What is the capital of France?", "Paris", "Geography", 1);
        controller.setCardAnswer(card);
        assertEquals("Paris", card.getAnswer());
    }
}