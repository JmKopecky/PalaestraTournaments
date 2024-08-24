package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.tournament.Match;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class FinishTournamentController {

    @GetMapping("/finishtournament")
    public String finishTournament(Model model) {

        HashMap<String, Double> sumScores = new HashMap<>();
        for (Match m : PalaestraTournamentsApplication.tournament.matchComposer.matches) {
            for (Map.Entry<String, Double> entry : m.matchScore.entrySet()) {
                sumScores.put(entry.getKey(), sumScores.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }
        ArrayList<Map.Entry<String, Double>> sortedScores = new ArrayList<>(sumScores.entrySet());
        sortedScores.sort((entry1, entry2) -> -1 * entry1.getValue().compareTo(entry2.getValue()));

        HashMap<String, Object> results = new HashMap<String, Object>();
        results.put("score", sortedScores);

        String winner = "";
        if (PalaestraTournamentsApplication.tournament.matchComposer.type.equals("ffa")) {
            String greatestName = "";
            double greatestScore = Double.MIN_VALUE;
            for (Map.Entry<String, Double> entry : sumScores.entrySet()) {
                if (entry.getValue() > greatestScore) {
                    greatestName = entry.getKey();
                    greatestScore = entry.getValue();
                }
            }
            winner = greatestName;
        } else if (PalaestraTournamentsApplication.tournament.matchComposer.type.equals("singleelim")) {
            System.out.println(PalaestraTournamentsApplication.tournament.matchComposer.matches.getLast());
            winner = PalaestraTournamentsApplication.tournament.matchComposer.matches.getLast().getWinner().name;
        }
        results.put("winner", winner);
        model.addAttribute("results", results);
        return "finishtournament";
    }
}
