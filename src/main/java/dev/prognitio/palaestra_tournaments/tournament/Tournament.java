package dev.prognitio.palaestra_tournaments.tournament;

import java.util.ArrayList;

public class Tournament {

    public ArrayList<Competitor> competitors;
    public DefaultSettings settings;


    public Tournament(ArrayList<Competitor> competitors, DefaultSettings settings) {
        this.competitors = competitors;
        this.settings = settings;
    }


    public String toString() {
        String output = "";

        output += "competitor:{";
        for (Competitor competitor : competitors) {
            output += competitor.name + ",";
        }
        output += "}";

        output += "settings:{";
        output += "defmultiplechoice=" + settings.multipleChoice + ",";
        output += "defquestionlocking=" + settings.questionLocking + ",";
        output += "defpointscorrect=" + settings.pointsPerCorrect + ",";
        output += "defpointswrong=" + settings.pointsPerWrong + ",";
        output += "defpointsskipped=" + settings.pointsPerSkipped + ",";
        output += "defpointscale=" + settings.pointScale;
        output += "}";
        return output;
    }
}
