package project2.Hospital.utils;

public class State {
    String name;
    String amount;
    public State(String name) {
        this.name = name;
    }
    public State(String name, String amount) {
        this(name);
        this.amount = amount;
    }

    public void setAmount(String amount) {this.amount = amount;}
    public String getAmount() {return amount;}
}
