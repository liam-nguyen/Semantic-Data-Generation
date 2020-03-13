package project2;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.XSD;
import project2.utils.US_States;

public class HospitalOntology {
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    public enum Classes {Hospital, Place, State, Nation, placeDealsWithMedicare}
    public enum Props {hasID, hasFacilityName, hasAverageSpending}

    private static String source = "https://data.medicare.gov/d/nrth-mfg3";
    private static String NS = source + "#";
    private OntModel model;

    private HospitalOntology() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", NS); // set namespace prefix
        model.setNsPrefix("du", duURI);
        populateModel();
    }

    public void populateModel() {
        // Create classes
        OntClass hospital = model.createClass(NS + project2.HospitalSpendingOntology.Classes.Hospital);
        OntClass state = model.createClass(NS + project2.HospitalSpendingOntology.Classes.State);
        state.addComment("One of 50 states in US", "EN");
        OntClass nation = model.createClass(NS + project2.HospitalSpendingOntology.Classes.Nation);
        state.addComment("United State of America", "EN");
        OntClass place = model.createClass(NS + project2.HospitalSpendingOntology.Classes.Place);
        place.addComment("Immobile things or locations", "EN");
        OntClass placeDealsWithMedicare = model.createClass(NS + project2.HospitalSpendingOntology.Classes.placeDealsWithMedicare);

        //Establish class relationships
        placeDealsWithMedicare.addSubClass(hospital);
        placeDealsWithMedicare.addSubClass(state);
        placeDealsWithMedicare.addSubClass(nation);
        place.addSubClass(state);
        place.addSubClass(nation);

        /* Create properties */
        ObjectProperty hasID = model.createObjectProperty(NS + project2.HospitalSpendingOntology.Props.hasID);
        hasID.addComment("has a facility ID of", "EN");
        hasID.addDomain(hospital);
        hasID.addRange(XSD.positiveInteger);
        ObjectProperty hasFacilityName = model.createObjectProperty(NS + project2.HospitalSpendingOntology.Props.hasFacilityName);
        hasFacilityName.addComment("has a facility ID of", "EN");
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(XSD.xstring);
        ObjectProperty hasAverageSpending = model.createObjectProperty(NS + project2.HospitalSpendingOntology.Props.hasAverageSpending);
        hasFacilityName.addComment("has a average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasAverageSpending.addDomain(placeDealsWithMedicare);
        hasAverageSpending.addRange(XSD.decimal);
        ObjectProperty hasLocation = model.createObjectProperty(duURI + "hasLocation");
        hasLocation.addDomain(hospital);
        hasLocation.addRange(place);
    }

    public OntModel getModel() {
        return model;
    }

    public void addHospital(String ID, String name, String inputState, String amount) {
        if (Integer.parseInt(ID) < 0) throw new IllegalArgumentException("Only positive ID");
        // Gather classes and properties
        OntClass hospital = model.getOntClass(NS + project2.HospitalSpendingOntology.Classes.Hospital);
        Individual instance = hospital.createIndividual(NS + name);
        Individual stateInstance = model.getIndividual(NS + inputState);
        if (stateInstance == null) {
            OntClass state = model.getOntClass(NS + project2.HospitalSpendingOntology.Classes.State);
            stateInstance = state.createIndividual(NS + inputState);
            stateInstance.addComment(US_States.getFullStateName(inputState), "EN");
        }
        Property hasID = model.getProperty(NS + project2.HospitalSpendingOntology.Props.hasID);
        Property hasFacilityName = model.getProperty(NS + project2.HospitalSpendingOntology.Props.hasFacilityName);
        Property hasLocation = model.getProperty(duURI + "hasLocation");
        Property hasAverageSpending = model.getProperty(NS + project2.HospitalSpendingOntology.Props.hasAverageSpending);

        // Add to model
        model.add(instance, hasFacilityName, name);
        model.add(instance, hasID, ID);
        model.add(instance, hasLocation, stateInstance);
        model.add(instance, hasAverageSpending, amount);
    }

    public void addStateSpending(String stateName, String amount) {
        // Gather classes and properties
        OntClass state = model.getOntClass(NS + project2.HospitalSpendingOntology.Classes.State);
        Individual stateInstance = model.getIndividual(NS + stateName);
        if (stateInstance == null) {
            stateInstance = state.createIndividual(NS + stateName);
            stateInstance.addComment(US_States.getFullStateName(stateName), "EN");
        }
        Property hasAverageSpending = model.getProperty(NS + project2.HospitalSpendingOntology.Props.hasAverageSpending);
        model.add(stateInstance, hasAverageSpending, amount);
    }

    public void addNationSpending(String amount) {
        // Gather classes and properties
        OntClass nation = model.getOntClass(NS + project2.HospitalSpendingOntology.Classes.Nation);
        Individual instance = model.getIndividual(NS + "USA");
        if (instance == null) {
            instance = nation.createIndividual(NS + "USA");
            instance.addLabel("United State of America", "EN");
        }
        Property hasAverageSpending = model.getProperty(NS + project2.HospitalSpendingOntology.Props.hasAverageSpending);
        model.add(instance, hasAverageSpending, amount);
    }

    public static void main(String[] args) {
        HospitalOntology ontology = new HospitalOntology();
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
