package project2.Hospital;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

/**
 * Handle all ontology modeling
 */
public class OntModel {
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    public enum Classes {MedicareMetadata, Hospital, State, Nation, Statistics, Location, Country, Address,
    	Zipcode, City, Type, Ownership, PhoneNumber, FacilityID, FacilityName, EmergencyServices, AverageMedicareSpending,
    	Score, Rating, Year} // Remove Nation
    public enum Props {hasFacilityID, hasFacilityName, hasAverageMedicareSpending, isFacilityIDOf,
        isFacilityNameOf, hasEmergencyService, isEmergencyServiceOf, hasPhoneNumber, isAverageMedicareSpendingOf,
        hasScore, isScoreOf, hasRating, isRatingOf, hasLocation, isLocationOf, hasAddress, isAddressOf, hasZipcode,
        isZipcodeOf, hasCity, isCityOf, hasState, isStateOf, hasCountry, isCountryOf, hasType, isTypeOf, hasOwnership, 
        isOwnershipOf, hasYear, isYearOf, hasStatistics, isStatisticsOf}

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
//        OntClass nation = model.createClass(NS + Classes.Nation);
        OntClass statistics = model.createClass(NS + Classes.Statistics);
        OntClass location = model.createClass(NS + Classes.Location);
        OntClass country = model.createClass(NS + Classes.Country);
        OntClass address = model.createClass(NS + Classes.Address);
        OntClass zipcode = model.createClass(NS + Classes.Zipcode);
        OntClass city = model.createClass(NS + Classes.City);
        OntClass type = model.createClass(NS + Classes.Type);
        OntClass ownership = model.createClass(NS + Classes.Ownership);
        OntClass phonenumber = model.createClass(NS + Classes.PhoneNumber);
        OntClass facilityid = model.createClass(NS + Classes.FacilityID);
        OntClass facilityname = model.createClass(NS + Classes.FacilityName);
        OntClass emergencyservices = model.createClass(NS + Classes.EmergencyServices);
        OntClass averagemedicarespending = model.createClass(NS + Classes.AverageMedicareSpending);
        OntClass score = model.createClass(NS + Classes.Score);
        OntClass rating = model.createClass(NS + Classes.Rating);
        OntClass year = model.createClass(NS + Classes.Year);
        
        // Class range for appropriate classes
        address.addRDFType(XSD.xstring);
        zipcode.addRDFType(XSD.xstring);
        city.addRDFType(XSD.xstring);
        type.addRDFType(XSD.xstring);
        ownership.addRDFType(XSD.xstring);
        phonenumber.addRDFType(XSD.xstring);
        facilityid.addRDFType(XSD.xint);
        facilityname.addRDFType(XSD.xstring);
        emergencyservices.addRDFType(XSD.xboolean);
        averagemedicarespending.addRDFType(XSD.decimal);
        score.addRDFType(XSD.xstring);
        year.addRDFType(XSD.xint);
        
        // D.Phuc's
        OntClass medicaremetadata = model.createClass(NS + Classes.MedicareMetadata);
        // Subclass relationship
        medicaremetadata.addSubClass(hospital);
        medicaremetadata.addSubClass(state);
        medicaremetadata.addSubClass(country);
        
        // Intersecting classes
        hospital.convertToIntersectionClass(
        		model.createList(
        			new RDFNode[] {type, ownership, phonenumber, facilityid,
        						   facilityname, emergencyservices}));
        year.convertToIntersectionClass(
        		model.createList(
        			new RDFNode[] {averagemedicarespending, score, rating}));
        location.convertToIntersectionClass(
        		model.createList(
        			new RDFNode[] {address, zipcode, city, state, country}));
        
        // Comments
//        nation.addComment("", "EN");
        state.addComment("One of 50 states in US", "EN");
        hospital.addComment("A hospital with general information and statistics", "EN");
        statistics.addComment("Various statistics for the hospital", "EN");
        location.addComment("A collection of information regarding the hospital's location", "EN");
        country.addComment("A large body of people united by common descent, history, culture, or language, inhabiting a particular country or territory.", "EN");
        medicaremetadata.addComment("A collection of the hospital's statistical data", "EN");

        /* Create properties */
        ObjectProperty hasFacilityID = model.createObjectProperty(NS + Props.hasFacilityID);
        hasFacilityID.addComment("has a particular facility ID", "EN");
        hasFacilityID.addDomain(hospital);
        hasFacilityID.setRDFType(OWL.FunctionalProperty);
        hasFacilityID.addRange(XSD.positiveInteger);
        ObjectProperty isFacilityIDOf = model.createObjectProperty(NS + Props.isFacilityIDOf);
        isFacilityIDOf.addInverseOf(hasFacilityID);
        isFacilityIDOf.addComment("is facility ID of", "EN");
        isFacilityIDOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasFacilityName = model.createObjectProperty(NS + Props.hasFacilityName);
        hasFacilityName.addComment("has the facility's name", "EN");
        hasFacilityName.addDomain(hospital);
        hasFacilityName.addRange(XSD.xstring);
        hasFacilityName.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isFacilityNameOf = model.createObjectProperty(NS + Props.isFacilityNameOf);
        isFacilityNameOf.addInverseOf(hasFacilityName);
        isFacilityNameOf.addComment("is facility's name of", "EN");
//        isFacilityNameOf.addDomain(XSD.xstring);
//        isFacilityNameOf.addRange(hospital);
        isFacilityNameOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasEmergencyService = model.createObjectProperty(NS + Props.hasEmergencyService);
        hasEmergencyService.addComment("A boolean whether a facility has emergency service", "EN");
        hasEmergencyService.addDomain(hospital);
        hasEmergencyService.addRange(XSD.xboolean);
        hasEmergencyService.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isEmergencyServiceOf = model.createObjectProperty(NS + Props.isEmergencyServiceOf);
        isEmergencyServiceOf.addInverseOf(hasEmergencyService);
        isEmergencyServiceOf.addComment("is a boolean whether a facility has emergency service", "EN");
//        isEmergencyServiceOf.addDomain(XSD.xboolean);
//        isEmergencyServiceOf.addRange(hospital);

        ObjectProperty hasPhoneNumber = model.createObjectProperty(NS + Props.hasPhoneNumber);
        hasPhoneNumber.addComment("A phone number of a facility", "EN");
        hasPhoneNumber.addDomain(hospital);
        hasPhoneNumber.addRange(XSD.xstring);
        hasPhoneNumber.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isPhoneNumberOf = model.createObjectProperty(NS + Props.isFacilityNameOf);
        isPhoneNumberOf.addInverseOf(hasPhoneNumber);
        isPhoneNumberOf.addComment("is a boolean whether a facility has emergency service", "EN");
//        isPhoneNumberOf.addDomain(XSD.xstring);
//        isPhoneNumberOf.addRange(hospital);
        isPhoneNumberOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasAverageMedicareSpending = model.createObjectProperty(NS + Props.hasAverageMedicareSpending);
        hasAverageMedicareSpending.addComment("has a hospital's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasAverageMedicareSpending.addRange(XSD.decimal);
        hasAverageMedicareSpending.addDomain(statistics);
        ObjectProperty isHospitalAverageSpendingOf = model.createObjectProperty(NS + Props.isAverageMedicareSpendingOf);
        isHospitalAverageSpendingOf.addComment("is a hospital's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isHospitalAverageSpendingOf.addInverseOf(hasAverageMedicareSpending);
//        isHospitalAverageSpendingOf.addDomain(XSD.decimal);
//        isHospitalAverageSpendingOf.addRange(statistics2018);

        ObjectProperty hasScore = model.createObjectProperty(NS + Props.hasScore);
        hasScore.addComment("has a hospital's score", "EN");
        hasScore.addRange(XSD.integer);
        hasScore.addDomain(statistics);
        ObjectProperty isScoreOf = model.createObjectProperty(NS + Props.isScoreOf);
        isScoreOf.addComment("is a hospital's score", "EN");
        isScoreOf.addInverseOf(hasScore);
//        isScoreOf.addDomain(statistics2018);
//        isScoreOf.addRange(XSD.integer);

        ObjectProperty hasRating = model.createObjectProperty(NS + Props.hasRating);
        hasRating.addComment("has a hospital's rating from 1-5", "EN");
        Literal ratingLow = model.createTypedLiteral(1);
        Literal ratingHigh = model.createTypedLiteral(5);
        DataRange ratingRange = model.createDataRange(model.createList(ratingLow,ratingHigh));
        hasRating.addRange(ratingRange);
        hasRating.addDomain(statistics);
        ObjectProperty isRatingOf = model.createObjectProperty(NS + Props.isRatingOf);
        isRatingOf.addComment("is a hospital's rating from 1-5", "EN");
        isRatingOf.addInverseOf(hasRating);
        
        // D.Phuc's
        ObjectProperty hasYear = model.createObjectProperty(NS + Props.hasYear);
        hasYear.addComment("has a statistic's year", "EN");
        hasYear.addRange(year);
        hasYear.addDomain(statistics);
        ObjectProperty isYearOf = model.createObjectProperty(NS + Props.isYearOf);
        isYearOf.addComment("is a statistic's year", "EN");
        isYearOf.addInverseOf(hasYear);
        
        ObjectProperty hasLocation = model.createObjectProperty(NS + Props.hasLocation);
        hasLocation.addComment("has a hospital's location", "EN");
        hasLocation.addRange(location);
        hasLocation.addDomain(hospital);
        ObjectProperty isLocationOf = model.createObjectProperty(NS + Props.isLocationOf);
        isLocationOf.addComment("is a hospital's location", "EN");
        isLocationOf.addInverseOf(hasLocation);
        
        ObjectProperty hasAddress = model.createObjectProperty(NS + Props.hasAddress);
        hasAddress.addComment("has a hospital location's address", "EN");
        hasAddress.addRange(address);
        hasAddress.addDomain(location);
        ObjectProperty isAddressOf = model.createObjectProperty(NS + Props.isAddressOf);
        isAddressOf.addComment("is a hospital location's address", "EN");
        isAddressOf.addInverseOf(hasAddress);
        
        ObjectProperty hasZipcode = model.createObjectProperty(NS + Props.hasZipcode);
        hasZipcode.addComment("has a hospital location's Zipcode", "EN");
        hasZipcode.addRange(zipcode);
        hasZipcode.addDomain(location);
        ObjectProperty isZipcodeOf = model.createObjectProperty(NS + Props.isZipcodeOf);
        isZipcodeOf.addComment("is a hospital location's Zipcode", "EN");
        isZipcodeOf.addInverseOf(hasZipcode);
        
        ObjectProperty hasCity = model.createObjectProperty(NS + Props.hasCity);
        hasCity.addComment("has a hospital location's city", "EN");
        hasCity.addRange(city);
        hasCity.addDomain(location);
        ObjectProperty isCityOf = model.createObjectProperty(NS + Props.isCityOf);
        isCityOf.addComment("is a hospital location's city", "EN");
        isCityOf.addInverseOf(hasCity);
        
        ObjectProperty hasCountry = model.createObjectProperty(NS + Props.hasCountry);
        hasCountry.addComment("has a hospital location's country", "EN");
        hasCountry.addRange(country);
        hasCountry.addDomain(location);
        ObjectProperty isCountryOf = model.createObjectProperty(NS + Props.isCountryOf);
        isCountryOf.addComment("is a hospital location's country", "EN");
        isCountryOf.addInverseOf(hasCountry);
        
        ObjectProperty hasState = model.createObjectProperty(NS + Props.hasState);
        hasState.addComment("has a hospital location's state", "EN");
        hasState.addRange(state);
        hasState.addDomain(location);
        ObjectProperty isStateOf = model.createObjectProperty(NS + Props.isStateOf);
        isStateOf.addComment("is a hospital location's state", "EN");
        isStateOf.addInverseOf(hasState);
        
        ObjectProperty hasType = model.createObjectProperty(NS + Props.hasType);
        hasType.addComment("has a hospital's type", "EN");
        hasType.addRange(type);
        hasType.addDomain(hospital);
        ObjectProperty isTypeOf = model.createObjectProperty(NS + Props.isTypeOf);
        isTypeOf.addComment("is a hospital's type", "EN");
        isTypeOf.addInverseOf(hasType);
        
        ObjectProperty hasOwnership = model.createObjectProperty(NS + Props.hasOwnership);
        hasOwnership.addComment("has a hospital's ownership", "EN");
        hasOwnership.addRange(ownership);
        hasOwnership.addDomain(hospital);
        ObjectProperty isOwnershipOf = model.createObjectProperty(NS + Props.isOwnershipOf);
        isOwnershipOf.addComment("is a hospital's ownership", "EN");
        isOwnershipOf.addInverseOf(hasOwnership);
        
        ObjectProperty hasStatistics = model.createObjectProperty(NS + Props.hasStatistics);
        hasStatistics.addComment("has MedicareMetadata's statistics", "EN");
        hasStatistics.addRange(statistics);
        hasStatistics.addDomain(medicaremetadata);
        ObjectProperty isStatisticsOf = model.createObjectProperty(NS + Props.isStatisticsOf);
        isStatisticsOf.addComment("is MedicareMetadata's statistics", "EN");
        isStatisticsOf.addInverseOf(hasStatistics);
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
