package dev.prognitio.palaestra_tournaments.tournament;

import java.util.ArrayList;

public class Match {

    public ArrayList<Competitor> competitors;

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
