package dev.prognitio.palaestra_tournaments.page_controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.tournament.Competitor;
import dev.prognitio.palaestra_tournaments.tournament.DefaultSettings;
import dev.prognitio.palaestra_tournaments.tournament.MatchComposer;
import dev.prognitio.palaestra_tournaments.tournament.Tournament;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ControlPanelController {

    @GetMapping("/controlpanel")
    public String controlPanel(Model model) {
        model.addAttribute("tournamentData", PalaestraTournamentsApplication.tournament);
        return "controlpanel";
    }


    @PostMapping(value = "/controlpanel", produces = "application/json")
    public ResponseEntity<?> controlPanel(Model model, @RequestBody String data) {


        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(data);
            //defaultMultipleChoice = node.get("defaultMultipleChoice").asBoolean();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> questions = new HashMap<>();
        questions.put("hi", "x");
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}
