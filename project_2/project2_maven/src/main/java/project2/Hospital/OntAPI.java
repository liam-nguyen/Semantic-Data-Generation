package project2.Hospital;

import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;

import java.util.ArrayList;
import java.util.List;

/**
 * This class gathers data to build a model
 */
public class OntAPI {
    public org.apache.jena.ontology.OntModel model;
    List<Hospital> hospitals;
    List<State> states;
    String nationAverage = "-1";

    public OntAPI() {
        model = new OntModel().getModel();
        hospitals = new ArrayList<>();
        states = new ArrayList<>();
    }

    public void addHospital(Hospital h) {
        hospitals.add(h);
    }

    public void addState(State s) {
        states.add(s);
    }

    public void addNationMedicareSpending(String amount) {
        nationAverage = amount;
    }

//    public void addHospitalName(String ID, String name ) {
//        if (Integer.parseInt(ID) < 0) throw new IllegalArgumentException("Only positive ID");
//        // Gather classes and properties
//        OntClass hospital = model.getOntClass(NS + Classes.Hospital);
//        Individual instance = hospital.createIndividual(NS + name);
//        Individual stateInstance = model.getIndividual(NS + inputState);
//        if (stateInstance == null) {
//            OntClass state = model.getOntClass(NS + Classes.State);
//            stateInstance = state.createIndividual(NS + inputState);
//            stateInstance.addComment(US_States.getFullStateName(inputState), "EN");
//        }
//        Property hasID = model.getProperty(NS + Props.hasID);
//        Property hasFacilityName = model.getProperty(NS + Props.hasFacilityName);
//        Property hasLocation = model.getProperty(duURI + "hasLocation");
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//
//        // Add to model
//        model.add(instance, hasFacilityName, name);
//        model.add(instance, hasID, ID);
//        model.add(instance, hasLocation, stateInstance);
//        model.add(instance, hasAverageSpending, amount);
//    }

//    public void addStateSpending(String stateName, String amount) {
//        // Gather classes and properties
//        OntClass state = model.getOntClass(NS + Classes.State);
//        Individual stateInstance = model.getIndividual(NS + stateName);
//        if (stateInstance == null) {
//            stateInstance = state.createIndividual(NS + stateName);
//            stateInstance.addComment(US_States.getFullStateName(stateName), "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(stateInstance, hasAverageSpending, amount);
//    }

//    public void addNationSpending(String amount) {
//        // Gather classes and properties
//        OntClass nation = model.getOntClass(NS + Classes.Nation);
//        Individual instance = model.getIndividual(NS + "USA");
//        if (instance == null) {
//            instance = nation.createIndividual(NS + "USA");
//            instance.addLabel("United State of America", "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(instance, hasAverageSpending, amount);
//    }

    public void display() {
        model.write(System.out);
    }

    public static void main(String[] args) {
//        OntAPI ont = new OntAPI();
//        ont.display();

        Hospital A = Hospital.create("1234").name("Liam").ownership("Self").validate();
        System.out.println(A);
    }
}
