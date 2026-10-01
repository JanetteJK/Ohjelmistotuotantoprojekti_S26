package controller;

import entity.User;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class CreateAccountControllerTest {

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

        controller.createAccount();

        assertEquals("Account created successfully!", createAccountInfo.getText());
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = createAccountController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    @Test
    void switchToStartChangesScene() throws Exception {
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();

            try {
                StackPane root = new StackPane();
                Button button = new Button();

                root.getChildren().add(button);

                Scene oldScene = new Scene(root);
                stage.setScene(oldScene);

                ActionEvent event = new ActionEvent(button, null);

                controller.switchToStart(event);

                assertNotNull(stage.getScene());
                assertNotSame(oldScene, stage.getScene());
                assertNotNull(stage.getScene().getRoot());

            } catch (Throwable e) {
                error.set(e);
            } finally {
                stage.close();
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

        if (error.get() != null) {
            fail(error.get());
        }
    }
}