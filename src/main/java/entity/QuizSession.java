package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizSession {
        private final List<Card> questions;
        private int questionIndex;
        private int score;
        private QuizQuestion currentQuestion;

        public QuizSession(List<Card> cards) {
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
            Random random = new Random();

            if (!questions.isEmpty()) {
                while (options.size() < 3) {
                    options.add(questions.get(
                            random.nextInt(questions.size())
                    ).getAnswer());
                }
            }
            Collections.shuffle(options);

            return new QuizQuestion(card.getQuestion(), options);
        }

        public boolean answer(String answer) {
            if (currentQuestion == null || !currentQuestion.getOptions().contains(answer)) {
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
