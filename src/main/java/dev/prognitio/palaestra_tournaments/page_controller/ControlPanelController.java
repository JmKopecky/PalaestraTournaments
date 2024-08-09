package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.tournament.Tournament;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControlPanelController {

    @GetMapping("/controlpanel")
    public String controlPanel(Model model) {
        model.addAttribute("tournamentData", PalaestraTournamentsApplication.tournament);
        return "controlpanel";
    }
}
