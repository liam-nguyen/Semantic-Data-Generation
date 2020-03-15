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
    public enum Classes {MedicareMetadata, Hospital, State, Nation, Statistics2018, Location, Country}
    public enum Props {hasID, hasFacilityName, hasHospitalAverageSpending, isIDOf,
        isFacilityNameOf, hasEmergencyService, isEmergencyServiceOf, hasPhoneNumber, isHospitalAverageSpendingOf,
        hasScore, isScoreOf, hasRating, isRatingOf, hasLocation, isLocationOf, hasAddress, isAddressOf, hasZipcode,
        isZipcodeOf, hasCity, isCityOf, hasState, isStateOf, hasCountry, isCountryOf, hasType, isTypeOf, hasOwnership, 
        isOwnershipOf}

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
        OntClass location = model.createClass(NS + Classes.Location);
        OntClass country = model.createClass(NS + Classes.Country);
        
        // D.Phuc's
        OntClass medicaremetadata = model.createClass(NS + Classes.MedicareMetadata);
        // Subclass relationship
        medicaremetadata.addSubClass(hospital);
        hospital.addSuperClass(medicaremetadata);
        
        medicaremetadata.addSubClass(state);
        state.addSuperClass(medicaremetadata);
        
        medicaremetadata.addSubClass(country);
        country.addSuperClass(medicaremetadata);
        

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
        
        // D.Phuc's
        ObjectProperty hasLocation = model.createObjectProperty(NS + Props.hasLocation);
        hasLocation.addComment("has a hospital's location", "EN");
        hasLocation.addRange(location);
        hasLocation.addDomain(hospital);
        ObjectProperty isLocationOf = model.createObjectProperty(NS + Props.isLocationOf);
        isLocationOf.addComment("is a hospital's location", "EN");
        isLocationOf.addRange(hospital);
        isLocationOf.addDomain(location);
        
        ObjectProperty hasAddress = model.createObjectProperty(NS + Props.hasAddress);
        hasAddress.addComment("has a location's address", "EN");
        hasAddress.addRange(XSD.xstring);
        hasAddress.addDomain(location);
        ObjectProperty isAddressOf = model.createObjectProperty(NS + Props.isAddressOf);
        isAddressOf.addComment("is a location's address", "EN");
        isAddressOf.addRange(location);
        isAddressOf.addDomain(XSD.xstring);
        
        ObjectProperty hasZipcode = model.createObjectProperty(NS + Props.hasZipcode);
        hasZipcode.addComment("has a location's Zipcode", "EN");
        hasZipcode.addRange(XSD.xstring);
        hasZipcode.addDomain(location);
        ObjectProperty isZipcodeOf = model.createObjectProperty(NS + Props.isZipcodeOf);
        isZipcodeOf.addComment("is a location's Zipcode", "EN");
        isZipcodeOf.addRange(location);
        isZipcodeOf.addDomain(XSD.xstring);
        
        ObjectProperty hasCity = model.createObjectProperty(NS + Props.hasCity);
        hasCity.addComment("has a location's city", "EN");
        hasCity.addRange(XSD.xstring);
        hasCity.addDomain(location);
        ObjectProperty isCityOf = model.createObjectProperty(NS + Props.isCityOf);
        isCityOf.addComment("is a location's city", "EN");
        isCityOf.addRange(location);
        isCityOf.addDomain(XSD.xstring);
        
        ObjectProperty hasCountry = model.createObjectProperty(NS + Props.hasCountry);
        hasCountry.addComment("has a location's country", "EN");
        hasCountry.addRange(country);
        hasCountry.addDomain(location);
        ObjectProperty isCountryOf = model.createObjectProperty(NS + Props.isCountryOf);
        isCountryOf.addComment("is a location's country", "EN");
        isCountryOf.addRange(location);
        isCountryOf.addDomain(country);
        
        ObjectProperty hasState = model.createObjectProperty(NS + Props.hasState);
        hasState.addComment("has a location's state", "EN");
        hasState.addRange(state);
        hasState.addDomain(location);
        ObjectProperty isStateOf = model.createObjectProperty(NS + Props.isStateOf);
        isStateOf.addComment("is a location's state", "EN");
        isStateOf.addRange(location);
        isStateOf.addDomain(state);
        
        ObjectProperty hasType = model.createObjectProperty(NS + Props.hasType);
        hasType.addComment("has a hospital's type", "EN");
        hasType.addRange(XSD.xstring);
        hasType.addDomain(hospital);
        ObjectProperty isTypeOf = model.createObjectProperty(NS + Props.isTypeOf);
        isTypeOf.addComment("is a hospital's type", "EN");
        isTypeOf.addRange(hospital);
        isTypeOf.addDomain(XSD.xstring);
        
        ObjectProperty hasOwnership = model.createObjectProperty(NS + Props.hasOwnership);
        hasOwnership.addComment("has a hospital's type", "EN");
        hasOwnership.addRange(XSD.xstring);
        hasOwnership.addDomain(hospital);
        ObjectProperty isOwnershipOf = model.createObjectProperty(NS + Props.isOwnershipOf);
        isOwnershipOf.addComment("is a hospital's type", "EN");
        isOwnershipOf.addRange(hospital);
        isOwnershipOf.addDomain(XSD.xstring);
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
