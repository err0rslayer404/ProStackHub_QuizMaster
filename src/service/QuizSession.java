package service;

import model.Question;
import model.QuestionOutcome;
import model.QuizResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Keeps track of one quiz attempt in progress: which question we're on,
// the score so far, and every answer given. The old Quiz class asked all
// its questions in one blocking loop using Scanner, which only works for
// a console app. A GUI has to ask one question, wait for a button click,
// then move to the next - so this class exposes the quiz one question at
// a time instead, and the UI decides when to move on.
public class QuizSession {

    private static final int MAX_QUESTIONS = 10;

    private String playerName;
    private String category;
    private List<Question> questions;
    private List<QuestionOutcome> outcomes = new ArrayList<>();
    private int currentIndex = 0;

    public QuizSession(String playerName, String category, List<Question> categoryQuestions) {
        this.playerName = playerName;
        this.category = category;

        List<Question> shuffled = new ArrayList<>(categoryQuestions);
        Collections.shuffle(shuffled);
        int count = Math.min(MAX_QUESTIONS, shuffled.size());
        this.questions = shuffled.subList(0, count);
    }

    public Question getCurrentQuestion() {
        return questions.get(currentIndex);
    }

    public int getCurrentQuestionNumber() {
        return currentIndex + 1;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public boolean isFinished() {
        return currentIndex >= questions.size();
    }

    // Pass null for answer if the player skipped the question.
    public QuestionOutcome submitAnswer(Character answer) {
        Question question = getCurrentQuestion();
        boolean skipped = (answer == null);
        boolean correct = !skipped && question.isCorrect(answer);

        QuestionOutcome outcome = new QuestionOutcome(question, answer, skipped, correct);
        outcomes.add(outcome);
        currentIndex++;
        return outcome;
    }

    public int getScoreSoFar() {
        int score = 0;
        for (QuestionOutcome outcome : outcomes) {
            if (outcome.isCorrect()) {
                score += 10;
            }
        }
        return score;
    }

    public QuizResult buildResult() {
        return new QuizResult(playerName, category, outcomes);
    }
}
