package controller;

import entity.Card;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class QuizControllerTest {

    private QuizController controller;
    private final ResourceBundle bundle = LanguageManager.getBundle();

    @BeforeAll
    static void startJavaFxToolkit() {
        /*
         * Initializes the JavaFX toolkit so JavaFX controls can be created in tests.
         * JFXPanel is enough for headless controller-level testing.
         */
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        controller = new QuizController();

        controller.categorySelection = new ChoiceBox<>();
        controller.startQuizButton = new Button();
        controller.cardSlot = new javafx.scene.layout.GridPane();
        controller.questionSlot = new Label();
        controller.answer1 = new RadioButton();
        controller.answer2 = new RadioButton();
        controller.answer3 = new RadioButton();
        controller.submitAnswer = new Button();
    }

    @Test
    void constructorShouldInitializeScoreToZero() throws Exception {
        assertEquals(0, getPrivateInt("score"));
    }

    @Test
    void setCategoriesShouldStoreProvidedCategories() throws Exception {
        List<String> categories = List.of("Math", "History", "Science");

        controller.setCategories(categories);

        assertSame(categories, getPrivateField("categories"));
    }

    @Test
    void populateCategorySelectionShouldAddAllCategories() {
        List<String> categories = List.of("Math", "History", "Science");

        controller.populateCategorySelection(categories);

        assertEquals(categories, controller.categorySelection.getItems());
    }

    @Test
    void populateCategorySelectionShouldClearExistingItemsBeforeAddingNewCategories() {
        controller.categorySelection.getItems().addAll("Old category", "Another old category");

        controller.populateCategorySelection(List.of("New category"));

        assertEquals(List.of("New category"), controller.categorySelection.getItems());
    }

    @Test
    void populateCategorySelectionShouldDoNothingWhenCategoriesAreNull() {
        controller.categorySelection.getItems().add("Existing category");

        controller.populateCategorySelection(null);

        assertEquals(List.of("Existing category"), controller.categorySelection.getItems());
    }

    @Test
    void populateCategorySelectionShouldDoNothingWhenChoiceBoxIsNull() {
        controller.categorySelection = null;

        assertDoesNotThrow(() -> controller.populateCategorySelection(List.of("Math")));
    }

    @Test
    void setQuestionShouldShowNotEnoughQuestionsMessageWhenQuizCardsAreEmpty() throws Exception {
        setPrivateField("quizCards", List.of());

        controller.setQuestion();

        assertEquals("Not enough questions in this category.", controller.questionSlot.getText());
    }

    @Test
    void setQuestionShouldShowNotEnoughQuestionsMessageWhenQuizCardsAreFewerThanThree() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2")
        ));

        controller.setQuestion();

        assertEquals("Not enough questions in this category.", controller.questionSlot.getText());
    }

    @Test
    void setQuestionShouldDisplayCurrentQuestionAndThreeAnswerOptions() throws Exception {
        Card firstCard = card("What is 2 + 2?", "4");
        Card secondCard = card("Capital of Finland?", "Helsinki");
        Card thirdCard = card("Color of the sky?", "Blue");

        setPrivateField("quizCards", List.of(firstCard, secondCard, thirdCard));

        controller.setQuestion();

        List<String> displayedAnswers = displayedAnswers();

        assertEquals("What is 2 + 2?", controller.questionSlot.getText());
        assertEquals(3, displayedAnswers.size());
        assertTrue(displayedAnswers.contains("4"));
        assertTrue(displayedAnswers.contains("Helsinki"));
        assertTrue(displayedAnswers.contains("Blue"));
        assertEquals("4", getPrivateField("correctAnswer"));
    }

    @Test
    void setQuestionShouldUseCardAtCurrentIndex() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));
        setPrivateField("index", 1);

        controller.setQuestion();

        assertEquals("Question 2", controller.questionSlot.getText());
        assertEquals("Answer 2", getPrivateField("correctAnswer"));
        assertTrue(displayedAnswers().contains("Answer 2"));
    }

    @Test
    void setQuestionShouldShowCompletionStateWhenIndexIsPastLastCard() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));
        setPrivateField("index", 3);
        setPrivateField("score", 2);

        setPrivateField("chooseAnswerLabel", mock(Label.class));

        controller.setQuestion();

        assertEquals(
                bundle.getString("SUBHEADER_quiz_score") + 2,
                controller.questionSlot.getText()
        );
        assertFalse(controller.answer1.isVisible());
        assertFalse(controller.answer2.isVisible());
        assertFalse(controller.answer3.isVisible());
        assertEquals(
                bundle.getString("ELEM_back"),
                controller.submitAnswer.getText()
        );
        assertNotNull(controller.submitAnswer.getOnAction());
    }

    @Test
    void checkAnswerShouldIncreaseScoreWhenSelectedAnswerIsCorrect() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));

        controller.setQuestion();

        String correctAnswer = (String) getPrivateField("correctAnswer");
        selectAnswerByText(correctAnswer);

        controller.checkAnswer(null);

        assertEquals(1, getPrivateInt("score"));
        assertEquals(1, getPrivateInt("index"));
    }

    @Test
    void checkAnswerShouldNotIncreaseScoreWhenSelectedAnswerIsWrong() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));

        controller.setQuestion();

        String correctAnswer = (String) getPrivateField("correctAnswer");
        selectFirstWrongAnswer(correctAnswer);

        controller.checkAnswer(null);

        assertEquals(0, getPrivateInt("score"));
        assertEquals(1, getPrivateInt("index"));
    }

    @Test
    void checkAnswerShouldClearSelectedAnswerAfterSubmitting() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));

        controller.setQuestion();
        controller.answer1.setSelected(true);

        controller.checkAnswer(null);

        assertFalse(controller.answer1.isSelected());
        assertFalse(controller.answer2.isSelected());
        assertFalse(controller.answer3.isSelected());
    }

    @Test
    void checkAnswerShouldAdvanceToNextQuestion() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));

        controller.setQuestion();

        controller.checkAnswer(null);

        assertEquals(1, getPrivateInt("index"));
        assertEquals("Question 2", controller.questionSlot.getText());
    }

    @Test
    void checkAnswerShouldCompleteQuizAfterLastQuestion() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));
        setPrivateField("index", 2);
        setPrivateField("chooseAnswerLabel", mock(Label.class));

        controller.setQuestion();

        String correctAnswer = (String) getPrivateField("correctAnswer");
        selectAnswerByText(correctAnswer);

        controller.checkAnswer(null);

        assertEquals(3, getPrivateInt("index"));
        assertEquals(1, getPrivateInt("score"));
        assertEquals(
                bundle.getString("SUBHEADER_quiz_score") + 1,
                controller.questionSlot.getText()
        );
        assertEquals(
                bundle.getString("ELEM_back"),
                controller.submitAnswer.getText()
        );
        assertFalse(controller.answer1.isVisible());
        assertFalse(controller.answer2.isVisible());
        assertFalse(controller.answer3.isVisible());
    }

    @Test
    void checkAnswerShouldNotIncreaseScoreWhenNoAnswerIsSelected() throws Exception {
        setPrivateField("quizCards", List.of(
                card("Question 1", "Answer 1"),
                card("Question 2", "Answer 2"),
                card("Question 3", "Answer 3")
        ));

        controller.setQuestion();

        controller.checkAnswer(null);

        assertEquals(0, getPrivateInt("score"));
        assertEquals(1, getPrivateInt("index"));
    }

    private Card card(String question, String answer) {
        return new Card(question, answer, "Test category", 1);
    }

    private List<String> displayedAnswers() {
        return List.of(
                controller.answer1.getText(),
                controller.answer2.getText(),
                controller.answer3.getText()
        );
    }

    private void selectAnswerByText(String text) {
        if (controller.answer1.getText().equals(text)) {
            controller.answer1.setSelected(true);
        } else if (controller.answer2.getText().equals(text)) {
            controller.answer2.setSelected(true);
        } else if (controller.answer3.getText().equals(text)) {
            controller.answer3.setSelected(true);
        } else {
            fail("Expected answer option was not displayed: " + text);
        }
    }

    private void selectFirstWrongAnswer(String correctAnswer) {
        if (!controller.answer1.getText().equals(correctAnswer)) {
            controller.answer1.setSelected(true);
        } else if (!controller.answer2.getText().equals(correctAnswer)) {
            controller.answer2.setSelected(true);
        } else if (!controller.answer3.getText().equals(correctAnswer)) {
            controller.answer3.setSelected(true);
        } else {
            fail("No wrong answer option was available.");
        }
    }

    private Object getPrivateField(String fieldName) throws Exception {
        Field field = QuizController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(controller);
    }

    private int getPrivateInt(String fieldName) throws Exception {
        Field field = QuizController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(controller);
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = QuizController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
}