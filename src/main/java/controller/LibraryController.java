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
import javafx.scene.input.MouseEvent;
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
        // Initialization logic if needed
    }

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToCreateCards(actionEvent);
    }

    public void switchToLibrary(MouseEvent actionEvent) throws IOException {
        PageController.switchToLibrary(actionEvent);
    }


    public void switchToCreateQuiz(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToCreateQuiz(actionEvent);

    }


    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToProfile(actionEvent);
    }


}