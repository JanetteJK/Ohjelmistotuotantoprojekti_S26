package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestion {
    private final String question;
    private final List<String> options;

    QuizQuestion(String question, List<String> options) {
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
