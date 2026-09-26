package controller;
import dao.UserDao;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class signInController {


    private UserDao ud = new UserDao();
    private Stage stage;
    private Scene scene;
    private createAccountController cac;

    @FXML
    private TextField tUsername;
    @FXML
    private PasswordField tPassw;
    @FXML
    private Button tOk;
    @FXML
    private TextField sUsername;
    @FXML
    private PasswordField sPassw;
    @FXML
    private Button sOk;
    @FXML
    private Hyperlink createAccount;

    public String gettUsername() {
        return tUsername.getText();
    }

    public PasswordField gettPassw(){
        return tPassw;
    }

    public String getSUsername(){
        return sUsername.getText();
    }

    public PasswordField getsPassw(){
        return sPassw;
    }

    // todo: see how to get the password value, create login handler for dao, repeat with sLogin
    public void tLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==tOk) {
            String un = gettUsername();
            String ps = gettPassw().toString();
            String email = "teacher@email.fi";
            User teacher = new User(un, email, ps, User.Role.teacher);
            ud.logInUser(teacher);

        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==sOk) {
            String un = getSUsername();
            String ps = getsPassw().toString();
            String email = "student@email.fi";
            User student = new User(un, email, ps, User.Role.student);
            ud.logInUser(student);
        }
    }

    public void switchToCreateAccount(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}






