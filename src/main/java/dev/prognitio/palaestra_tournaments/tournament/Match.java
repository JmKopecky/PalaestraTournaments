package dev.prognitio.palaestra_tournaments.tournament;

import dev.prognitio.palaestra_tournaments.test_parsing.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class Match {

    public ArrayList<Competitor> competitors;
    public Test test;
    public HashMap<String, Double> matchScore = new HashMap<>();

    public Match(ArrayList<Competitor> competitors) {
        this.competitors = competitors;
        for (Competitor competitor : competitors) {
            matchScore.put(competitor.name, 0.0);
        }
    }


    public void notifyQuestionAttempt(String competitor, boolean correct, boolean skipped, int attemptNumber, DefaultSettings config) {
        double score = 0;
        if (correct) {
            score = config.pointsPerCorrect + (config.pointsPerCorrect * attemptNumber);
        } else {
            score = config.pointsPerWrong;
        }
        if (skipped) {
            score = config.pointsPerSkipped;
        }
        matchScore.put(competitor, matchScore.get(competitor) + score);
    }


    public String toString() {
        String output = "";

        for (Competitor c : competitors) {
            output += c.toString() + ", ";
        }

        return output;
    }
}
