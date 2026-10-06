package controller;

import dao.*;
import javafx.fxml.FXML;
import entity.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ccController {

    CardDao cardDao = new CardDao();
    UserDao userDao = new UserDao();
    private Stage stage;
    private Scene scene;
    private ccController cac;
    List<Card> cards = new ArrayList<>();
    private Card card;
    private User user;

    @FXML
    TextArea questionBox;
    @FXML
    TextArea answerBox;
    @FXML
    private Button submitCard;
    @FXML
    private Label LibraryMenu;
    @FXML
    private Label StudyMaterialsMenu;
    @FXML
    private Label ProfileMenu;
    @FXML
    private Label CreateQuizMenu;
    @FXML
    private Label CreateCardMenu;


    // cardCreation functions
    public String getQuestion() {
        return questionBox.getText();
    }

    public String getAnswer() {
        return answerBox.getText();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void submitCard() {
        String question = getQuestion();
        String answer = getAnswer();
        String category = "default"; // Placeholder for category, todo: implement category selection logic
        //int userId = 1; // Placeholder for the current user's ID, todo: replace with actual logic to get the logged-in user's ID§
        //int userId = this.user.getUserId(); // Assuming User class has a method to get the user ID
        Card card = new Card(question, answer, category, user.getUserId());
        CardDao.addCard(card);
        questionBox.clear();
        answerBox.clear();
        System.out.println("Card submitted: " + question + " - " + answer);
    }

    /*public void showAllCards(int userId) {
        // Placeholder for the current user's ID, todo: replace with actual logic to get the logged-in user's ID
        //int userId = this.user.getUserId(); // Assuming User class has a method to get the user ID
        ArrayList<Card> cards = CardDao.showAllCards(userId); // Replace 1 with the actual user ID
        if (cards != null) {
            for (Card card : cards) {
                System.out.println("Question: " + card.getQuestion() + ", Answer: " + card.getAnswer() + ", Category: " + card.getCategory());
                cards.add(card);
            }
        } else {
            System.out.println("No cards found.");
        }
    }
    */

    //switch funtions
    public void switchToCreateAccount(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateCard(MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLibrary(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/libraryUi.fxml"));
        Parent root = fxmlLoader.load();
        // todo: add cards to library
        libraryController lc = fxmlLoader.getController();
        lc.setUser(user);
        lc.getAllCards();
        System.out.println(cards);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateQuiz(MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createQuizUI.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.@MonotonicNonNull MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfile(javafx.scene.input.@MonotonicNonNull MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
