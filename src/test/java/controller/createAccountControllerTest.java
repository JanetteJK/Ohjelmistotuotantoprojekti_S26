package controller;

import dao.UserDao;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class createAccountControllerTest {

    private createAccountController controller;

    private TextField newUsername;
    private PasswordField newPassw;
    private RadioButton teacherButton;
    private RadioButton studentButton;
    private Label createAccountInfo;

    @BeforeAll
    static void startJavaFxToolkit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }

        latch.await();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new createAccountController();

        newUsername = new TextField();
        newPassw = new PasswordField();
        teacherButton = new RadioButton();
        studentButton = new RadioButton();
        createAccountInfo = new Label();

        setField("newUsername", newUsername);
        setField("newPassw", newPassw);
        setField("teacherButton", teacherButton);
        setField("studentButton", studentButton);
        setField("createAccountInfo", createAccountInfo);
    }

    @Test
    void getUserCreationDetailsCreatesTeacherUser() {
        newUsername.setText("teacherUser");
        newPassw.setText("teacherPassword");
        teacherButton.setSelected(true);
        studentButton.setSelected(false);

        User user = controller.getUserCreationDetails();

        assertEquals("teacherUser", user.getUserName());
        assertEquals("teacherPassword", user.getPassword());
        assertEquals("teacher@email.com", user.getEmail());
        assertEquals(User.Role.teacher, user.getRole());
    }

    @Test
    void getUserCreationDetailsCreatesStudentUser() {
        newUsername.setText("studentUser");
        newPassw.setText("studentPassword");
        teacherButton.setSelected(false);
        studentButton.setSelected(true);

        User user = controller.getUserCreationDetails();

        assertEquals("studentUser", user.getUserName());
        assertEquals("studentPassword", user.getPassword());
        assertEquals("student@email.com", user.getEmail());
        assertEquals(User.Role.student, user.getRole());
    }

    @Test
    void getUserCreationDetailsCreatesUserWithNullRoleWhenNoRoleSelected() {
        newUsername.setText("noRoleUser");
        newPassw.setText("password123");
        teacherButton.setSelected(false);
        studentButton.setSelected(false);

        User user = controller.getUserCreationDetails();

        assertEquals("noRoleUser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getRole());
    }

    @Test
    void createAccountSetsSuccessMessage() {
        newUsername.setText("newUser");
        newPassw.setText("newPassword");
        studentButton.setSelected(true);

        try (MockedStatic<UserDao> userDao = org.mockito.Mockito.mockStatic(UserDao.class)) {
            controller.createAccount();
        }

        assertEquals("ELEM_account_created", createAccountInfo.getText());
    }

    @Test
    void createAccountRegistersNewUser() {
        newUsername.setText("newUser");
        newPassw.setText("newPassword");
        studentButton.setSelected(true);

        try (MockedStatic<UserDao> userDao = org.mockito.Mockito.mockStatic(UserDao.class)) {
            controller.createAccount();

            userDao.verify(() -> UserDao.registerUser(org.mockito.ArgumentMatchers.argThat(
                    user -> "newUser".equals(user.getUserName())
                            && "newPassword".equals(user.getPassword())
                            && User.Role.student == user.getRole()
            )));
        }
    }

    @Test
    void switchToStart_shouldLoadLoginView() throws Exception {
        ActionEvent actionEvent = org.mockito.Mockito.mock(ActionEvent.class);
        Node node = org.mockito.Mockito.mock(Node.class);
        Scene scene = org.mockito.Mockito.mock(Scene.class);
        Stage stage = org.mockito.Mockito.mock(Stage.class);
        org.mockito.Mockito.when(actionEvent.getSource()).thenReturn(node);
        org.mockito.Mockito.when(node.getScene()).thenReturn(scene);
        org.mockito.Mockito.when(scene.getWindow()).thenReturn(stage);
        Parent root = org.mockito.Mockito.mock(Parent.class);

        try (MockedConstruction<FXMLLoader> loaders = org.mockito.Mockito.mockConstruction(
                FXMLLoader.class, (loader, context) -> org.mockito.Mockito.when(loader.load()).thenReturn(root));
             MockedConstruction<Scene> scenes = org.mockito.Mockito.mockConstruction(Scene.class)) {
            controller.switchToStart(actionEvent);

            org.mockito.Mockito.verify(loaders.constructed().get(0)).load();
            org.mockito.Mockito.verify(stage).setScene(org.mockito.ArgumentMatchers.any(Scene.class));
            org.mockito.Mockito.verify(stage).show();
        }
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = createAccountController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

}
