package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.messages.PostFacilitatorConnectedMessage;
import dev.prognitio.palaestra_tournaments.tournament.Competitor;
import dev.prognitio.palaestra_tournaments.tournament.Match;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MatchController {
    Match match;
    HashMap<String, String> competitorStatus = new HashMap<>();


    @GetMapping("/matchfacilitator")
    public String matchFacilitator(Model model) {
        match = PalaestraTournamentsApplication.tournament.matchComposer.matches.get(PalaestraTournamentsApplication.currentMatchIndex);
        for (Competitor competitor : match.competitors) {
            competitorStatus.put(competitor.name, "Disconnected");
        }
        return "matchfacilitator";
    }


    @MessageMapping("/verifyfacilitatorconnection")
    @SendTo("/topic/postfacilitatorconnected")
    public ResponseEntity<?> verifyFacilitatorConnection(String message) {
        System.out.println("Received message in verifyFacilitatorConnection(): " + message);
        for (Map.Entry<String, String> entry : competitorStatus.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return new ResponseEntity<>(new PostFacilitatorConnectedMessage("Connected", competitorStatus), HttpStatus.OK);
    }


    @GetMapping("/matchclient")
    public String matchClient(Model model) {

        return "matchclient";
    }
}
