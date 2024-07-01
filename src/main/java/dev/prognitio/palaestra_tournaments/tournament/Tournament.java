package dev.prognitio.palaestra_tournaments.tournament;

import java.util.ArrayList;

public class Tournament {

    public DefaultSettings settings;
    public MatchComposer matchComposer;


    public Tournament(DefaultSettings settings, MatchComposer matchComposer) {
        this.settings = settings;
        this.matchComposer = matchComposer;
    }



    public String toString() {
        String output = "";
        output += "TOURNAMENT STRING OUTPUT: \n";
        output += "\n";
        output += settings.toString();
        output += "\n";
        output += matchComposer.toString();
        return output;
    }
}
