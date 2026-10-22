package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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

    @Test
    void switchToCreateCards_shouldLoadViewAndSetUser() throws Exception {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        Node node = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);

        when(mouseEvent.getSource()).thenReturn(node);
        when(node.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        Parent root = mock(Parent.class);
        ccController cardController = mock(ccController.class);
        User user = mock(User.class);

        controller.setUser(user);

        try (MockedConstruction<FXMLLoader> loaders =
                     mockConstruction(FXMLLoader.class, (loader, context) -> {
                         when(loader.load()).thenReturn(root);
                         when(loader.getController()).thenReturn(cardController);
                     });
             MockedConstruction<Scene> scenes =
                     mockConstruction(Scene.class)) {

            controller.switchToCreateCards(mouseEvent);

            FXMLLoader loader = loaders.constructed().get(0);

            verify(loader).load();
            verify(loader).getController();
            verify(cardController).setUser(user);
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