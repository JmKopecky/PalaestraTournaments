package dev.prognitio.palaestra_tournaments.tournament;

import jakarta.annotation.Nullable;

import java.util.ArrayList;

public class MatchComposer {


    public String type;
    public ArrayList<Competitor> allCompetitors;
    public ArrayList<Match> matches;

    public MatchComposer(String type, ArrayList<Competitor> competitors, int nMatches) {
        this.type = type;
        this.allCompetitors = competitors;
        switch (type) {
            case "singleelim": {
                //create an arraylist of matches representing the bracket. competitors.size() % 2^n == 0
                matches = initSingleElim();
                break;
            }
            case "ffa": {
                matches = initFFA(nMatches);
                break;
            }
        }
    }



    private ArrayList<Match> initSingleElim() {
        ArrayList<Match> output = new ArrayList<>();

        int nMatches = allCompetitors.size() / 2;
        for (int i = 0; i < nMatches; i++) {
            ArrayList<Competitor> matchCompetitors = new ArrayList<>();
            matchCompetitors.add(allCompetitors.get(i * 2));
            matchCompetitors.add(allCompetitors.get(i * 2 + 1));
            output.add(new Match(matchCompetitors));
        }

        return output;
    }


    private ArrayList<Match> initFFA(int matchCount) {
        ArrayList<Match> output = new ArrayList<>();

        for (int i = 0; i < matchCount; i++) {
            Match match = new Match(allCompetitors);
            output.add(match);
        }

        return output;
    }


    public String toString() {
        String output = "";
        output += "MATCH COMPOSER: \n";
        output += "type: " + type + "\n";
        output += "Competitors: ";
        for (Competitor c : allCompetitors) {
            output += c.toString() + ", ";
        }
        output += "\n";
        output += "Matches: ";
        for (Match m : matches) {
            output += "{" + m.toString() + "}";
        }
        output += "\n";
        return output;
    }
}
