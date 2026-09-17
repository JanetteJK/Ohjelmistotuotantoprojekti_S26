package controller;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import java.awt.*;
import java.awt.event.ActionEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;


public class signInController {

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

    public String gettPassw(){
        return tPassw.getText();
    }

    public String getSUsername(){
        return sUsername.getText();
    }

    public String getsPassw(){
        return sPassw.getText();
    }
    // todo: see how to get the password value, create login handler for dao, repeat with sLogin
    public void tLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==tOk) {
            System.out.println(gettUsername());
        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) {
        if(actionEvent.getSource()==sOk) {
            System.out.println(getSUsername());
        }
    }

    // todo: create account functions
}


