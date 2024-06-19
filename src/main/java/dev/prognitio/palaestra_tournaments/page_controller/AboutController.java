package dev.prognitio.palaestra_tournaments.page_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String verifyFacilitator(Model model) {
        //model.addAttribute();

        return "about";
    }
}
