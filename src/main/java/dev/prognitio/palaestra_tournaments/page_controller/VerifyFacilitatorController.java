package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.Log;
import dev.prognitio.palaestra_tournaments.PalaestraTournamentsApplication;
import dev.prognitio.palaestra_tournaments.formdata.KeyFormData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class VerifyFacilitatorController {

    @GetMapping("/verifyfacilitator")
    public String verifyFacilitator(Model model) {

        String LANIP = "";

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            LANIP = localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            LANIP = "Failed to retrieve the LAN IP. Is the server connected to a network?";
        }

        model.addAttribute("localip", LANIP + ":" + PalaestraTournamentsApplication.PORT);

        model.addAttribute("keyFormData", new KeyFormData());

        return "verify_facilitator";
    }


    @PostMapping("/verifyfacilitator")
    public String verifyFacilitatorSubmit(Model model, @ModelAttribute("keyFormData") KeyFormData data) {

        Log.log(Log.Type.INFO, "Name: " + data.getName() + " | Key: " + data.getSubmittedKey(), this.getClass());

        if (PalaestraTournamentsApplication.FACILITATOR_KEY.containsKey(data.getName()) && PalaestraTournamentsApplication.FACILITATOR_KEY.get(data.getName()).equals(data.getSubmittedKey())) {
            return "redirect:/tournamentsetup";
        }

        return "verify_facilitator";
    }
}
