package project2.Hospital.utils;

public class State {
    String abbr;
    String amount;

    public State(String abbr) {
        this.abbr = abbr;
    }

    public String getAbbr() {
        return abbr;
    }
    public String getAmount() { return amount; }
    public String getFullStateName() { return US_States.getFullStateName(abbr); }
    public static State create(String abbr) {
        return new State(abbr);
    }
    public void setAverageMedicareAmount(String amount) {
        this.amount = amount;
    }
}
