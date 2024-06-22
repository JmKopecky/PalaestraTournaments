package dev.prognitio.palaestra_tournaments.page_controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import dev.prognitio.palaestra_tournaments.Log;
import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.formdata.TournamentSetupFormData;
import dev.prognitio.palaestra_tournaments.tournament.Competitor;
import dev.prognitio.palaestra_tournaments.tournament.DefaultSettings;
import dev.prognitio.palaestra_tournaments.tournament.Tournament;
import netscape.javascript.JSObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class ConfigureTournamentController {

    @GetMapping("/tournamentsetup")
    public String setupTournament(Model model) {

        model.addAttribute("tournamentSettingsFormData", new TournamentSetupFormData());

        return "tournament_setup";
    }


    @PostMapping("/tournamentsetup")
    public String setupTournamentSubmit(Model model, @RequestBody String data) {
        boolean defaultMultipleChoice;
        boolean defaultQuestionLocking;
        int defaultPointsPerCorrect;
        int defaultPointsPerWrong;
        int defaultPointsPerSkipped;
        double defaultPointScale;
        ArrayList<Competitor> competitors = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(data);
            defaultMultipleChoice = node.get("defaultMultipleChoice").asBoolean();
            defaultQuestionLocking = node.get("defaultQuestionLocking").asBoolean();
            defaultPointsPerCorrect = node.get("defaultPointsPerCorrect").asInt();
            defaultPointsPerWrong = node.get("defaultPointsPerWrong").asInt();
            defaultPointsPerSkipped = node.get("defaultPointsPerSkipped").asInt();
            defaultPointScale = node.get("defaultPointScale").asDouble();
            node.get("competitorNames").elements().forEachRemaining(
                    competitor -> competitors.add(new Competitor(competitor.asText())));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        PalaestraTournamentsApplication.tournament = new Tournament(competitors, new DefaultSettings(
                defaultMultipleChoice, defaultQuestionLocking,
                defaultPointsPerCorrect, defaultPointsPerWrong,
                defaultPointsPerSkipped, defaultPointScale
        ));

        System.out.println(PalaestraTournamentsApplication.tournament);

        return "tournament_setup"; //"redirect:/URL"
    }
}
