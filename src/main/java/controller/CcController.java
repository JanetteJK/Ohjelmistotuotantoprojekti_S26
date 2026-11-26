package controller;


import javafx.fxml.FXML;
import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CcController {

    private static final Logger log = LoggerFactory.getLogger(CcController.class);
    private Stage stage;
    private Scene scene;
    CcController cac;
    List<Card> cards = new ArrayList<>();
    private User user;

    @FXML
    TextArea questionBox;
    @FXML
    TextArea answerBox;
    @FXML
    TextField categoryField;
    @FXML
    Label cardSentLabel;

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

    public void submitCard(){
        String question = getQuestion();
        String answer = getAnswer();
        String category = getCategory();
        Card cardS = new Card(question, answer, category, user.getUserId());
        CardDao.addCard(cardS);
        questionBox.clear();
        answerBox.clear();
        categoryField.clear();
    }



    //switch funtions

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
        QuizController qc = fxmlLoader.getController();
        List<String> categories = CardDao.getCategories(user.getUserId());
        qc.currentUser = user;
        qc.populateCategorySelection(categories);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeP = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/initHomeGui.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();

        SignInController controller = fxmlLoader.getController();
        controller.setCurrentUser(user);
        controller.setUserGreeting(user.getUserName());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
