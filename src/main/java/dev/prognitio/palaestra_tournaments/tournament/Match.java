package dev.prognitio.palaestra_tournaments.tournament;

import dev.prognitio.palaestra_tournaments.test_parsing.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Match {

    public ArrayList<Competitor> competitors;
    public Test test;
    public boolean started = false;
    public boolean concluded = false;
    public boolean isPlaceholder = false;
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
            score = Math.max(config.pointsPerCorrect - (config.pointScale * attemptNumber), 0);
        } else {
            score = -1 * config.pointsPerWrong;
        }
        if (skipped) {
            score = -1 * config.pointsPerSkipped;
        }
        matchScore.put(competitor, matchScore.get(competitor) + score);
    }


    public Competitor getWinner() {
        Map.Entry<String, Double> greatestScore = null;
        for (Map.Entry<String, Double> entry : matchScore.entrySet()) {
            if (greatestScore != null) {
                if (greatestScore.getValue() < entry.getValue()) {
                    greatestScore = entry;
                }
            } else {
                greatestScore = entry;
            }
        }
        for (Competitor c : competitors) {
            if (c.name.equals(greatestScore.getKey())) {
                return c;
            }
        }
        return null;
    }


    public String toString() {
        String output = "";

        for (Competitor c : competitors) {
            output += c.toString() + ", ";
        }

        return output;
    }
}
