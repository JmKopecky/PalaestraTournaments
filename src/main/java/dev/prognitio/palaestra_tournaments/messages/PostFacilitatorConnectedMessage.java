package dev.prognitio.palaestra_tournaments.messages;

import java.util.HashMap;

public class PostFacilitatorConnectedMessage {

    private String status;
    private HashMap<String, String> competitorStatus;
    private HashMap<String, Integer> competitorPasswords;

    public PostFacilitatorConnectedMessage() {}

    public PostFacilitatorConnectedMessage(String status, HashMap<String, String> competitorStatus, HashMap<String, Integer> competitorPasswords) {
        this.status = status;
        this.competitorStatus = competitorStatus;
        this.competitorPasswords = competitorPasswords;
    }

    public String getStatus() {
        return status;
    }

    public HashMap<String, String> getCompetitorStatus() {
        return competitorStatus;
    }

    public HashMap<String, Integer> getCompetitorPasswords() {
        return competitorPasswords;
    }
}
