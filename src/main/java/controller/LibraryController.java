package controller;

import dao.CardDao;
import entity.Card;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LibraryController implements Initializable {

    private User user;
    private Stage stage;
    private Scene scene;
    private User currentUser;
    private static final String HOME_GUI_FXML_PATH = "/initHomeGui.fxml";


    @FXML
    private GridPane grid;

    public void setUser(User user) {
        this.user = user;
        this.currentUser = user;
    }


    public void getAllCards() {
        List<Card> cards;
        cards = (CardDao.showAllCards(user.getUserId()));

        int column = 0;
        int row = 0;
        try {
            assert cards != null;
            for (Card card : cards) {

                FXMLLoader cardFxmlLoader = new FXMLLoader(
                        getClass().getResource("/card.fxml"),
                        LanguageManager.getBundle()
                );

                // Load the FXML file

                AnchorPane anchorPane = cardFxmlLoader.load();
                CardController cc = cardFxmlLoader.getController();
                cc.setCardQuestion(card.getQuestion(), card.getCategory());
                cc.getCardAnswer(card.getAnswer());

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));

            }
        } catch (IOException _) {
            Logger logger = Logger.getLogger(LibraryController.class.getName());
            logger.severe("Error loading FXML file.");
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SignInController sc = new SignInController();
        LibraryController lc = new LibraryController();

    }

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/createCard.fxml"),
                LanguageManager.getBundle()
        );
        Parent root = fxmlLoader.load();
        CcController cardCtrl = fxmlLoader.getController();
        cardCtrl.setUser(user);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateQuiz(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeCQ = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createQuizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        QuizController qc = fxmlLoader.getController();
        List<String> categories = CardDao.getCategories(currentUser.getUserId());
        qc.currentUser = currentUser;
        qc.populateCategorySelection(categories);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeSM = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeP = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(HOME_GUI_FXML_PATH), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();

        SignInController controller = fxmlLoader.getController();
        controller.setUserGreeting(currentUser.getUserName());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}