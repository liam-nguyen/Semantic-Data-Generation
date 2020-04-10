package project2.Helper;

import lombok.Getter;
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
import lombok.Setter;
>>>>>>> liam
import project2.Util.US_States;
=======
import Util.US_States;
>>>>>>> Stashed changes

public class State {
    @Getter private String abbr;
    @Getter private double averageMedicareAmount;

    //== Public method ==//
    public static State create(String abbr) throws IllegalArgumentException {
        if (!US_States.states.containsKey(abbr)) throw new IllegalArgumentException("Invalid State Abbreviation");
        return new State(abbr);
    }
    public String getFullStateName() { return US_States.getFullStateName(abbr); }

    //== == Setters == ==//
    public State setAverageStateMedicareAmount(double amount) {
        this.averageMedicareAmount = amount;
        return this;
    }
    public State setAverageStateMedicareAmount(String amount) {
        try {
            return  setAverageStateMedicareAmount(Double.parseDouble(amount));
        }
        catch(NumberFormatException e) {
            return setAverageStateMedicareAmount(-1);
        }
    }

    //== Private ==//
    //== == Constructor == ==//
    private State(String abbr) {
        this.abbr = abbr;
    }
}
