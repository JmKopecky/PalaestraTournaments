package dev.prognitio.palaestra_tournaments.tournament;

public class Competitor {

    public String name;

    public Competitor(String name) {
        this.name = name;
    }


    public String toString() {
        String output = "";

        output += name;

        return output;
    }
}
