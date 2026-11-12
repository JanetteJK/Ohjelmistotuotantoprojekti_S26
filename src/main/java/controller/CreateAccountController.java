package controller;

import dao.UserDao;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateAccountController {
    private static final Logger log = LoggerFactory.getLogger(CreateAccountController.class);
    SignInController sct;
    Stage stage;
    Scene scene;

    @FXML
    public TextField newUsername;
    @FXML
    private PasswordField newPassw;
    @FXML
    private RadioButton teacherButton;
    @FXML
    private RadioButton studentButton;
    @FXML
    public Label createAccountInfo;


    public User getUserCreationDetails() {
        String un = newUsername.getText();
        User.Role role = null;
        String passw = newPassw.getText();
        String email = null;
        if (teacherButton.isSelected()) {
            role = User.Role.teacher;
            email = "teacher@email.com";
        } else if (studentButton.isSelected()) {
            role = User.Role.student;
            email = "student@email.com";
        } else {
            log.info("no role selected");
        }
        return new User(un, email, passw, role);
    }


    public void createAccount() {
        User newUser = getUserCreationDetails();
        UserDao.registerUser(newUser);
        createAccountInfo.setText("ELEM_account_created");
    }

    public void switchToStart(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/loginUi.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        sct = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
