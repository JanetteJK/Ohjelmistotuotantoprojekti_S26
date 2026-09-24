package controller;
import dao.UserDao;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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


    UserDao ud;

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
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField newUsername;
    @FXML
    private PasswordField newPassw;
    @FXML
    private RadioButton teacherButton;
    @FXML
    private RadioButton studentButton;

    public signInController(UserDao ud, TextField tUsername, PasswordField tPassw, Button tOk, TextField sUsername, PasswordField sPassw, Button sOk, Hyperlink createAccount, Button createAccountButton, RadioButton studentButton, RadioButton teacherButton) {
        this.ud = ud;
        this.tUsername = tUsername;
        this.tPassw = tPassw;
        this.tOk = tOk;
        this.sUsername = sUsername;
        this.sPassw = sPassw;
        this.sOk = sOk;
        this.createAccount = createAccount;
        this.createAccountButton = createAccountButton;
        this.teacherButton = teacherButton;
        this.studentButton = studentButton;
    }


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
            String email = "example@email.fi";
            User teacher = new User(un, email, ps, User.Role.teacher);

        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==sOk) {
            String un = getSUsername();

        }
    }

    public void openCreateAccount(){
        System.out.println("ähän tulee sivu");
    }

    public User getUserCreationDetails(){
        String un = newUsername.getText();
        User.Role role = null;
        String passw = newPassw.toString();
        if (teacherButton.isSelected()){
            role = User.Role.teacher;
        }
        else if (studentButton.isSelected()){
            role = User.Role.student;
        }
        else {
            System.out.println("no role selected");
        }
        User user = new User(un, "Malli@email.fi", passw, role);
        return user;
    }

    public void createAccount(){
        User newUser = getUserCreationDetails();
        UserDao.registerUser(newUser);
    }
}


