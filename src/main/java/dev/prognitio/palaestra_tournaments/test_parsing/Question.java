package dev.prognitio.palaestra_tournaments.test_parsing;

import java.util.ArrayList;

public class Question {

    public String questionBody;
    public String answer;
    public ArrayList<String> alternateAnswers;

    public Question(String questionBody, String answer, ArrayList<String> alternateAnswers) {
        this.questionBody = questionBody;
        this.answer = answer;
        this.alternateAnswers = alternateAnswers;
    }


    public boolean isCorrect(String attempt) {
        try {
            double answerAsDouble = Double.parseDouble(answer);
            double attemptAsDouble = Double.parseDouble(attempt);
            if (answerAsDouble == attemptAsDouble) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            //number is not a decimal number, compare strings
            if (answer.equals(attempt)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
