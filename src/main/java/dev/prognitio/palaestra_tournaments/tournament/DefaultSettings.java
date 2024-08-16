package dev.prognitio.palaestra_tournaments.tournament;

public class DefaultSettings {

    //question config
    public boolean multipleChoice; //Whether to allow multiple choice questions
    public boolean questionLocking; //Whether one competitor completing a question blocks other competitors from completing it
    public int pointsPerCorrect;
    public int pointsPerWrong;
    public int pointsPerSkipped;
    public double pointScale; //points = pointsPerCorrect + (point Scale * attemptNum)

    public DefaultSettings(boolean multipleChoice, boolean questionLocking, int pointsPerCorrect, int pointsPerWrong, int pointsPerSkipped, double pointScale) {
        this.multipleChoice = multipleChoice;
        this.questionLocking = questionLocking;
        this.pointsPerCorrect = pointsPerCorrect;
        this.pointsPerWrong = pointsPerWrong;
        this.pointsPerSkipped = pointsPerSkipped;
        this.pointScale = pointScale;
    }


    public String toString() {
        String output = "";

        output += "DEFAULT SETTINGS: \n";
        output += "multipleChoice: " + multipleChoice + "\n";
        output += "questionLocking: " + questionLocking + "\n";
        output += "pointsPerCorrect: " + pointsPerCorrect + "\n";
        output += "pointsPerWrong: " + pointsPerWrong + "\n";
        output += "pointsPerSkipped: " + pointsPerSkipped + "\n";
        output += "pointScale: " + pointScale + "\n";

        return output;
    }
}
