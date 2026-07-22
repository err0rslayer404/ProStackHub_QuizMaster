package model;

import java.util.List;

public class Question {

    private String category;
    private String text;
    private List<String> options; // index 0 = A, 1 = B, 2 = C, 3 = D
    private char correctAnswer;

    public Question(String category, String text, List<String> options, char correctAnswer) {
        this.category = category;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public String getOptionText(char letter) {
        int index = letter - 'A';
        return options.get(index);
    }

    public String getCorrectAnswerText() {
        return correctAnswer + ") " + getOptionText(correctAnswer);
    }

    public boolean isCorrect(char answer) {
        return Character.toUpperCase(answer) == correctAnswer;
    }
}
