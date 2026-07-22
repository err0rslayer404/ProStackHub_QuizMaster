package model;

import java.time.LocalDateTime;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    private String playerName;
    private String category;
    private int score;
    private int totalQuestions;
    private double percentage;
    private LocalDateTime playedOn;

    public LeaderboardEntry(String playerName, String category, int score,
                             int totalQuestions, double percentage, LocalDateTime playedOn) {
        this.playerName = playerName;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.playedOn = playedOn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    public LocalDateTime getPlayedOn() {
        return playedOn;
    }

    // Sorts highest percentage first, ties broken by higher score.
    @Override
    public int compareTo(LeaderboardEntry other) {
        int result = Double.compare(other.percentage, this.percentage);
        if (result != 0) {
            return result;
        }
        return Integer.compare(other.score, this.score);
    }

    public String toCsvLine() {
        return playerName + "|" + category + "|" + score + "|" + totalQuestions + "|"
                + String.format("%.2f", percentage) + "|" + playedOn;
    }

    public static LeaderboardEntry fromCsvLine(String line) {
        String[] parts = line.split("\\|");
        String name = parts[0].trim();
        String category = parts[1].trim();
        int score = Integer.parseInt(parts[2].trim());
        int total = Integer.parseInt(parts[3].trim());
        double percentage = Double.parseDouble(parts[4].trim());
        LocalDateTime playedOn = LocalDateTime.parse(parts[5].trim());
        return new LeaderboardEntry(name, category, score, total, percentage, playedOn);
    }
}
