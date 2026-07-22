# QuizMaster — A Java Quiz Practice Application

QuizMaster is a Java-based desktop quiz application developed as part of my Java Programming internship task at ProStackHub.

The application allows users to select a quiz category, answer multiple-choice questions, view their score, and review the questions they answered incorrectly or skipped. The application also maintains a leaderboard so that previous quiz results can be viewed later.

The application uses Java Swing for the graphical user interface and does not require any external UI libraries.

---

## Features

- Select a quiz category before starting
- Answer multiple-choice questions
- Get immediate feedback after submitting an answer
- Skip questions if needed
- Track the score during the quiz
- View the final score and percentage
- Review incorrect and skipped questions
- View the correct answer for missed questions
- Save quiz results to a leaderboard
- View the top 10 leaderboard entries
- Load questions from a CSV file
- Add new questions without changing the source code

---

## Project Structure

```text
QuizMaster/
├── src/
│   ├── Main.java
│   │
│   ├── model/
│   │   ├── Question.java
│   │   ├── QuestionOutcome.java
│   │   ├── QuizResult.java
│   │   └── LeaderboardEntry.java
│   │
│   ├── service/
│   │   ├── QuestionLoader.java
│   │   ├── QuizSession.java
│   │   └── Leaderboard.java
│   │
│   └── ui/
│       ├── QuizMasterFrame.java
│       ├── StartPanel.java
│       ├── QuizPanel.java
│       ├── ResultPanel.java
│       └── LeaderboardPanel.java
│
├── data/
│   ├── questions.csv
│   └── leaderboard.csv
│
├── README.md
└── ARCHITECTURE.md
```

The project is divided into different packages to keep the code organized:

- **model** — Contains classes that represent application data.
- **service** — Contains the main application logic, including question loading, quiz management, and leaderboard handling.
- **ui** — Contains the Java Swing screens used by the user.
- **data** — Contains the question file and the leaderboard file.

For more details about the project structure and how the components interact, see [ARCHITECTURE.md](ARCHITECTURE.md).

---


## Requirements

- Java Development Kit (JDK) 11 or higher
- Java 21 was used during development and testing


---

## How to Use the Application

### 1. Start Screen

Enter your name and select a quiz category. After that, click **Start Quiz** to begin.

You can also open the leaderboard before starting a quiz.

### 2. Quiz Screen

Each question is displayed with four answer options.

Select an answer and click **Submit Answer**. The application will show whether the answer was correct and display the current score.

If you do not want to answer a question, you can click **Skip**.

### 3. Result Screen

After completing the quiz, the result screen displays:

- Final score
- Percentage
- Correct answers
- Wrong answers
- Skipped questions

The application also shows a review of the questions that were answered incorrectly or skipped, along with the correct answers.

### 4. Leaderboard

The leaderboard stores the results of completed quizzes.

The top 10 scores are displayed and ranked according to the percentage achieved. If two players have the same percentage, the higher score is ranked first.

---

## Question File

Questions are stored in:

```text
data/questions.csv
```

The file uses the following format:

```text
Category|Question|OptionA|OptionB|OptionC|OptionD|CorrectAnswer
```

Example:

```text
Java Basics|Which keyword is used to define a constant in Java?|const|final|static|immutable|B
```

The correct answer should be one of:

```text
A, B, C, or D
```

To add more questions, add new lines to the CSV file. No changes to the Java source code are required.

Lines starting with `#` and blank lines are ignored.

---

## Leaderboard

Quiz results are saved in:

```text
data/leaderboard.csv
```

The file is created automatically when a quiz result is saved.

Each entry contains:

```text
playerName|category|score|totalQuestions|percentage|dateTime
```

The leaderboard is sorted by:

1. Highest percentage
2. Highest score if the percentage is the same

If the leaderboard file does not exist when the application starts, the application starts with an empty leaderboard.

---

## Future Improvements

Some features that could be added in the future include:

- Difficulty levels for questions
- A timer for each question
- Separate leaderboards for different categories
- Unit tests for the service and model classes
- More quiz categories

---

## Architecture

The project follows a simple separation of responsibilities:

- **Model** classes represent the data used by the application.
- **Service** classes handle the main quiz and leaderboard logic.
- **UI** classes handle user interaction and display the application screens.

For a detailed explanation of the architecture and the interaction between the different components, see:

```text
ARCHITECTURE.md
```