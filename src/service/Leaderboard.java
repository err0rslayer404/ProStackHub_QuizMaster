package service;

import model.LeaderboardEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {

    private String filePath;
    private List<LeaderboardEntry> entries;

    public Leaderboard(String filePath) throws IOException {
        this.filePath = filePath;
        this.entries = new ArrayList<>();
        loadFromFile();
        Collections.sort(entries);
    }

    private void loadFromFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // no scores saved yet, that's fine on first run
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    entries.add(LeaderboardEntry.fromCsvLine(line));
                } catch (Exception e) {
                    System.out.println("Skipping a corrupted leaderboard line.");
                }
            }
        }
    }

    public void addEntry(LeaderboardEntry entry) throws IOException {
        entries.add(entry);
        Collections.sort(entries);
        saveToFile();
    }

    private void saveToFile() throws IOException {
        File file = new File(filePath);
        File folder = file.getParentFile();
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# name|category|score|total|percentage|dateTime");
            writer.newLine();
            for (LeaderboardEntry entry : entries) {
                writer.write(entry.toCsvLine());
                writer.newLine();
            }
        }
    }

    public List<LeaderboardEntry> getTopEntries(int limit) {
        List<LeaderboardEntry> top = new ArrayList<>();
        for (int i = 0; i < entries.size() && i < limit; i++) {
            top.add(entries.get(i));
        }
        return top;
    }
}
