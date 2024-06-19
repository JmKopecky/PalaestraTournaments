package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.formdata.KeyFormData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class ConfigureTournamentController {

    @GetMapping("/tournamentsetup")
    public String verifyFacilitator(Model model) {

        String LANIP = "";

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            LANIP = localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            LANIP = "Failed to retrieve the LAN IP. Is the server connected to a network?";
        }

        model.addAttribute("localip", LANIP + ":" + PalaestraTournamentsApplication.PORT);



        return "tournament_setup";
    }
}
