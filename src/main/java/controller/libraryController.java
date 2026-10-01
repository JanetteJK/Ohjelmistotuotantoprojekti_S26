package controller;

import dao.CardDao;
import entity.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class libraryController {

    libraryController lc;
    Card card;
    List<Card> cards;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
    @FXML
    private javafx.scene.control.TextField cardText;

    public void setCardQuestion(Card card) {
        this.card = card;
        cardText.setText(card.getQuestion());
    }

    public void setCardAnswer(Card card) {
        this.card = card;
        cardText.setText(card.getAnswer());
    }

    public void getAllCards() {
        cards = (CardDao.showAllCards(1));
    }

    public void addCardsToLibrary() throws IOException {
        int column = 0;
        int row = 0;
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("Adding card: " + cards.get(i).getQuestion() + " to library");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/card.fxml"));
            // Load the FXML file
            fxmlLoader.load();
            AnchorPane anchorPane = fxmlLoader.getRoot();
            System.out.println("Loaded card: " + cards.get(i).getQuestion() + " to library");

            //lc.setCardQuestion(cards.get(i));

            if(column == 3) {
                column = 0;
                row++;
            }

            grid.add(anchorPane, column++, row);
            GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));

        }
    }
}
