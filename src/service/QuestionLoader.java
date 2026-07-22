package service;

import model.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {

    // File format: Category|Question|OptionA|OptionB|OptionC|OptionD|CorrectAnswer
    // Lines starting with '#' and blank lines are ignored.
    public static List<Question> loadQuestions(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Questions file not found: " + filePath);
        }

        List<Question> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length != 7) {
                    System.out.println("Skipping line " + lineNumber + ": wrong number of fields.");
                    continue;
                }

                String category = parts[0].trim();
                String text = parts[1].trim();
                List<String> options = new ArrayList<>();
                options.add(parts[2].trim());
                options.add(parts[3].trim());
                options.add(parts[4].trim());
                options.add(parts[5].trim());

                char correctAnswer = Character.toUpperCase(parts[6].trim().charAt(0));
                if (correctAnswer < 'A' || correctAnswer > 'D') {
                    System.out.println("Skipping line " + lineNumber + ": correct answer must be A, B, C or D.");
                    continue;
                }

                questions.add(new Question(category, text, options, correctAnswer));
            }
        }

        if (questions.isEmpty()) {
            throw new IOException("No questions found in file: " + filePath);
        }

        return questions;
    }
}
