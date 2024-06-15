package dev.prognitio.palaestra_tournaments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PalaestraTournamentsApplication {


    public static int port = 8080;

    public static void main(String[] args) {
        SpringApplication.run(PalaestraTournamentsApplication.class, args);
    }
}
