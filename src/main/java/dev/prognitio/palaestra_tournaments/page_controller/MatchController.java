package dev.prognitio.palaestra_tournaments.page_controller;

import dev.prognitio.palaestra_tournaments.messages.ConnectedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatchController {

    @GetMapping("/matchfacilitator")
    public String matchFacilitator(Model model) {

        return "matchfacilitator";
    }


    @MessageMapping("/verifyfacilitatorconnection")
    @SendTo("/topic/incomingfacilitator")
    public ResponseEntity<?> verifyFacilitatorConnection(String message) {
        System.out.println("Received message in verifyFacilitatorConnection(): " + message);
        return new ResponseEntity<>(new ConnectedMessage("Connected"), HttpStatus.OK);
    }


    @GetMapping("/matchclient")
    public String matchClient(Model model) {

        return "matchclient";
    }
}
