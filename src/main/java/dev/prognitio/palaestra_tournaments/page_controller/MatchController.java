package dev.prognitio.palaestra_tournaments.page_controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.messages.PostFacilitatorConnectedMessage;
import dev.prognitio.palaestra_tournaments.test_parsing.Question;
import dev.prognitio.palaestra_tournaments.tournament.Competitor;
import dev.prognitio.palaestra_tournaments.tournament.Match;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class MatchController {
    Match match;
    int questionIndex = 0;
    HashMap<String, String> competitorStatus = new HashMap<>();
    HashMap<String, Integer> competitorPasswords = new HashMap<>();


    @GetMapping("/matchfacilitator")
    public String matchFacilitator(Model model) {
        match = PalaestraTournamentsApplication.tournament.matchComposer.matches.get(PalaestraTournamentsApplication.currentMatchIndex);
        for (Competitor competitor : match.competitors) {
            competitorStatus.put(competitor.name, "Disconnected");
            competitorPasswords.put(competitor.name, genPassword());
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
        return new ResponseEntity<>(new PostFacilitatorConnectedMessage("Connected", competitorStatus, competitorPasswords), HttpStatus.OK);
    }


    @MessageMapping("/facilitatorbeginmatch")
    @SendTo("/topic/beginmatchinit")
    public ResponseEntity<?> matchInit() {
        System.out.println("Beginning match...");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @MessageMapping("/requestquestiondata")
    @SendTo("/topic/receivequestion")
    public ResponseEntity<?> sendQuestion(String message) {
        HashMap<String, Object> toReturn = new HashMap<>();
        Question currentQuestion = match.test.questions.get(questionIndex);
        toReturn.put("for", message);
        toReturn.put("qnum", questionIndex + 1);
        toReturn.put("qbody", currentQuestion.questionBody);
        toReturn.put("qoptions", currentQuestion.alternateAnswers.isEmpty() ? "notmultiplechoice" : currentQuestion.alternateAnswers);
        if (message.equals("facilitator")) {
            toReturn.put("qanswer", currentQuestion.answer);
            toReturn.put("score", match.matchScore);
        }
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }


    @GetMapping("/matchclient")
    public String matchClient(Model model) {
        return "matchclient";
    }


    @PostMapping("/matchclient")
    public ResponseEntity<?> matchClientAuth(Model model, @RequestBody String data) {
        String assertedCompetitor;
        int assertedPassword;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(data);
            assertedCompetitor = node.get("assertedCompetitor").asText();
            assertedPassword = node.get("assertedPassword").asInt();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (competitorPasswords.containsKey(assertedCompetitor)) {
            if (competitorPasswords.get(assertedCompetitor) == assertedPassword) {
                competitorStatus.put(assertedCompetitor, "Connected");
                return new ResponseEntity<>("Success_" + assertedCompetitor + "_" + assertedPassword, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fail", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Competitor not found", HttpStatus.NOT_FOUND);
        }
    }


    public int genPassword() {
        Random random = new Random();
        int attemptPassword = random.nextInt(111111, 999999);
        if (competitorPasswords.containsValue(attemptPassword)) {
            return genPassword();
        }
        return attemptPassword;
    }
}
