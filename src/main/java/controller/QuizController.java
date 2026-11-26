package controller;

import dao.CardDao;
import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;



public class QuizController {
    private static final Logger msg = Logger.getLogger(QuizController.class.getName());
    User currentUser;
    List<String> categories;
    private String correctAnswer;
    private int score;
    private int index = 0;
    private final List<String> options = new java.util.ArrayList<>();
    private List<Card> quizCards;
    private final Random random = new Random();
    private Stage stage;
    private Scene scene;
    ResourceBundle bundle = LanguageManager.getBundle();

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

    void showQuizAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(LanguageManager.getBundle().getString("ELEM_quiz_error"));
        alert.setHeaderText(LanguageManager.getBundle().getString("ELEM_quiz_error"));
        alert.setContentText(LanguageManager.getBundle().getString("ELEM_quiz_error2"));
        alert.showAndWait();
    }

    @FXML
    public void setQuestion() {
        if (quizCards.size() >= 3) {
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
                endQuiz();
            }

        }
    }

    public void endQuiz(){
        questionSlot.setText(bundle.getString("SUBHEADER_quiz_score") + this.score );
        answer1.setVisible(false);
        answer2.setVisible(false);
        answer3.setVisible(false);
        chooseAnswerLabel.setVisible(false);
        submitAnswer.setText(bundle.getString("ELEM_back"));
        submitAnswer.setOnAction(e -> {
            try {
                returnToQuiz(e);
            } catch (IOException _) {
                msg.severe("Error");
            }
        });
    }

    public void startQuiz(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/quizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        QuizController qc = fxmlLoader.getController();
        List<Card> cards = CardDao.getCardsBasedOnCategory(categorySelection.getValue(), currentUser.getUserId());
        qc.quizCards = cards;
        qc.currentUser = currentUser;
        Collections.shuffle(qc.quizCards);
        if (cards.size() < 3) {
            showQuizAlert();
            returnToQuiz(actionEvent);
        }
        else {
            qc.setQuestion();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void checkAnswer(ActionEvent actionEvent) {
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

    public void returnToQuiz(ActionEvent actionEvent) throws IOException {
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

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToCreateCards(actionEvent);
    }

    public void switchToLibrary(MouseEvent actionEvent) throws IOException {
        PageController.switchToLibrary(actionEvent);
    }

    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        PageController.switchToProfile(actionEvent);
    }

}
