package dev.prognitio.palaestra_tournaments;

import dev.prognitio.palaestra_tournaments.tournament.Tournament;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
@RestController
public class PalaestraTournamentsApplication {


    public static int PORT = 8080;
    public static HashMap<String, String> FACILITATOR_KEY = new HashMap<>();

    public static Tournament tournament;
    public static int currentMatchIndex = -1;



    public static void main(String[] args) {
        SpringApplication.run(PalaestraTournamentsApplication.class, args);

        Log.log(Log.Type.INFO, "Server main method ran.", PalaestraTournamentsApplication.class);

        FACILITATOR_KEY.put("dev", "WhateverItTakes");
    }
}
