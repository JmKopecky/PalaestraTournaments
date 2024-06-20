package dev.prognitio.palaestra_tournaments.tournament;

import java.util.ArrayList;

public class Tournament {

    public ArrayList<Competitor> competitors;
    public DefaultSettings settings;


    public Tournament(ArrayList<Competitor> competitors, DefaultSettings settings) {
        this.competitors = competitors;
        this.settings = settings;
    }
}
