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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static controller.LibraryControllerTest.createTestUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ccControllerTest {

    private static boolean javafxStarted = false;

    private ccController controller;

    private TextArea questionBox;
    private TextArea answerBox;
    private Button submitCard;
    private Label libraryMenu;
    private Label studyMaterialsMenu;
    private Label profileMenu;
    private Label createQuizMenu;
    private Label createCardMenu;

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
            controller = new ccController();

            questionBox = new TextArea();
            answerBox = new TextArea();
            submitCard = new Button();
            libraryMenu = new Label();
            studyMaterialsMenu = new Label();
            profileMenu = new Label();
            createQuizMenu = new Label();
            createCardMenu = new Label();

            setField(controller, "questionBox", questionBox);
            setField(controller, "answerBox", answerBox);
            setField(controller, "submitCard", submitCard);
            setField(controller, "LibraryMenu", libraryMenu);
            setField(controller, "StudyMaterialsMenu", studyMaterialsMenu);
            setField(controller, "ProfileMenu", profileMenu);
            setField(controller, "CreateQuizMenu", createQuizMenu);
            setField(controller, "CreateCardMenu", createCardMenu);
        });
    }

    @Test
    void getQuestionShouldReturnTextFromQuestionBox() throws Exception {
        runOnFxThread(() -> {
            questionBox.setText("What is Java?");

            assertEquals(
                    "What is Java?",
                    controller.getQuestion()
            );
        });
    }

    @Test
    void getQuestionShouldReturnEmptyStringWhenQuestionBoxIsEmpty() throws Exception {
        runOnFxThread(() -> {
            questionBox.setText("");

            assertEquals(
                    "",
                    controller.getQuestion()
            );
        });
    }

    @Test
    void getAnswerShouldReturnTextFromAnswerBox() throws Exception {
        runOnFxThread(() -> {
            answerBox.setText("A programming language");

            assertEquals(
                    "A programming language",
                    controller.getAnswer()
            );
        });
    }

    @Test
    void getAnswerShouldReturnEmptyStringWhenAnswerBoxIsEmpty() throws Exception {
        runOnFxThread(() -> {
            answerBox.setText("");

            assertEquals(
                    "",
                    controller.getAnswer()
            );
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
    void setUserShouldReplaceExistingUser() {
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
    void submitCardShouldCreateCardWithQuestionAnswerDefaultCategoryAndUserId() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        runOnFxThread(() -> {
            questionBox.setText("What is Java?");
            answerBox.setText("A programming language");
        });

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(argThat(card ->
                            card.getQuestion().equals("What is Java?")
                                    && card.getAnswer().equals("A programming language")
                                    && card.getCategory().equals("default")
                                    && card.getUserId() == 123
                    ))
            );
        }
    }

    @Test
    void submitCardShouldCallCardDaoAddCardExactlyOnce() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        runOnFxThread(() -> {
            questionBox.setText("Question");
            answerBox.setText("Answer");
        });

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.submitCard();

            cardDaoMock.verify(
                    () -> CardDao.addCard(any(Card.class)),
                    times(1)
            );
        }
    }

    @Test
    void submitCardShouldClearQuestionAndAnswerFieldsAfterSubmit() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        runOnFxThread(() -> {
            questionBox.setText("Question");
            answerBox.setText("Answer");
        });

        try (MockedStatic<CardDao> ignored = mockStatic(CardDao.class)) {
            controller.submitCard();

            runOnFxThread(() -> {
                assertEquals("", questionBox.getText());
                assertEquals("", answerBox.getText());
            });
        }
    }

    @Test
    void submitCardShouldAllowEmptyQuestionAndAnswer() throws Exception {
        User user = createTestUser(123);
        controller.setUser(user);

        runOnFxThread(() -> {
            questionBox.setText("");
            answerBox.setText("");
        });

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            controller.submitCard();

            cardDaoMock.verify(() ->
                    CardDao.addCard(argThat(card ->
                            card.getQuestion().equals("")
                                    && card.getAnswer().equals("")
                                    && card.getCategory().equals("default")
                                    && card.getUserId() == 123
                    ))
            );
        }
    }

    @Test
    void submitCardShouldThrowNullPointerExceptionWhenUserIsNotSet() throws Exception {
        runOnFxThread(() -> {
            questionBox.setText("Question");
            answerBox.setText("Answer");
        });

        try (MockedStatic<CardDao> cardDaoMock = mockStatic(CardDao.class)) {
            assertThrows(
                    NullPointerException.class,
                    () -> controller.submitCard()
            );

            cardDaoMock.verifyNoInteractions();
        }
    }

    @Test
    void submitCardShouldNotClearFieldsWhenUserIsNotSet() throws Exception {
        runOnFxThread(() -> {
            questionBox.setText("Question");
            answerBox.setText("Answer");
        });

        try (MockedStatic<CardDao> ignored = mockStatic(CardDao.class)) {
            assertThrows(
                    NullPointerException.class,
                    () -> controller.submitCard()
            );

            runOnFxThread(() -> {
                assertEquals("Question", questionBox.getText());
                assertEquals("Answer", answerBox.getText());
            });
        }
    }


    private static MockedConstruction<FXMLLoader> mockNavigationLoader(
            Object controllerToReturn
    ) {
        return mockConstruction(
                FXMLLoader.class,
                (mock, context) -> {
                    Parent root = new AnchorPane();

                    when(mock.load()).thenReturn(root);
                    when(mock.getController()).thenReturn(controllerToReturn);
                }
        );
    }

    private static ActionEvent createActionEventWithStage() {
        ActionEvent event = mock(ActionEvent.class);
        Node source = mock(Node.class);
        Scene oldScene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(oldScene);
        when(oldScene.getWindow()).thenReturn(stage);

        return event;
    }

    private static MouseEvent createMouseEventWithStage() {
        MouseEvent event = mock(MouseEvent.class);
        Node source = mock(Node.class);
        Scene oldScene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(event.getSource()).thenReturn(source);
        when(source.getScene()).thenReturn(oldScene);
        when(oldScene.getWindow()).thenReturn(stage);

        return event;
    }

    private static Stage getStageFromActionEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
    }

    private static Stage getStageFromMouseEvent(MouseEvent event) {
        return (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
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