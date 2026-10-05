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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class libraryController implements Initializable {

    libraryController lc;
    Card card;
    private List<Card> cards = new ArrayList<>();
    private User user;
    private Stage stage;
    private Scene scene;


    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;

    public void setUser(User user) {
        this.user = user;
    }



    public void getAllCards() {
        cards = (CardDao.showAllCards(user.getUserId()));

        int column = 0;
        int row = 0;
        try {
            for (Card card : cards) {
                FXMLLoader cardFxmlLoader = new FXMLLoader();
                System.out.println("Adding card: " + card.getQuestion() + " to library");
                cardFxmlLoader.setLocation(getClass().getResource("/card.fxml"));
                // Load the FXML file

                AnchorPane anchorPane = cardFxmlLoader.load();
                System.out.println("Loaded card: " + card.getQuestion() + " to library");
                cardController cc = cardFxmlLoader.getController();
                cc.setCardQuestion(card.getQuestion());
                cc.getCardAnswer(card.getAnswer());

                if(column == 1) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInController sc = new signInController();
        libraryController lc = new libraryController();

    }

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"));
        Parent root = fxmlLoader.load();
        ccController cardCtrl = fxmlLoader.getController();
        cardCtrl.setUser(user);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}