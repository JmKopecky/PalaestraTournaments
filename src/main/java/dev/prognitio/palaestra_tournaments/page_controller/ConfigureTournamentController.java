package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.Log;
import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.formdata.KeyFormData;
import dev.prognitio.palaestra_tournaments.formdata.TournamentSetupFormData;
import dev.prognitio.palaestra_tournaments.tournament.Competitor;
import dev.prognitio.palaestra_tournaments.tournament.DefaultSettings;
import dev.prognitio.palaestra_tournaments.tournament.Tournament;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Controller
public class ConfigureTournamentController {

    @GetMapping("/tournamentsetup")
    public String setupTournament(Model model) {

        model.addAttribute("tournamentSettingsFormData", new TournamentSetupFormData());

        return "tournament_setup";
    }


    @PostMapping("/tournamentsetup")
    public String setupTournamentSubmit(Model model, @ModelAttribute("tournamentSettingsFormData") TournamentSetupFormData data) {
        Log.log(Log.Type.INFO,
                "Multiple Choice: " + data.isMultipleChoice() +
                        " | Question Locking: " + data.isQuestionLocking() +
                        " | Points Per Correct: " + data.getPointsPerCorrect() +
                        " | Points Per Wrong: " + data.getPointsPerWrong() +
                        " | Points Per Skipped: " + data.getPointsPerSkipped() +
                        " | Point Scale: " + data.getPointScale(), this.getClass());

        ArrayList<Competitor> competitorList = new ArrayList<>();

        PalaestraTournamentsApplication.tournament = new Tournament(competitorList,
                new DefaultSettings(
                        data.isMultipleChoice(), data.isQuestionLocking(),
                        data.getPointsPerCorrect(), data.getPointsPerWrong(),
                        data.getPointsPerSkipped(), data.getPointScale()
                ));

        return "tournament_setup"; //"redirect:/URL"
    }
}
