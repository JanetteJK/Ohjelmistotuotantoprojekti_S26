package controller;


import javafx.fxml.FXML;
import dao.CardDao;
import dao.UserDao;
import entity.Card;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CcController {

    private static final Logger log = LoggerFactory.getLogger(CcController.class);
    CardDao cardDao;
    UserDao userDao;
    private Stage stage;
    private Scene scene;
    CcController cac;
    List<Card> cards = new ArrayList<>();
    Card card;
    private User user;

    @FXML
    TextArea questionBox;
    @FXML
    TextArea answerBox;
    @FXML
    TextField categoryField;
    @FXML
    private Button submitCard;
    @FXML
    private Label libraryMenu;
    @FXML
    private Label studyMaterialsMenu;
    @FXML
    private Label profileMenu;
    @FXML
    private Label createQuizMenu;
    @FXML
    private Label createCardsMenu;


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

    public String getCategory() {
        return categoryField.getText();
    }

    public void submitCard() {
        String question = getQuestion();
        String answer = getAnswer();
        String category = getCategory();
        Card cardS = new Card(question, answer, category, user.getUserId());
        log.info("Card submitted: {} - {}", question, answer);
        CardDao.addCard(cardS);
        questionBox.clear();
        answerBox.clear();
    }

    //switch funtions
    public void switchToCreateAccount(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createAccount.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateCard(MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createCard.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLibrary(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/libraryUI.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        LibraryController lc = fxmlLoader.getController();
        lc.setUser(user);
        lc.getAllCards();
        log.info("Cards: {}", cards);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateQuiz(MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createQuizUI.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.@MonotonicNonNull MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createAccount.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfile(javafx.scene.input.@MonotonicNonNull MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createAccount.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
