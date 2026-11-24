package controller;

import dao.CardDao;
import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;



public class QuizController {
    private static final Logger msg = Logger.getLogger(QuizController.class.getName());
    User currentUser;
    private List<String> categories;
    private String correctAnswer;
    private int score;
    private int index = 0;
    private List<String> options = new java.util.ArrayList<>();
    private List<Card> quizCards;
    private final Random random = new Random();
    private Stage stage;
    private Scene scene;

    @FXML
    ChoiceBox<String> categorySelection;
    @FXML
    Button startQuizButton;
    @FXML
    GridPane cardSlot;
    @FXML
    Label questionSlot;
    @FXML
    RadioButton answer1;
    @FXML
    RadioButton answer2;
    @FXML
    RadioButton answer3;
    @FXML
    Button submitAnswer;
    @FXML
    Label chooseAnswerLabel;

    public QuizController() {
        // Default constructor
        this.score = 0;
    }

    public void setCategories (List<String> categories) {
        this.categories = categories;
    }

    public void populateCategorySelection(List<String> categories) {
        if (categorySelection != null && categories != null) {
            categorySelection.getItems().clear();
            categorySelection.getItems().addAll(categories);
        }
    }
    @FXML
    public void setQuestion() {
        if (!quizCards.isEmpty() && quizCards.size() >= 3) {
            if (index < quizCards.size()) {
                Card card = quizCards.get(index);
                options.add(card.getAnswer());
                correctAnswer = card.getAnswer();

                while (options.size() < 3) {
                    String newAnswer = quizCards.get(
                            this.random.nextInt(quizCards.size())
                    ).getAnswer();
                    if (!options.contains(newAnswer)) {
                        options.add(newAnswer);
                    }
                }
                Collections.shuffle(options);

                questionSlot.setText(quizCards.get(index).getQuestion());
                String random1 = options.removeFirst();
                String random2 = options.removeFirst();
                String random3 = options.removeFirst();
                answer1.setText(random3);
                answer2.setText(random1);
                answer3.setText(random2);
            } else {
                questionSlot.setText("Quiz Completed!\nYour score: " + this.score );
                answer1.setVisible(false);
                answer2.setVisible(false);
                answer3.setVisible(false);
                chooseAnswerLabel.setVisible(false);
                submitAnswer.setText("Back");
                submitAnswer.setOnAction(e -> {
                    try {
                        returnToQuiz(e);
                    } catch (IOException _) {
                        msg.severe("Error loading quizUI.fxml");
                    }
                });

            }
        } else {
        questionSlot.setText("Not enough questions in this category.");
        }
    }

    public void startQuiz(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/quizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        QuizController qc = fxmlLoader.getController();
        List<Card> cards = CardDao.getCardsBasedOnCategory(categorySelection.getValue(), currentUser.getUserId());
        qc.quizCards = cards;
        Collections.shuffle(qc.quizCards);
        qc.setQuestion();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void checkAnswer(javafx.event.ActionEvent actionEvent) {
    if (answer1.isSelected()) {
        if (answer1.getText().equals(correctAnswer)) {
            this.score++;
        }
        answer1.setSelected(false);
    }
    if (answer2.isSelected()) {
        if (answer2.getText().equals(correctAnswer)) {
            this.score++;
        }
        answer2.setSelected(false);
    }
    if (answer3.isSelected()) {
        if (answer3.getText().equals(correctAnswer)) {
            this.score++;
        }
        answer3.setSelected(false);
    }
    index++;
    setQuestion();
    }

    public void returnToQuiz(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createQuizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
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
        controller.setUserGreeting(currentUser.getUserName());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeSM = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/studyMaterialsUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLibrary(MouseEvent actionEvent) throws IOException {
        Locale localeL = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/libraryUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        LibraryController lc = fxmlLoader.getController();
        lc.setUser(currentUser);
        lc.getAllCards();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeCC = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        CcController cardCtrl = fxmlLoader.getController();
        cardCtrl.setUser(currentUser);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
