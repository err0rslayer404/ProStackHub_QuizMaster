package ui;

import model.LeaderboardEntry;
import model.Question;
import model.QuizResult;
import service.Leaderboard;
import service.QuizSession;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// The main application window. Holds the questions and the leaderboard,
// and switches between the four screens (start, quiz, result, leaderboard)
// using a CardLayout. This class is the only place that knows about all
// four screens - each screen only knows about itself and reports back
// through the callbacks passed into its constructor.
public class QuizMasterFrame extends JFrame {

    private static final int LEADERBOARD_LIMIT = 10;

    private List<Question> allQuestions;
    private Leaderboard leaderboard; // null if the leaderboard file could not be loaded

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardContainer = new JPanel(cardLayout);

    private StartPanel startPanel;
    private QuizPanel quizPanel;
    private ResultPanel resultPanel;
    private LeaderboardPanel leaderboardPanel;

    private QuizSession currentSession;

    public QuizMasterFrame(List<Question> allQuestions, List<String> categories, Leaderboard leaderboard) {
        super("QuizMaster - A Quiz Practice Platform");
        this.allQuestions = allQuestions;
        this.leaderboard = leaderboard;

        startPanel = new StartPanel(categories, this::startQuiz, this::showLeaderboard);
        quizPanel = new QuizPanel(this::onQuizFinished);
        resultPanel = new ResultPanel(this::showStart, this::showLeaderboard, this::exitApp);
        leaderboardPanel = new LeaderboardPanel(this::showStart);

        cardContainer.add(startPanel, "start");
        cardContainer.add(quizPanel, "quiz");
        cardContainer.add(resultPanel, "result");
        cardContainer.add(leaderboardPanel, "leaderboard");

        setContentPane(cardContainer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(null);

        showStart();
    }

    private void showStart() {
        startPanel.clearName();
        cardLayout.show(cardContainer, "start");
    }

    private void startQuiz(String playerName, String category) {
        List<Question> categoryQuestions = filterByCategory(category);
        currentSession = new QuizSession(playerName, category, categoryQuestions);
        quizPanel.startSession(currentSession);
        cardLayout.show(cardContainer, "quiz");
    }

    private void onQuizFinished() {
        QuizResult result = currentSession.buildResult();
        saveToLeaderboard(result);
        resultPanel.showResult(result);
        cardLayout.show(cardContainer, "result");
    }

    private void saveToLeaderboard(QuizResult result) {
        if (leaderboard == null) {
            return;
        }
        LeaderboardEntry entry = new LeaderboardEntry(result.getPlayerName(), result.getCategory(),
                result.getScore(), result.getTotalQuestions(), result.getPercentage(), LocalDateTime.now());
        try {
            leaderboard.addEntry(entry);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save your result to the leaderboard.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showLeaderboard() {
        if (leaderboard == null) {
            JOptionPane.showMessageDialog(this, "The leaderboard is not available this session.",
                    "Leaderboard unavailable", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        leaderboardPanel.refresh(leaderboard.getTopEntries(LEADERBOARD_LIMIT));
        cardLayout.show(cardContainer, "leaderboard");
    }

    private void exitApp() {
        dispose();
        System.exit(0);
    }

    private List<Question> filterByCategory(String category) {
        List<Question> result = new ArrayList<>();
        for (Question question : allQuestions) {
            if (question.getCategory().equals(category)) {
                result.add(question);
            }
        }
        return result;
    }
}
