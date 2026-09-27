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
import view.SignIn;

import java.awt.*;
import java.io.IOException;

public class createAccountController {
    private UserDao ud = new UserDao();
    signInController sct;
    Stage stage;
    Scene scene;

    @FXML
    private javafx.scene.control.Button createAccountButton;
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

    



    public User getUserCreationDetails(){
        String un = newUsername.getText();
        User.Role role = null;
        String passw = newPassw.getText();
        String email = null;
        if (teacherButton.isSelected()){
            role = User.Role.teacher;
            email = "teacher@email.com";
        }
        else if (studentButton.isSelected()){
            role = User.Role.student;
            email = "student@email.com";
        }
        else {
            System.out.println("no role selected");
        }
        User user = new User(un, email, passw, role);
        return user;
    }




    public void createAccount(){
        User newUser = getUserCreationDetails();
        ud.registerUser(newUser);
        // todo: add check if username in use to ud and finish this
        System.out.println("toimiipas");
        createAccountInfo.setText("Account created successfully!");
    }

    public void switchToStart(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginUi.fxml"));
        Parent root = fxmlLoader.load();
        sct = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
