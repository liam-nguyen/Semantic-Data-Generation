package project2.Ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

/**
 * This class is responsible to create Class and Properties for the ontology.
 */
public class OntModel {
    //== Static fields ==//
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    public static final String source = "https://data.medicare.gov/d/nrth-mfg3";
    public static final String NS = source + "#";

    //== Enum ==//
    public enum Class_Name {
        MedicareMetadata, Hospital, State, Statistics, Location, Country, Address,
        Zipcode, City, Type, Ownership, PhoneNumber, FacilityID, FacilityName,
        EmergencyServices, AverageMedicareSpending, Score, Rating, Year}
    public enum Prop_Name {
        hasFacilityID, hasFacilityName, hasAverageMedicareSpending, isFacilityIDOf,
        isFacilityNameOf, hasEmergencyService, isEmergencyServiceOf, hasPhoneNumber,
        isAverageMedicareSpendingOf, hasScore, isScoreOf, hasRating, isRatingOf, hasLocation,
        isLocationOf, hasAddress, isAddressOf, hasZipcode, isZipcodeOf, hasCity, isCityOf,
        hasState, isStateOf, hasCountry, isCountryOf, hasType, isTypeOf, hasOwnership,
        isOwnershipOf, hasYear, isYearOf, hasStatistics, isStatisticsOf
    }

    //== Instance fields ==//
    private org.apache.jena.ontology.OntModel model;

    //== Constructor ==//
    public OntModel() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", NS); // set namespace prefix
        model.setNsPrefix("du", duURI);
        populateModel();
    }

    //== Public methods ==//
    public org.apache.jena.ontology.OntModel getModel() {return model;}

    //== Private methods ==//
    private void populateModel() {
        // Create classes
        OntClass hospital = model.createClass(NS + Class_Name.Hospital);
        OntClass statistics = model.createClass(NS + Class_Name.Statistics);
//        OntClass location = model.createClass(NS + Classes.Location);
        OntClass country = model.createClass(NS + Class_Name.Country);
        OntClass address = model.createClass(NS + Class_Name.Address);
        OntClass zipcode = model.createClass(NS + Class_Name.Zipcode);
        OntClass city = model.createClass(NS + Class_Name.City);
        OntClass type = model.createClass(NS + Class_Name.Type);
        OntClass ownership = model.createClass(NS + Class_Name.Ownership);
        OntClass phonenumber = model.createClass(NS + Class_Name.PhoneNumber);
        OntClass facilityid = model.createClass(NS + Class_Name.FacilityID);
        OntClass facilityname = model.createClass(NS + Class_Name.FacilityName);
        OntClass emergencyservices = model.createClass(NS + Class_Name.EmergencyServices);
        OntClass averagemedicarespending = model.createClass(NS + Class_Name.AverageMedicareSpending);
        OntClass score = model.createClass(NS + Class_Name.Score);
        OntClass rating = model.createClass(NS + Class_Name.Rating);
//        OntClass year = model.createClass(NS + Class_Name.Year);
        OntClass medicaremetadata = model.createClass(NS + Class_Name.MedicareMetadata);

        // For State
        OntClass state = model.createClass(NS + Class_Name.State);


        // Class relationships
//        address.addRDFType(XSD.xstring);
//        zipcode.addRDFType(XSD.xstring);
//        city.addRDFType(XSD.xstring);
//        type.addRDFType(XSD.xstring);
//        ownership.addRDFType(XSD.xstring);
//        phonenumber.addRDFType(XSD.xstring);
//        facilityid.addRDFType(XSD.xint);
//        facilityname.addRDFType(XSD.xstring);
//        emergencyservices.addRDFType(XSD.xboolean);
//        averagemedicarespending.addRDFType(XSD.decimal);
//        score.addRDFType(XSD.xstring);
//        year.addRDFType(XSD.xint);
        medicaremetadata.addSubClass(hospital);
        medicaremetadata.addSubClass(state);
        medicaremetadata.addSubClass(country);

        // Intersecting classes
        hospital.convertToIntersectionClass(
                model.createList(type, ownership, score, rating,
                        phonenumber, facilityid,
                        facilityname, emergencyservices));
//        year.convertToIntersectionClass(
//                model.createList(averagemedicarespending, score, rating));
//        location.convertToIntersectionClass(
//        		model.createList(address, zipcode, city, state, country));

        // Comments
        state.addComment("One of 50 states in US", "EN");
        hospital.addComment("A hospital with general information and statistics", "EN");
        statistics.addComment("Various statistics for the hospital", "EN");
//        location.addComment("A collection of information regarding the hospital's location", "EN");
        country.addComment("A large body of people united by common descent, history, culture, or language, inhabiting a particular country or territory.", "EN");
        medicaremetadata.addComment("A collection of the hospital's statistical data", "EN");

        /* Create properties */
        ObjectProperty hasFacilityID = model.createObjectProperty(NS + Prop_Name.hasFacilityID);
        hasFacilityID.addComment("has a particular facility ID", "EN");
        hasFacilityID.addDomain(hospital);
        hasFacilityID.setRDFType(OWL.FunctionalProperty);
        hasFacilityID.addRange(XSD.positiveInteger);
        ObjectProperty isFacilityIDOf = model.createObjectProperty(NS + Prop_Name.isFacilityIDOf);
        isFacilityIDOf.addInverseOf(hasFacilityID);
        isFacilityIDOf.addComment("is facility ID of", "EN");
        isFacilityIDOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasFacilityName = model.createObjectProperty(NS + Prop_Name.hasFacilityName);
        hasFacilityName.addComment("has the facility's name", "EN");
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(XSD.xstring);
        hasFacilityName.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isFacilityNameOf = model.createObjectProperty(NS + Prop_Name.isFacilityNameOf);
        isFacilityNameOf.addInverseOf(hasFacilityName);
        isFacilityNameOf.addComment("is facility's name of", "EN");
        isFacilityNameOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasEmergencyService = model.createObjectProperty(NS + Prop_Name.hasEmergencyService);
        hasEmergencyService.addComment("A boolean whether a facility has emergency service", "EN");
        hasEmergencyService.addDomain(hospital);
        hasEmergencyService.addRange(XSD.xboolean);
        hasEmergencyService.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isEmergencyServiceOf = model.createObjectProperty(NS + Prop_Name.isEmergencyServiceOf);
        isEmergencyServiceOf.addInverseOf(hasEmergencyService);
        isEmergencyServiceOf.addComment("is a boolean whether a facility has emergency service", "EN");

        ObjectProperty hasPhoneNumber = model.createObjectProperty(NS + Prop_Name.hasPhoneNumber);
        hasPhoneNumber.addComment("A phone number of a facility", "EN");
        hasPhoneNumber.addDomain(hospital);
        hasPhoneNumber.addRange(XSD.xstring);
        hasPhoneNumber.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isPhoneNumberOf = model.createObjectProperty(NS + Prop_Name.isFacilityNameOf);
        isPhoneNumberOf.addInverseOf(hasPhoneNumber);
        isPhoneNumberOf.addComment("is a boolean whether a facility has emergency service", "EN");
        isPhoneNumberOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasAverageMedicareSpending = model.createObjectProperty(NS + Prop_Name.hasAverageMedicareSpending);
        hasAverageMedicareSpending.addComment("has a hospital's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasAverageMedicareSpending.addRange(XSD.xdouble);
        hasAverageMedicareSpending.addDomain(statistics);
        ObjectProperty isHospitalAverageSpendingOf = model.createObjectProperty(NS + Prop_Name.isAverageMedicareSpendingOf);
        isHospitalAverageSpendingOf.addComment("is a hospital's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isHospitalAverageSpendingOf.addInverseOf(hasAverageMedicareSpending);

        ObjectProperty hasScore = model.createObjectProperty(NS + Prop_Name.hasScore);
        hasScore.addComment("has a hospital's score", "EN");
        hasScore.addRange(XSD.integer);
        hasScore.addDomain(statistics);
        ObjectProperty isScoreOf = model.createObjectProperty(NS + Prop_Name.isScoreOf);
        isScoreOf.addComment("is a hospital's score", "EN");
        isScoreOf.addInverseOf(hasScore);

        ObjectProperty hasRating = model.createObjectProperty(NS + Prop_Name.hasRating);
        hasRating.addComment("has a hospital's rating from 1-5", "EN");
        Literal ratingLow = model.createTypedLiteral(1);
        Literal ratingHigh = model.createTypedLiteral(5);
        DataRange ratingRange = model.createDataRange(model.createList(ratingLow,ratingHigh));
        hasRating.addRange(ratingRange);
        hasRating.addDomain(statistics);
        ObjectProperty isRatingOf = model.createObjectProperty(NS + Prop_Name.isRatingOf);
        isRatingOf.addComment("is a hospital's rating from 1-5", "EN");
        isRatingOf.addInverseOf(hasRating);

        ObjectProperty hasYear = model.createObjectProperty(NS + Prop_Name.hasYear);
        hasYear.addComment("has a statistic's year", "EN");
//        hasYear.addRange(year);
        hasYear.addRange(XSD.xstring);
        hasYear.addDomain(statistics);
        ObjectProperty isYearOf = model.createObjectProperty(NS + Prop_Name.isYearOf);
        isYearOf.addComment("is a statistic's year", "EN");
        isYearOf.addInverseOf(hasYear);

//        ObjectProperty hasLocation = model.createObjectProperty(NS + Props.hasLocation);
//        hasLocation.addComment("has a hospital's location", "EN");
//        hasLocation.addRange(location);
//        hasLocation.addDomain(hospital);
//        ObjectProperty isLocationOf = model.createObjectProperty(NS + Props.isLocationOf);
//        isLocationOf.addComment("is a hospital's location", "EN");
//        isLocationOf.addInverseOf(hasLocation);

        ObjectProperty hasAddress = model.createObjectProperty(NS + Prop_Name.hasAddress);
        hasAddress.addComment("has a hospital location's address", "EN");
        hasAddress.addRange(address);
//        hasAddress.addDomain(location);
        hasAddress.addDomain(hospital);
        ObjectProperty isAddressOf = model.createObjectProperty(NS + Prop_Name.isAddressOf);
        isAddressOf.addComment("is a hospital location's address", "EN");
        isAddressOf.addInverseOf(hasAddress);

        ObjectProperty hasZipcode = model.createObjectProperty(NS + Prop_Name.hasZipcode);
        hasZipcode.addComment("has a hospital location's Zipcode", "EN");
        hasZipcode.addRange(zipcode);
//        hasZipcode.addDomain(location);
        hasZipcode.addDomain(hospital);
        ObjectProperty isZipcodeOf = model.createObjectProperty(NS + Prop_Name.isZipcodeOf);
        isZipcodeOf.addComment("is a hospital location's Zipcode", "EN");
        isZipcodeOf.addInverseOf(hasZipcode);

        ObjectProperty hasCity = model.createObjectProperty(NS + Prop_Name.hasCity);
        hasCity.addComment("has a hospital location's city", "EN");
        hasCity.addRange(city);
//        hasCity.addDomain(location);
        hasCity.addDomain(hospital);
        ObjectProperty isCityOf = model.createObjectProperty(NS + Prop_Name.isCityOf);
        isCityOf.addComment("is a hospital location's city", "EN");
        isCityOf.addInverseOf(hasCity);

        ObjectProperty hasCountry = model.createObjectProperty(NS + Prop_Name.hasCountry);
        hasCountry.addComment("has a hospital location's country", "EN");
        hasCountry.addRange(country);
//        hasCountry.addDomain(location);
        hasCountry.addDomain(hospital);
        ObjectProperty isCountryOf = model.createObjectProperty(NS + Prop_Name.isCountryOf);
        isCountryOf.addComment("is a hospital location's country", "EN");
        isCountryOf.addInverseOf(hasCountry);

        ObjectProperty hasState = model.createObjectProperty(NS + Prop_Name.hasState);
        hasState.addComment("has a hospital location's state", "EN");
        hasState.addRange(state);
//        hasState.addDomain(location);
        hasState.addDomain(hospital);
        ObjectProperty isStateOf = model.createObjectProperty(NS + Prop_Name.isStateOf);
        isStateOf.addComment("is a hospital location's state", "EN");
        isStateOf.addInverseOf(hasState);

        ObjectProperty hasType = model.createObjectProperty(NS + Prop_Name.hasType);
        hasType.addComment("has a hospital's type", "EN");
        hasType.addRange(type);
        hasType.addDomain(hospital);
        ObjectProperty isTypeOf = model.createObjectProperty(NS + Prop_Name.isTypeOf);
        isTypeOf.addComment("is a hospital's type", "EN");
        isTypeOf.addInverseOf(hasType);

        ObjectProperty hasOwnership = model.createObjectProperty(NS + Prop_Name.hasOwnership);
        hasOwnership.addComment("has a hospital's ownership", "EN");
        hasOwnership.addRange(ownership);
        hasOwnership.addDomain(hospital);
        ObjectProperty isOwnershipOf = model.createObjectProperty(NS + Prop_Name.isOwnershipOf);
        isOwnershipOf.addComment("is a hospital's ownership", "EN");
        isOwnershipOf.addInverseOf(hasOwnership);

        ObjectProperty hasStatistics = model.createObjectProperty(NS + Prop_Name.hasStatistics);
        hasStatistics.addComment("has MedicareMetadata's statistics", "EN");
        hasStatistics.addRange(statistics);
        hasStatistics.addDomain(medicaremetadata);
        ObjectProperty isStatisticsOf = model.createObjectProperty(NS + Prop_Name.isStatisticsOf);
        isStatisticsOf.addComment("is MedicareMetadata's statistics", "EN");
        isStatisticsOf.addInverseOf(hasStatistics);
    }

//    public static void main(String[] args) {
//        OntModel ontology = new OntModel();
//        org.apache.jena.ontology.OntModel model = ontology.getModel();
//
//        model.write(System.out);
//    }
}
