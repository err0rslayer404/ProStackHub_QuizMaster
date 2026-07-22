import model.Question;
import service.Leaderboard;
import service.QuestionLoader;
import ui.QuizMasterFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Entry point of the application. Loads the questions and the leaderboard
// from disk, then opens the main window. All the actual screens and quiz
// logic live in the ui and service packages - this class only handles
// startup.
public class Main {

    private static final String QUESTIONS_FILE = "data/questions.csv";
    private static final String LEADERBOARD_FILE = "data/leaderboard.csv";

    public static void main(String[] args) {
        List<Question> questions;
        try {
            questions = QuestionLoader.loadQuestions(QUESTIONS_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load questions:\n" + e.getMessage(),
                    "QuizMaster - Startup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Leaderboard leaderboard;
        try {
            leaderboard = new Leaderboard(LEADERBOARD_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not load the leaderboard file. The leaderboard will not be available this session.",
                    "QuizMaster - Warning", JOptionPane.WARNING_MESSAGE);
            leaderboard = null;
        }

        List<String> categories = getCategories(questions);
        List<Question> finalQuestions = questions;
        Leaderboard finalLeaderboard = leaderboard;

        SwingUtilities.invokeLater(() -> {
            QuizMasterFrame frame = new QuizMasterFrame(finalQuestions, categories, finalLeaderboard);
            frame.setVisible(true);
        });
    }

    private static List<String> getCategories(List<Question> questions) {
        List<String> categories = new ArrayList<>();
        for (Question question : questions) {
            if (!categories.contains(question.getCategory())) {
                categories.add(question.getCategory());
            }
        }
        Collections.sort(categories);
        return categories;
    }
}
