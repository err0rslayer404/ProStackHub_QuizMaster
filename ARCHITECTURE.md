# QuizMaster - Architecture

This document explains the structure of the QuizMaster application, the responsibility of each part of the project, and how the different components work together.

The project is organized into three main areas: **model**, **service**, and **ui**. This separation helps keep the data, application logic, and user interface independent from each other.

---

## 1. Project Structure

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
├── ARCHITECTURE.md
```

### Package Responsibilities

- **model**  
  Contains the classes that represent the data used by the application.

- **service**  
  Contains the main application logic, such as loading questions, managing a quiz session, and handling the leaderboard.

- **ui**  
  Contains the Java Swing components that display the application and handle user interaction.

- **data**  
  Contains the question bank and the leaderboard data.

---

## 2. Design Decisions

### Separation of Responsibilities

The application separates data, logic, and user interface responsibilities.

The `model` classes represent application data. The `service` classes perform the main operations of the application, while the `ui` classes handle the graphical interface.

This structure makes the project easier to understand and maintain. Changes to one part of the application can usually be made without affecting unrelated parts.

For example, the question-loading logic is handled by `QuestionLoader`, while the UI does not directly read the questions file.

---

### Java Swing for the User Interface

The application uses **Java Swing** to provide a desktop graphical user interface.

Swing was chosen because it is included in the standard Java Development Kit and does not require any external UI libraries or frameworks. This keeps the application simple to set up while providing a proper interface for the user.

The UI contains separate screens for:

- Starting a quiz
- Answering questions
- Viewing results
- Viewing the leaderboard

---

### Model Classes

The model classes mainly represent application data.

For example:

- `Question` represents a multiple-choice question.
- `QuestionOutcome` stores what happened for one question, such as whether it was answered correctly, incorrectly, or skipped.
- `QuizResult` represents the result of a completed quiz.
- `LeaderboardEntry` represents one entry in the leaderboard.

Keeping these classes separate from the UI makes the data easier to work with and keeps the application logic more organized.

---

### Quiz Session Design

The `QuizSession` class manages one quiz attempt.

Instead of making the UI responsible for the complete quiz logic, `QuizSession` keeps track of:

- The questions selected for the quiz
- The current question
- The answers given by the player
- The score
- The result of each question

The UI interacts with the session one question at a time. This works well with a graphical interface because the application waits for the user to click a button before moving to the next step.

---

### Result Handling

The `QuizResult` class stores the outcomes of the questions answered during a quiz.

The final score, percentage, and counts of correct, wrong, and skipped questions are calculated from these outcomes.

This keeps the result information in one place and allows the result screen to display the information in a clear format.

---

### Single Window with Multiple Screens

The application uses one main `JFrame` managed by `QuizMasterFrame`.

The different screens are displayed inside this window:

- `StartPanel`
- `QuizPanel`
- `ResultPanel`
- `LeaderboardPanel`

A `CardLayout` is used to switch between these screens.

This provides a simple flow for the user without opening multiple application windows.

---

## 3. Class Responsibilities

| Class | Responsibility |
|---|---|
| `Main` | Starts the application, loads the question and leaderboard data, and opens the main window. |
| `Question` | Represents one multiple-choice question and stores its category, text, options, and correct answer. |
| `QuestionOutcome` | Stores the result of one question, including the answer given and whether it was correct or skipped. |
| `QuizResult` | Represents the result of a completed quiz and provides score-related information. |
| `LeaderboardEntry` | Represents one leaderboard record and contains the information needed to save, load, and rank an entry. |
| `QuestionLoader` | Reads questions from `questions.csv` and converts valid records into `Question` objects. |
| `QuizSession` | Manages one quiz attempt, including question selection, answers, skipped questions, and score tracking. |
| `Leaderboard` | Loads, stores, sorts, and saves leaderboard entries. |
| `QuizMasterFrame` | Acts as the main application window and controls navigation between the different screens. |
| `StartPanel` | Allows the user to enter their name, select a category, and start a quiz. |
| `QuizPanel` | Displays questions, answer options, feedback, and the current score. |
| `ResultPanel` | Displays the final quiz result and reviews incorrect or skipped questions. |
| `LeaderboardPanel` | Displays the leaderboard entries to the user. |

---

## 4. How the Components Interact

The main flow of the application is:

```text
Main
 │
 ├── Loads questions
 │       │
 │       ▼
 │   QuestionLoader
 │       │
 │       ▼
 │   List<Question>
 │
 ├── Loads leaderboard data
 │       │
 │       ▼
 │   Leaderboard
 │
 ▼
QuizMasterFrame
 │
 ├── StartPanel
 │      │
 │      └── User selects name and category
 │
 ├── QuizPanel
 │      │
 │      └── QuizSession manages the quiz
 │
 ├── ResultPanel
 │      │
 │      └── Displays QuizResult
 │      │
 │      └── Saves result to Leaderboard
 │
 └── LeaderboardPanel
        │
        └── Displays saved leaderboard entries
```

### Typical Quiz Flow

1. `Main` starts the application.
2. `QuestionLoader` loads the questions from `data/questions.csv`.
3. `Leaderboard` loads previous results from `data/leaderboard.csv`, if the file exists.
4. `QuizMasterFrame` opens the start screen.
5. The user enters their name and selects a category.
6. A new `QuizSession` is created for the selected category.
7. `QuizPanel` displays the questions one at a time.
8. The user answers or skips each question.
9. `QuizSession` records the outcome and updates the score.
10. After the last question, a `QuizResult` is created.
11. `ResultPanel` displays the final result.
12. The result is saved to the leaderboard.
13. The user can play again, view the leaderboard, or exit.

---

## 5. User Interface Flow

The application uses a simple screen-based flow:

```text
Start Screen
     │
     ▼
Quiz Screen
     │
     ▼
Result Screen
     │
     ├── Play Again ──────► Start Screen
     │
     ├── Leaderboard ─────► Leaderboard Screen
     │                              │
     │                              ▼
     │                        Start Screen
     │
     └── Exit
```

`QuizMasterFrame` manages the navigation between these screens.

The individual panels focus on their own responsibilities and communicate actions back to the main frame through callbacks. This prevents the panels from directly controlling one another.

---

## 6. Data Files

The application uses two plain text data files.

### `data/questions.csv`

This file contains the question bank.

```text
Category|Question|OptionA|OptionB|OptionC|OptionD|CorrectAnswer
```

Example:

```text
Java Basics|Which keyword is used to define a constant in Java?|const|final|static|immutable|B
```

The correct answer must be `A`, `B`, `C`, or `D`.

New questions and categories can be added by adding new records to the file. The Java source code does not need to be changed.

---

### `data/leaderboard.csv`

This file is created automatically when a quiz result is saved.

```text
playerName|category|score|totalQuestions|percentage|dateTime
```

The leaderboard is sorted by:

1. Highest percentage
2. Highest score when percentages are equal

If the leaderboard file does not exist, the application starts with an empty leaderboard.

---

## 7. Error Handling

The application handles common data problems without unnecessarily stopping the entire application.

For example:

- Invalid question records can be skipped while loading the question file.
- A missing leaderboard file starts with an empty leaderboard.
- Invalid leaderboard records can be ignored instead of crashing the application.

However, if the question file cannot be loaded and no questions are available, the application cannot start a quiz and displays an error to the user.

---

## 8. Possible Future Improvements

The current application meets the requirements of the task. Some possible future improvements include:

- Adding difficulty levels to questions
- Adding a timer for each question
- Creating separate leaderboards for different categories
- Adding unit tests for the model and service classes
- Adding more categories and questions

---

## Conclusion

QuizMaster uses a simple separation between data models, application services, and the user interface.

This structure keeps the project organized and makes each part easier to understand, maintain, and improve. The application also keeps the quiz logic separate from the Swing interface, which allows the core functionality to be changed or tested without depending directly on the UI.