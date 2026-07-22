package ui;

import model.LeaderboardEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Shows the saved high scores in a simple table, best score first.
public class LeaderboardPanel extends JPanel {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
    private static final String[] COLUMN_NAMES = {"Rank", "Player", "Category", "Score", "Percentage", "Date"};

    private DefaultTableModel tableModel;

    public LeaderboardPanel(Runnable onBack) {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Leaderboard - Top Scores", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refresh(List<LeaderboardEntry> entries) {
        tableModel.setRowCount(0);

        if (entries.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "No scores yet - be the first to play!", "", "", "", ""});
            return;
        }

        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            tableModel.addRow(new Object[]{
                    rank,
                    entry.getPlayerName(),
                    entry.getCategory(),
                    entry.getScore(),
                    String.format("%.1f%%", entry.getPercentage()),
                    entry.getPlayedOn().format(DATE_FORMAT)
            });
            rank++;
        }
    }
}
