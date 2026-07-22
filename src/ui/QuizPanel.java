package ui;

import model.Question;
import model.QuestionOutcome;
import service.QuizSession;

import javax.swing.*;
import java.awt.*;

// Shows the current question of an in-progress QuizSession. The player
// picks an option (or clicks Skip), sees immediate feedback, then clicks
// Next to move on. When the session runs out of questions, onFinished
// is called so the calling code (QuizMasterFrame) can show the results.
public class QuizPanel extends JPanel {

    private JLabel progressLabel;
    private JLabel scoreLabel;
    private JTextArea questionArea;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JLabel feedbackLabel;
    private JButton submitButton;
    private JButton skipButton;
    private JButton nextButton;

    private QuizSession session;
    private Runnable onFinished;

    public QuizPanel(Runnable onFinished) {
        this.onFinished = onFinished;
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        progressLabel = new JLabel();
        scoreLabel = new JLabel();
        topPanel.add(progressLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionArea = new JTextArea(3, 40);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("SansSerif", Font.BOLD, 15));
        questionArea.setOpaque(false);
        centerPanel.add(questionArea);

        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        char label = 'A';
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
            label++;
        }

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(feedbackLabel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(e -> handleSubmit());
        skipButton = new JButton("Skip");
        skipButton.addActionListener(e -> handleAnswer(null));
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> handleNext());
        nextButton.setEnabled(false);

        buttonPanel.add(submitButton);
        buttonPanel.add(skipButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void startSession(QuizSession session) {
        this.session = session;
        showCurrentQuestion();
    }

    private void showCurrentQuestion() {
        Question question = session.getCurrentQuestion();
        progressLabel.setText("Question " + session.getCurrentQuestionNumber() + " of "
                + session.getTotalQuestions() + "  [" + question.getCategory() + "]");
        scoreLabel.setText("Score so far: " + session.getScoreSoFar());
        questionArea.setText(question.getText());

        char label = 'A';
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(label + ") " + question.getOptions().get(i));
            optionButtons[i].setEnabled(true);
            label++;
        }
        optionGroup.clearSelection();

        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(Color.BLACK);
        submitButton.setEnabled(true);
        skipButton.setEnabled(true);
        nextButton.setEnabled(false);
    }

    private void handleSubmit() {
        char selected = 0;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selected = (char) ('A' + i);
                break;
            }
        }
        if (selected == 0) {
            JOptionPane.showMessageDialog(this, "Please select an answer, or click Skip.",
                    "No answer selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        handleAnswer(selected);
    }

    private void handleAnswer(Character answer) {
        QuestionOutcome outcome = session.submitAnswer(answer);

        if (outcome.isSkipped()) {
            feedbackLabel.setForeground(Color.ORANGE.darker());
            feedbackLabel.setText("Skipped. Correct answer was: " + outcome.getQuestion().getCorrectAnswerText());
        } else if (outcome.isCorrect()) {
            feedbackLabel.setForeground(new Color(0, 140, 0));
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setForeground(Color.RED);
            feedbackLabel.setText("Incorrect. Correct answer was: " + outcome.getQuestion().getCorrectAnswerText());
        }

        scoreLabel.setText("Score so far: " + session.getScoreSoFar());

        for (JRadioButton button : optionButtons) {
            button.setEnabled(false);
        }
        submitButton.setEnabled(false);
        skipButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private void handleNext() {
        if (session.isFinished()) {
            onFinished.run();
        } else {
            showCurrentQuestion();
        }
    }
}
