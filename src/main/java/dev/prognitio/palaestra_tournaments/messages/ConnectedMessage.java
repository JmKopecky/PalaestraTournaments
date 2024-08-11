package dev.prognitio.palaestra_tournaments.messages;

public class ConnectedMessage {

    private String status;

    public ConnectedMessage() {}

    public ConnectedMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
