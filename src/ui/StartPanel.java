package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

// The first screen the player sees. Lets them type their name, pick a
// category from the ones found in the questions file, and either start
// a quiz or jump straight to the leaderboard.
public class StartPanel extends JPanel {

    private JTextField nameField;
    private JComboBox<String> categoryBox;

    public StartPanel(List<String> categories, BiConsumer<String, String> onStartQuiz, Runnable onViewLeaderboard) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("QuizMaster");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridy = 0;
        add(title, gbc);

        JLabel subtitle = new JLabel("A Quiz Practice Platform");
        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Your name:"), gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Category:"), gbc);

        categoryBox = new JComboBox<>(categories.toArray(new String[0]));
        gbc.gridx = 1;
        add(categoryBox, gbc);

        JButton startButton = new JButton("Start Quiz");
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name before starting.",
                        "Name required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String category = (String) categoryBox.getSelectedItem();
            onStartQuiz.accept(name, category);
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(startButton, gbc);

        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.addActionListener(e -> onViewLeaderboard.run());
        gbc.gridy = 5;
        add(leaderboardButton, gbc);
    }

    // Called when coming back to this screen after a quiz, so the name field is ready for another round.
    public void clearName() {
        nameField.setText("");
    }
}
