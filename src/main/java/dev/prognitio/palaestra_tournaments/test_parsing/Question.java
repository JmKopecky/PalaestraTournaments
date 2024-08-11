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
}
