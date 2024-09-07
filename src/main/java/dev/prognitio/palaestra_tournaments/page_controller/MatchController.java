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

import java.util.*;

@Controller
public class MatchController {
    Match match;
    int questionIndex = 0;
    HashMap<String, String> competitorStatus = new HashMap<>();
    HashMap<String, Integer> competitorPasswords = new HashMap<>();
    HashMap<String, Integer> competitorQuestionAttempts = new HashMap<>();
    HashMap<String, Boolean> competitorQuestionFinished = new HashMap<>();
    boolean lockQuestion = false;


    @GetMapping("/matchfacilitator")
    public String matchFacilitator(Model model) {
        match = PalaestraTournamentsApplication.tournament.matchComposer.matches.get(PalaestraTournamentsApplication.currentMatchIndex);
        competitorStatus.clear();
        competitorPasswords.clear();
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
        match.started = true;
        nextQuestion(true, false);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @MessageMapping("/requestquestiondata")
    @SendTo("/topic/receivequestion")
    public ResponseEntity<?> sendQuestion(String message) {
        String forVal = message.split("_")[0];
        boolean shouldCompetitorsRequestData = Boolean.parseBoolean(message.split("_")[1]);
        boolean nextQuestion = Boolean.parseBoolean(message.split("_")[2]);
        boolean tieBreaker = Boolean.parseBoolean(message.split("_")[3]);
        if (nextQuestion) {
            nextQuestion(false, tieBreaker);
        }
        boolean isLastMainQuestion = match.test.questions.size() - 1 == questionIndex || match.test.questions.get(questionIndex + 1).isTiebreaker;
        HashMap<String, Object> toReturn = new HashMap<>();
        Question currentQuestion = match.test.questions.get(questionIndex);
        toReturn.put("for", forVal);
        toReturn.put("competitorrequest", shouldCompetitorsRequestData);
        toReturn.put("qnum", questionIndex + 1);
        toReturn.put("qbody", currentQuestion.questionBody);
        toReturn.put("istiebreaker", currentQuestion.isTiebreaker);
        ArrayList<String> answers = new ArrayList<>();
        if (currentQuestion.alternateAnswers.getFirst().equals("nomultiplechoice")) {
            answers.add("nomultiplechoice");
        } else {
            answers.addAll(currentQuestion.alternateAnswers);
            answers.add(currentQuestion.answer);
            Collections.shuffle(answers);
        }
        toReturn.put("qoptions", answers);
        if (forVal.equals("facilitator")) {
            toReturn.put("qanswer", currentQuestion.answer);
            toReturn.put("competitors", match.competitors);
            toReturn.put("score", match.matchScore);
            toReturn.put("attempts", competitorQuestionAttempts);
            toReturn.put("successes", competitorQuestionFinished);
            toReturn.put("lockquestion", lockQuestion);
            toReturn.put("islastquestion", isLastMainQuestion);
        }
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }


    @MessageMapping("/questionresponse")
    @SendTo("/topic/questionanswered")
    public ResponseEntity<?> processQuestionAttempt(String message) {
        String competitor = message.split("_")[0];
        String answer = message.split("_")[1];
        int attemptNum = competitorQuestionAttempts.get(competitor);
        competitorQuestionAttempts.put(competitor, attemptNum + 1);
        boolean isCorrect = match.test.questions.get(questionIndex).isCorrect(answer);
        competitorQuestionFinished.put(competitor, isCorrect || answer.equals("skipped"));

        match.notifyQuestionAttempt(
                competitor,
                isCorrect,
                answer.equals("skipped"),
                attemptNum,
                PalaestraTournamentsApplication.tournament.settings);

        HashMap<String, Object> response = new HashMap<>();
        response.put("competitor", competitor);
        response.put("wascorrect", isCorrect);
        response.put("wasskipped", answer.equals("skipped"));
        response.put("lockquestion", PalaestraTournamentsApplication.tournament.settings.questionLocking && isCorrect);
        lockQuestion = PalaestraTournamentsApplication.tournament.settings.questionLocking && isCorrect;
        response.put("successes", competitorQuestionFinished);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @MessageMapping("/doscorescreen")
    @SendTo("/topic/matchscore")
    public ResponseEntity<?> doScoreScreen() {
        System.out.println("Showing score screen.");
        return new ResponseEntity<>(match.matchScore, HttpStatus.OK);
    }


    @MessageMapping("/endmatch")
    @SendTo("/topic/forceclientsendmatch")
    public ResponseEntity<?> endMatch() {
        match.concluded = true;
        PalaestraTournamentsApplication.tournament.matchComposer.updateMatches();
        return new ResponseEntity<>(null, HttpStatus.OK);
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
                return new ResponseEntity<>("Fail_pw", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Fail_name", HttpStatus.OK);
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


    public void nextQuestion(boolean matchInit, boolean wantsTiebreaker) {
        if (matchInit) {
            questionIndex = 0;
        } else {
            if (questionIndex + 1 >= match.test.questions.size()) {
                return;
            }
            if (match.test.questions.get(questionIndex + 1).isTiebreaker) {
                if (wantsTiebreaker) {
                    questionIndex++;
                } else if (questionIndex + 2 < match.test.questions.size()) {
                    questionIndex += 2;
                } else {
                    return;
                }
            } else {
                questionIndex++;
            }
        }
        competitorQuestionAttempts.clear();
        competitorQuestionFinished.clear();
        for (Competitor competitor : match.competitors) {
            competitorQuestionAttempts.put(competitor.name, 0);
            competitorQuestionFinished.put(competitor.name, false);
        }
        lockQuestion = false;
    }
}
