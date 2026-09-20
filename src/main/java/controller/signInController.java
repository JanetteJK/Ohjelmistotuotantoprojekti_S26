package controller;
import dao.UserDao;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import java.awt.*;
import java.awt.event.ActionEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;


public class signInController {

    UserDao ud = new UserDao();

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
            PasswordField ps = gettPassw();
            // todo: here the thing that I talked about w sonja



        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==sOk) {

        }
    }

    // todo: 'create account' functions
}


