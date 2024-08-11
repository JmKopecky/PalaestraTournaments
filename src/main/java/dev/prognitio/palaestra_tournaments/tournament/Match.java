package dev.prognitio.palaestra_tournaments.tournament;

import dev.prognitio.palaestra_tournaments.test_parsing.Test;

import java.util.ArrayList;

public class Match {

    public ArrayList<Competitor> competitors;
    public Test test;

    public Match(ArrayList<Competitor> competitors) {
        this.competitors = competitors;
    }

    public String toString() {
        String output = "";

        for (Competitor c : competitors) {
            output += c.toString() + ", ";
        }

        return output;
    }
}
