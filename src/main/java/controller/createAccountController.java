package controller;

import dao.UserDao;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.awt.*;

public class createAccountController {

    private UserDao ud = new UserDao();

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
    



    public User getUserCreationDetails(){
        String un = newUsername.getText();
        User.Role role = null;
        String passw = newPassw.toString();
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
        role = User.Role.student;
        User user = new User(un, email, passw, role);
        return user;
    }




    public void createAccount(){
        User newUser = getUserCreationDetails();
        ud.registerUser(newUser);
        System.out.println("toimiipas");
    }

}
