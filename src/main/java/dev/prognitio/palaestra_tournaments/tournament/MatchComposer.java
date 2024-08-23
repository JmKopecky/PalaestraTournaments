package dev.prognitio.palaestra_tournaments.tournament;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

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


    public Match getMatchWithKey(String key) {
        if (type.equals("ffa")) {
            return matches.get(Integer.parseInt(key.split("Match ")[1].split("_")[0]) - 1);
        } else if (type.equals("singleelim")) {
            String comp1String = key.split(",")[0];
            String comp2String = key.split(",")[1];
            Competitor c1 = null;
            Competitor c2 = null;
            for (Competitor competitor : allCompetitors) {
                if (competitor.name.equals(comp1String)) {
                    c1 = competitor;
                }
                if (competitor.name.equals(comp2String)) {
                    c2 = competitor;
                }
            }

            for (Match m : matches) {
                if (m.competitors.contains(c1) && m.competitors.contains(c2)) {
                    return m;
                }
            }
        }
        return null;
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
        for (int i = output.size(); i < allCompetitors.size() - 1; i++) {
            ArrayList<Competitor> matchCompetitors = new ArrayList<>();
            Match placeholder = new Match(matchCompetitors);
            placeholder.isPlaceholder = true;
            output.add(placeholder);
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


    public void updateMatches() {
        if (type.equals("singleelim")) {
            for (int i = 1; i < matches.size(); i += 2) {
                if (matches.get(i-1).concluded && matches.get(i).concluded) {
                    int maxPower = (int) (Math.log(allCompetitors.size()) / Math.log(2));
                    int totalLevelSize = 0;
                    for (int levelSizeCounter = 1; levelSizeCounter <= maxPower; levelSizeCounter++) {
                        int levelSize = allCompetitors.size() >> levelSizeCounter;

                        //1, 2, 3, 4, 5, 6, 7, 8
                        //9, 10, 11, 12
                        //13, 14
                        //15
                        //only run if i is between totalLevelSize and totalLevelSize - levelSize; (if i is in the current level)
                        System.out.println("I: " + i + " MaxPower: " + maxPower + " totalLevelSize: " + totalLevelSize + " levelSize: " + levelSize + " levelSizeCounter: " + levelSizeCounter);
                        if (i+1 > totalLevelSize && i+1 <= totalLevelSize + levelSize) { //TODO
                            System.out.println("levelSizeCounter: " + levelSizeCounter + " totalLevelSize: " + totalLevelSize + " levelSize: " + levelSize);
                            System.out.println("I: " + i);
                            // totalLevelSize + ((i+1) - previousLevelSize)/2 == matchPos-1
                            int previousLevelSize = Math.max(totalLevelSize - levelSize, 0);
                            int targetPos = (totalLevelSize + levelSize + ( (i+1) - (previousLevelSize * 2 ))/2) - 1;
                            System.out.println("targetPos: " + targetPos);
                            if (matches.get(targetPos).isPlaceholder) {
                                ArrayList<Competitor> competitors = new ArrayList<Competitor>();
                                competitors.add(matches.get(i-1).getWinner());
                                competitors.add(matches.get(i).getWinner());
                                Match toAdd = new Match(competitors);
                                matches.set(targetPos, toAdd);
                            }
                        }
                        totalLevelSize += levelSize;
                    }
                }
            }
        }
        System.out.println("Updated Matches: " + matches);
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
