package model;

// What happened on one question during a quiz: the question itself,
// the answer the player gave (null if they skipped it), and whether
// it was correct.
public class QuestionOutcome {

    private Question question;
    private Character givenAnswer;
    private boolean skipped;
    private boolean correct;

    public QuestionOutcome(Question question, Character givenAnswer, boolean skipped, boolean correct) {
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.skipped = skipped;
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public Character getGivenAnswer() {
        return givenAnswer;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public boolean isCorrect() {
        return correct;
    }
}
