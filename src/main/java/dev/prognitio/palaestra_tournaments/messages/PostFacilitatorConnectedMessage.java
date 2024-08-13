package dev.prognitio.palaestra_tournaments.messages;

import java.util.HashMap;

public class PostFacilitatorConnectedMessage {

    private String status;
    private HashMap<String, String> competitorStatus;

    public PostFacilitatorConnectedMessage() {}

    public PostFacilitatorConnectedMessage(String status, HashMap<String, String> competitorStatus) {
        this.status = status;
        this.competitorStatus = competitorStatus;
    }

    public String getStatus() {
        return status;
    }

    public HashMap<String, String> getCompetitorStatus() {
        return competitorStatus;
    }
}
