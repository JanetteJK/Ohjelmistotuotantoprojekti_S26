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
import java.awt.event.MouseEvent;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class signInController {
    private UserDao ud = new UserDao();
    private Stage stage;
    private Scene scene;
    private createAccountController cac;
    private User currentUser;

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
    private Label nameTag;

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

    public void setCurrentUser(User cu){
        this.currentUser = cu;
    }

    // todo: see how to get the password value, create login handler for dao, repeat with sLogin
    public void tLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==tOk) {
            String un = gettUsername();
            String ps = gettPassw();
            String email = "teacher@email.fi";
            User teacher = new User(un, email, ps, User.Role.teacher);
            ud.logInUser(teacher);
            setCurrentUser(teacher);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/initHomeGui.fxml"));
            Parent root = fxmlLoader.load();
            signInController controller = fxmlLoader.getController();
            controller.setUserGreeting(currentUser.getUserName());
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==sOk) {
            String un = getSUsername();
            String ps = getsPassw();
            String email = "student@email.fi";
            User student = new User(un, email, ps, User.Role.student);
            ud.logInUser(student);
            setCurrentUser(student);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/initHomeGui.fxml"));
            Parent root = fxmlLoader.load();
            signInController controller = fxmlLoader.getController();
            controller.setUserGreeting(currentUser.getUserName());
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


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

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLibrary(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/libraryUi.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateQuiz(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createQuizUI.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/initHomeGui.fxml"));
        Parent root = fxmlLoader.load();

        signInController controller = fxmlLoader.getController();
        controller.setUserGreeting(currentUser.getUserName());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setUserGreeting(String un){
        nameTag.setText(un);
    }


}






