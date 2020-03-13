package project2;

import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.XSD;
import project2.utils.US_States;

/**
 * A model for medicare hospital spending dataset
 */
public class HospitalSpendingOntology {
    private static String source = "https://data.medicare.gov/d/nrth-mfg3";
    private static String NS = source + "#";
    public enum Classes {Hospital, Place, State, Nation, placeDealsWithMedicare}
    public enum Props {hasID, hasFacilityName, hasAverageSpending}
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";

    private OntModel model;

    private HospitalSpendingOntology() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", NS); // set namespace prefix
        model.setNsPrefix("du", duURI);

        populateModel();
    }

    public void populateModel() {
        // Create classes
        OntClass hospital = model.createClass(NS + Classes.Hospital);
        OntClass state = model.createClass(NS + Classes.State);
        state.addComment("One of 50 states in US", "EN");
        OntClass nation = model.createClass(NS + Classes.Nation);
        state.addComment("United State of America", "EN");
        OntClass place = model.createClass(NS + Classes.Place);
        place.addComment("Immobile things or locations", "EN");
        OntClass placeDealsWithMedicare = model.createClass(NS + Classes.placeDealsWithMedicare);

        //Establish class relationships
        placeDealsWithMedicare.addSubClass(hospital);
        placeDealsWithMedicare.addSubClass(state);
        placeDealsWithMedicare.addSubClass(nation);
        place.addSubClass(state);
        place.addSubClass(nation);

        /* Create properties */
        ObjectProperty hasID = model.createObjectProperty(NS + Props.hasID);
        hasID.addComment("has a facility ID of", "EN");
        hasID.addDomain(hospital);
        hasID.addRange(XSD.positiveInteger);
        ObjectProperty hasFacilityName = model.createObjectProperty(NS + Props.hasFacilityName);
        hasFacilityName.addComment("has a facility ID of", "EN");
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(XSD.xstring);
        ObjectProperty hasAverageSpending = model.createObjectProperty(NS + Props.hasAverageSpending);
        hasFacilityName.addComment("has a average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasAverageSpending.addDomain(placeDealsWithMedicare);
        hasAverageSpending.addRange(XSD.decimal);
        ObjectProperty hasLocation = model.createObjectProperty(duURI + "hasLocation");
        hasLocation.addDomain(hospital);
        hasLocation.addRange(place);
    }

    public OntModel getModel() {return model;}

    public void addHospital(String ID, String name, String inputState, String amount) {
        if (Integer.parseInt(ID) < 0) throw new IllegalArgumentException("Only positive ID");
        // Gather classes and properties
        OntClass hospital = model.getOntClass(NS + Classes.Hospital);
        Individual instance = hospital.createIndividual(NS + name);
        Individual stateInstance = model.getIndividual(NS + inputState);
        if (stateInstance == null) {
            OntClass state = model.getOntClass(NS + Classes.State);
            stateInstance = state.createIndividual(NS + inputState);
            stateInstance.addComment(US_States.getFullStateName(inputState),"EN");
        }
        Property hasID = model.getProperty(NS + Props.hasID);
        Property hasFacilityName = model.getProperty(NS + Props.hasFacilityName);
        Property hasLocation = model.getProperty(duURI + "hasLocation");
        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);

        // Add to model
        model.add(instance, hasFacilityName, name);
        model.add(instance, hasID, ID);
        model.add(instance, hasLocation, stateInstance);
        model.add(instance, hasAverageSpending, amount);
    }

    public void addStateSpending(String stateName, String amount) {
        // Gather classes and properties
        OntClass state = model.getOntClass(NS + Classes.State);
        Individual stateInstance = model.getIndividual(NS + stateName);
        if (stateInstance == null) {
            stateInstance = state.createIndividual(NS + stateName);
            stateInstance.addComment(US_States.getFullStateName(stateName),"EN");
        }
        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
        model.add(stateInstance, hasAverageSpending, amount);
    }

    public void addNationSpending(String amount) {
        // Gather classes and properties
        OntClass nation = model.getOntClass(NS + Classes.Nation);
        Individual instance = model.getIndividual(NS + "USA");
        if (instance == null) {
            instance = nation.createIndividual(NS + "USA");
            instance.addLabel("United State of America","EN");
        }
        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
        model.add(instance, hasAverageSpending, amount);
    }

    public static void main(String[] args) {
        HospitalSpendingOntology ontology = new HospitalSpendingOntology();
        OntModel model = ontology.getModel();

        /* Examples */
        // How to get class and add individual instance
        ontology.addHospital("10001", "SOUTHEAST ALABAMA MEDICAL CENTER", "AL", "20779");
        ontology.addStateSpending("CA", "22304");
        ontology.addNationSpending("21646");
        model.write(System.out);
//        System.out.println(model.getOntClass(NS + Class.Hospital));
    }
}
