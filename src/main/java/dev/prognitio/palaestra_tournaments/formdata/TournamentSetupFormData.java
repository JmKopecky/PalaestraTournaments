package dev.prognitio.palaestra_tournaments.formdata;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TournamentSetupFormData {
    private boolean multipleChoice;
    private boolean questionLocking;
    private int pointsPerCorrect;
    private int pointsPerWrong;
    private int pointsPerSkipped;
    private double pointScale;

}
