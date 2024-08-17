package dev.prognitio.palaestra_tournaments.test_parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public ArrayList<Question> questions = new ArrayList<>();
    String testTitle = "";

    public Test(String inputString) {
        testTitle = inputString.split("title:")[1].split("&qnum:")[0];
        int qnum = Integer.parseInt(inputString.split("qnum:")[1].split("question:")[0]);

        ArrayList<String> questionStrings = new ArrayList<>();
        questionStrings.addAll(List.of(inputString.split("question:")));
        questionStrings.removeFirst();

        questions.clear();
        for (String questionString : questionStrings) {
            String text = questionString.split("text=")[1].split("_answer=")[0];
            String answer = questionString.split("_answer=")[1].split("_altanswers=")[0];
            ArrayList<String> altAnswers = new ArrayList<>();
            if (questionString.split("_altanswers=")[1].split("text=")[0].equals("fillintheblank")) {
                //no multiple choice.
                altAnswers.add("nomultiplechoice");
            } else {
                altAnswers.addAll(List.of(questionString.split("_altanswers=")[1].split("&")));
            }
            Question q = new Question(text, answer, altAnswers);
            questions.add(q);
        }
    }
}
