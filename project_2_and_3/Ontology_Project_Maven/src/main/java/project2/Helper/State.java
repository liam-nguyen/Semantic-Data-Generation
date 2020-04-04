package project2.Helper;

import lombok.Getter;
import lombok.Setter;
import project2.Util.US_States;

import java.util.Optional;

public class State {
    @Getter private String abbr;
    @Setter private String averageMedicareAmount;

    public State(String abbr) {
        this.abbr = abbr;
    }

    public String getFullStateName() { return US_States.getFullStateName(abbr); }

    public static State create(String abbr) throws IllegalArgumentException {
        if (!US_States.states.containsKey(abbr)) throw new IllegalArgumentException("Invalid State Abbreviation");
        return new State(abbr);
    }

    public double getAverageMedicareAmount() {
        try {
            return Double.parseDouble(averageMedicareAmount);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
