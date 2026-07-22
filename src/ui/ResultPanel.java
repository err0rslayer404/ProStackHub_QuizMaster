package ui;

import model.QuestionOutcome;
import model.QuizResult;

import javax.swing.*;
import java.awt.*;

// Shows the final results of a finished quiz: the score summary at the
// top, and a scrollable review of every wrong or skipped question below.
public class ResultPanel extends JPanel {

    private JLabel summaryLabel;
    private JTextArea reviewArea;

    public ResultPanel(Runnable onPlayAgain, Runnable onViewLeaderboard, Runnable onExit) {
        setLayout(new BorderLayout(10, 10));

        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(summaryLabel, BorderLayout.NORTH);

        reviewArea = new JTextArea();
        reviewArea.setEditable(false);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        add(new JScrollPane(reviewArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> onPlayAgain.run());
        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.addActionListener(e -> onViewLeaderboard.run());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> onExit.run());

        buttonPanel.add(playAgainButton);
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showResult(QuizResult result) {
        summaryLabel.setText(String.format(
                "<html>%s - %s<br>Score: %d &nbsp; Correct: %d &nbsp; Incorrect: %d &nbsp; "
                        + "Missed: %d &nbsp; Percentage: %.2f%%</html>",
                result.getPlayerName(), result.getCategory(), result.getScore(),
                result.getCorrectCount(), result.getWrongCount(), result.getSkippedCount(),
                result.getPercentage()));

        StringBuilder review = new StringBuilder();

        if (!result.getWrongOutcomes().isEmpty()) {
            review.append("Questions you got wrong:\n\n");
            for (QuestionOutcome outcome : result.getWrongOutcomes()) {
                review.append("Q: ").append(outcome.getQuestion().getText()).append("\n");
                review.append("   Your answer   : ").append(outcome.getGivenAnswer()).append(") ")
                        .append(outcome.getQuestion().getOptionText(outcome.getGivenAnswer())).append("\n");
                review.append("   Correct answer: ").append(outcome.getQuestion().getCorrectAnswerText()).append("\n\n");
            }
        }

        if (!result.getSkippedOutcomes().isEmpty()) {
            review.append("Questions you skipped:\n\n");
            for (QuestionOutcome outcome : result.getSkippedOutcomes()) {
                review.append("Q: ").append(outcome.getQuestion().getText()).append("\n");
                review.append("   Correct answer: ").append(outcome.getQuestion().getCorrectAnswerText()).append("\n\n");
            }
        }

        if (review.length() == 0) {
            review.append("Perfect score! You answered every question correctly.");
        }

        reviewArea.setText(review.toString());
        reviewArea.setCaretPosition(0);
    }
}
