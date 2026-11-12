package controller;

import dao.CardDao;
import dao.QuizDao;
import entity.Card;
import entity.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizController {
    QuizDao qd;
    private static final String PLACEHOLDER_CATEGORY = "default";

    public QuizController(QuizDao qd) {
        this.qd = qd;
    }

    public Quiz getQuiz(String category, int userId) {
        if (category == null || category.isBlank()) {
            category = PLACEHOLDER_CATEGORY;
        }

        return QuizDao.createQuiz(category, userId);
    }

    public QuizSession openQuiz(String category, int userId) {
        if (category == null || category.isBlank()) {
            category = PLACEHOLDER_CATEGORY;
        }

        final List<Card> cards = CardDao.getCardsBasedOnCategory(category, userId);
        return new QuizSession(cards);
    }

    public static class QuizSession {
        private final List<Card> questions;
        private int questionIndex;
        private int score;
        private QuizQuestion currentQuestion;
        private QuizSession(List<Card> cards) {
            questions = new ArrayList<>(cards);
            Collections.shuffle(questions);
        }

        public QuizQuestion nextQuestion() {
            if (currentQuestion != null || questionIndex >= questions.size()) {
                return null;
            }

            Card card = questions.get(questionIndex);
            List<String> options = new ArrayList<>();
            options.add(card.getAnswer());

            if (!questions.isEmpty()) {
                Random random = new Random();
                while (options.size() < 3) {
                    options.add(questions.get(
                            random.nextInt(questions.size())
                    ).getAnswer());
                }
            }
            Collections.shuffle(options);
            currentQuestion = new QuizQuestion(card.getQuestion(), options);
            return currentQuestion;
        }

        public boolean answer(String answer) {
            if (currentQuestion == null || !currentQuestion.options.contains(answer)) {
                return false;
            }

            if (answer.equals(questions.get(questionIndex).getAnswer())) {
                score++;
            }
            currentQuestion = null;
            questionIndex++;
            return true;
        }

        public int getScore() {
            return score;
        }

        public boolean isFinished() {
            return questionIndex >= questions.size() && currentQuestion == null;
        }
    }

    public static class QuizQuestion {
        private final String question;
        private final List<String> options;

        private QuizQuestion(String question, List<String> options) {
            this.question = question;
            this.options = Collections.unmodifiableList(new ArrayList<>(options));
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
