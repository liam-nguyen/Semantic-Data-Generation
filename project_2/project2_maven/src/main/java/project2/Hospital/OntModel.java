package project2.Hospital;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

/**
 * Handle all ontology modeling
 */
public class OntModel {
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    public enum Classes {Hospital, State, Nation, Statistics2018}
    public enum Props {hasID, hasFacilityName, hasHospitalAverageSpending, isIDOf,
        isFacilityNameOf, hasEmergencyService, isEmergencyServiceOf, hasPhoneNumber, isHospitalAverageSpendingOf,
        hasScore, isScoreOf, hasRating, isRatingOf}

    public static String source = "https://data.medicare.gov/d/nrth-mfg3";
    public static String NS = source + "#";
    private static org.apache.jena.ontology.OntModel model;

    public OntModel() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", NS); // set namespace prefix
        model.setNsPrefix("du", duURI);
        populateModel();
    }

    public org.apache.jena.ontology.OntModel getModel() {return model;}

    public void populateModel() {
        // Create classes
        OntClass hospital = model.createClass(NS + Classes.Hospital);
        OntClass state = model.createClass(NS + Classes.State);
        OntClass nation = model.createClass(NS + Classes.Nation);
        OntClass statistics2018 = model.createClass(NS + Classes.Statistics2018);

        // Comments
        nation.addComment("A large body of people united by common descent, history, culture, or language, inhabiting a particular country or territory.", "EN");
        state.addComment("One of 50 states in US", "EN");

        /* Create properties */
        ObjectProperty hasID = model.createObjectProperty(NS + Props.hasID);
        hasID.addComment("has a particular facility ID", "EN");
        hasID.addDomain(hospital);
        hasID.setRDFType(OWL.FunctionalProperty);
        hasID.addRange(XSD.positiveInteger);
        ObjectProperty isIDOf = model.createObjectProperty(NS + Props.isIDOf);
        isIDOf.addInverseOf(hasID);
        isIDOf.addComment("is facility ID of", "EN");
        isIDOf.addDomain(XSD.positiveInteger);
        isIDOf.addRange(hospital);
        isIDOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasFacilityName = model.createObjectProperty(NS + Props.hasFacilityName);
        hasFacilityName.addComment("has the facility's name", "EN");
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(XSD.xstring);
        hasFacilityName.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isFacilityNameOf = model.createObjectProperty(NS + Props.isFacilityNameOf);
        isFacilityNameOf.addInverseOf(hasFacilityName);
        isFacilityNameOf.addComment("is facility's name of", "EN");
        isFacilityNameOf.addDomain(XSD.xstring);
        isFacilityNameOf.addRange(hospital);
        isFacilityNameOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasEmergencyService = model.createObjectProperty(NS + Props.hasEmergencyService);
        hasEmergencyService.addComment("A boolean whether a facility has emergency service", "EN");
        hasEmergencyService.addDomain(hospital);
        hasEmergencyService.addRange(XSD.xboolean);
        hasEmergencyService.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isEmergencyServiceOf = model.createObjectProperty(NS + Props.isEmergencyServiceOf);
        isEmergencyServiceOf.addInverseOf(hasFacilityName);
        isEmergencyServiceOf.addComment("is a boolean whether a facility has emergency service", "EN");
        isEmergencyServiceOf.addDomain(XSD.xboolean);
        isEmergencyServiceOf.addRange(hospital);

        ObjectProperty hasPhoneNumber = model.createObjectProperty(NS + Props.hasPhoneNumber);
        hasPhoneNumber.addComment("A phone number of a facility", "EN");
        hasPhoneNumber.addDomain(hospital);
        hasPhoneNumber.addRange(XSD.xstring);
        hasPhoneNumber.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isPhoneNumberOf = model.createObjectProperty(NS + Props.isFacilityNameOf);
        isPhoneNumberOf.addInverseOf(hasFacilityName);
        isPhoneNumberOf.addComment("is a boolean whether a facility has emergency service", "EN");
        isPhoneNumberOf.addDomain(XSD.xstring);
        isPhoneNumberOf.addRange(hospital);
        isPhoneNumberOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasHospitalAverageSpending = model.createObjectProperty(NS + Props.hasHospitalAverageSpending);
        hasHospitalAverageSpending.addComment("has a hospital's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasHospitalAverageSpending.addRange(XSD.decimal);
        hasHospitalAverageSpending.addDomain(statistics2018);
        ObjectProperty isHospitalAverageSpendingOf = model.createObjectProperty(NS + Props.isHospitalAverageSpendingOf);
        isHospitalAverageSpendingOf.addComment("is a hospital's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isHospitalAverageSpendingOf.addDomain(XSD.decimal);
        isHospitalAverageSpendingOf.addRange(statistics2018);

        ObjectProperty hasScore = model.createObjectProperty(NS + Props.hasScore);
        hasScore.addComment("has a hospital's score", "EN");
        hasScore.addRange(XSD.integer);
        hasScore.addDomain(statistics2018);
        ObjectProperty isScoreOf = model.createObjectProperty(NS + Props.isScoreOf);
        isScoreOf.addComment("is a hospital's score", "EN");
        isScoreOf.addDomain(statistics2018);
        isScoreOf.addRange(XSD.integer);

        ObjectProperty hasRating = model.createObjectProperty(NS + Props.hasRating);
        hasRating.addComment("has a hospital's rating from 1-5", "EN");
        Literal ratingLow = model.createTypedLiteral(1);
        Literal ratingHigh = model.createTypedLiteral(5);
        DataRange ratingRange = model.createDataRange(model.createList(ratingLow,ratingHigh));
        hasRating.addRange(ratingRange);
        hasRating.addDomain(statistics2018);
        ObjectProperty isRatingOf = model.createObjectProperty(NS + Props.isRatingOf);
        isRatingOf.addComment("is a hospital's rating from 1-5", "EN");
        isRatingOf.addDomain(ratingRange);
        isRatingOf.addRange(statistics2018);
    }

    public static void main(String[] args) {
        OntModel ontology = new OntModel();
        org.apache.jena.ontology.OntModel model = ontology.getModel();

        /* Examples */
        // How to get class and add individual instance
//        ontology.addHospital("10001", "SOUTHEAST ALABAMA MEDICAL CENTER", "AL", "20779");
//        ontology.addStateSpending("CA", "22304");
//        ontology.addNationSpending("21646");
        model.write(System.out);
//        System.out.println(model.getOntClass(NS + Class.Hospital));
    }
}
