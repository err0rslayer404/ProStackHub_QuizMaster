package model;

import java.util.ArrayList;
import java.util.List;

// The outcome of one finished quiz attempt. Everything (score, percentage,
// correct/wrong/skipped counts) is worked out from the list of question
// outcomes, so there is only one place that holds the real data - no
// separate counters that could get out of sync with it.
public class QuizResult {

    private static final int POINTS_PER_CORRECT_ANSWER = 10;

    private String playerName;
    private String category;
    private List<QuestionOutcome> outcomes;

    public QuizResult(String playerName, String category, List<QuestionOutcome> outcomes) {
        this.playerName = playerName;
        this.category = category;
        this.outcomes = outcomes;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalQuestions() {
        return outcomes.size();
    }

    public int getCorrectCount() {
        int count = 0;
        for (QuestionOutcome outcome : outcomes) {
            if (outcome.isCorrect()) {
                count++;
            }
        }
        return count;
    }

    public int getSkippedCount() {
        int count = 0;
        for (QuestionOutcome outcome : outcomes) {
            if (outcome.isSkipped()) {
                count++;
            }
        }
        return count;
    }

    public int getWrongCount() {
        return getTotalQuestions() - getCorrectCount() - getSkippedCount();
    }

    public int getScore() {
        return getCorrectCount() * POINTS_PER_CORRECT_ANSWER;
    }

    public double getPercentage() {
        if (getTotalQuestions() == 0) {
            return 0.0;
        }
        return getCorrectCount() * 100.0 / getTotalQuestions();
    }

    // Questions the player answered but got wrong (used on the results screen).
    public List<QuestionOutcome> getWrongOutcomes() {
        List<QuestionOutcome> wrong = new ArrayList<>();
        for (QuestionOutcome outcome : outcomes) {
            if (!outcome.isSkipped() && !outcome.isCorrect()) {
                wrong.add(outcome);
            }
        }
        return wrong;
    }

    // Questions the player skipped (used on the results screen).
    public List<QuestionOutcome> getSkippedOutcomes() {
        List<QuestionOutcome> skipped = new ArrayList<>();
        for (QuestionOutcome outcome : outcomes) {
            if (outcome.isSkipped()) {
                skipped.add(outcome);
            }
        }
        return skipped;
    }
}
