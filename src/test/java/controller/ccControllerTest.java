package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
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



    @Test
    void switchToLibrary_shouldLoadViewAndGetCards() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        libraryController libraryController =
                mock(libraryController.class);
        User user = mock(User.class);

        controller.setUser(user);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(libraryController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToLibrary(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(libraryController).setUser(user);
            verify(libraryController).getAllCards();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToCreateQuiz_shouldLoadView() throws Exception {
        java.awt.event.MouseEvent mouseEvent = mock(java.awt.event.MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToCreateQuiz(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }

    @Test
    void switchToStudyMaterials_shouldLoadView() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToStudyMaterials(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(stage).setScene(any(Scene.class));
            verify(stage).show();
        }
    }


    private Stage createTestStage() {
        Stage stage = new Stage();
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        return stage;
    }

    private Button createButtonOnStage(Stage stage) {
        Button button = new Button();
        stage.getScene().setRoot(button);
        return button;
    }

    private void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}