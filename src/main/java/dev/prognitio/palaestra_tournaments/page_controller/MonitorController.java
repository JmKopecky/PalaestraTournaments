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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MonitorController {


    @GetMapping("/monitor")
    public String monitor(Model model) {
        //model.addAttribute();

        String LANIP = "";

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            LANIP = localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            LANIP = "Failed to retrieve the LAN IP. Is the server connected to a network?";
        }

        model.addAttribute("localip", LANIP + ":" + PalaestraTournamentsApplication.PORT);

        return "monitor";
    }


    @MessageMapping("/requestmonitordata")
    @SendTo("/topic/receivemonitordata")
    public ResponseEntity<?> monitorDataUpdate(String message) {
        return new ResponseEntity<>(packageData(), HttpStatus.OK);
    }


    private HashMap<String, Object> packageData() {
        HashMap<String, Object> data = new HashMap<>();

        HashMap<String, Double> sumScores = new HashMap<>();
        if (PalaestraTournamentsApplication.tournament == null) {
            return data;
        }
        for (Match m : PalaestraTournamentsApplication.tournament.matchComposer.matches) {
            for (Map.Entry<String, Double> entry : m.matchScore.entrySet()) {
                sumScores.put(entry.getKey(), sumScores.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }
        ArrayList<Map.Entry<String, Double>> sortedScores = new ArrayList<>(sumScores.entrySet());
        sortedScores.sort((entry1, entry2) -> -1 * entry1.getValue().compareTo(entry2.getValue()));

        data.put("scores", sortedScores);

        return data;
    }
}
