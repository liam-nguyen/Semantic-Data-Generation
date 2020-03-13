package project2;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.XSD;

/**
 * A model for medicare hospital spending dataset
 */
public class HospitalSpendingModel {
    private static String source = "https://data.medicare.gov/d/nrth-mfg3";
    private static String NS = source + "#";
    public static enum Classes {Hospital, Place, State, USA, placeDealsWithMedicare}
    public static enum Props {FacilityID, FacilityName, AverageSpending, hasID, hasFacilityName, hasAverageSpending}

    private HospitalSpendingModel() {}

    public static OntModel createModel() {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules

        // Create classes
        OntClass hospital = model.createClass(NS + Classes.Hospital);
        OntClass state = model.createClass(NS + Classes.State);
        OntClass USA = model.createClass(NS + Classes.USA);
        OntClass place = model.createClass(NS + Classes.Place);
        OntClass placeDealsWithMedicare = model.createClass(NS + Classes.placeDealsWithMedicare);

        //Establish class relationships
        placeDealsWithMedicare.addSubClass(hospital);
        placeDealsWithMedicare.addSubClass(state);
        placeDealsWithMedicare.addSubClass(USA);
        place.addSubClass(state);
        place.addSubClass(USA);

        /* Create properties */
        ObjectProperty facilityID = model.createObjectProperty(NS + Props.FacilityID);
        facilityID.addRange(XSD.positiveInteger);
        ObjectProperty facilityName = model.createObjectProperty(NS + Props.FacilityName);
        facilityName.addRange(XSD.xstring);
        ObjectProperty avgSpending = model.createObjectProperty(NS + Props.AverageSpending);
        avgSpending.addRange(XSD.integer);
        ObjectProperty hasID = model.createObjectProperty(NS + Props.hasID);
        hasID.addDomain(hospital);
        hasID.addRange(facilityID);
        ObjectProperty hasFacilityName = model.createObjectProperty(NS + Props.hasFacilityName);
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(facilityName);
        ObjectProperty hasAverageSpending = model.createObjectProperty(NS + Props.hasAverageSpending);
        hasAverageSpending.addDomain(placeDealsWithMedicare);
        hasAverageSpending.addRange(avgSpending);
        ObjectProperty hasLocation = model.createObjectProperty("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#hasLocation");
        hasLocation.addDomain(hospital);
        hasLocation.addRange(place);

        return model;
    }

    public static void main(String[] args) {
        var model = HospitalSpendingModel.createModel();
        model.write(System.out);
//        System.out.println(model.getOntClass(NS + Class.Hospital));
    }
}
