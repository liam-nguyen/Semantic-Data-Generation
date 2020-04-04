package project2.Ontology;

import lombok.Getter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;
import project2.Helper.CSVData;
import project2.Helper.Hospital;
import project2.Helper.State;
import project2.Util.Stopwatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class is responsible to create Class and Properties for the ontology.
 */
public class OurModel {
    //== Enum ==//
    public enum Class_Name {
        MedicareMetadata, Hospital, State, Statistics, Location, Country, Address,
        Zipcode, City, Type, Ownership, PhoneNumber, FacilityID, FacilityName,
        EmergencyServices, AverageMedicareSpending, Score, Rating, Year;

        public String getURI() {
            return sourceURI + this.name();
        }
    }

    public enum Prop_Name {
        hasFacilityID, hasFacilityName, hasHospitalAverageMedicareSpending, isFacilityIDOf,
        isFacilityNameOf, hasEmergencyService, isEmergencyServiceOf, hasPhoneNumber,
        isAverageMedicareSpendingOf, hasScore, isScoreOf, hasRating, isRatingOf, hasLocation,
        isLocationOf, hasAddress, isAddressOf, hasZipcode, isZipcodeOf, hasCity, isCityOf,
        hasState, isStateOf, hasCountry, isCountryOf, hasType, isTypeOf, hasOwnership,
        isOwnershipOf, hasYear, isYearOf, hasStatistics, isStatisticsOf, hasStateAverageMedicareSpending,
        hasNationalAverageSpending;

        public String getURI() {
            return sourceURI + this.name();
        }
    }

    //== Static fields ==//
    @Getter
    private static final String duURI = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    @Getter
    private static final String sourceURI = "https://data.medicare.gov/d/nrth-mfg3#";
    private static final String owlFileName = "hospital.owl";

    @Getter
    private static OntModel model;

    private static Map<String, Hospital> hospitals; // String = facilityID
    private static Map<String, State> states; // String = full State's name
    private static String nationalAverage; // National Average Medicare Spending

    @Getter
    private static Map<String, OntClass> classCache;
    @Getter
    private static Map<String, ObjectProperty> propCache;
    private static Map<String, Individual> individualCache; // Cache for faster Ontology's instance look-up time

    //== Static Initialize ==//
    static {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", sourceURI); // set namespace prefix
        model.setNsPrefix("du", duURI);

        classCache = new HashMap<>();
        propCache = new HashMap<>();
        individualCache = new HashMap<>();

        states = CSVData.getStates();
        hospitals = CSVData.getHospitals();
        nationalAverage = CSVData.getNationalAverage();

        addClasses();
        addProps();

        try {
            addInstances(Hospital::isValid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //== Public methods ==//
    //== == Build ontology instances == ==//
    public static void addInstances(Predicate<Hospital> hospitalPred) throws UnsupportedEncodingException {
        Stopwatch timer;
        List<Hospital> filteredHospitals = hospitals.values().stream().filter(hospitalPred).collect(Collectors.toList());

        System.out.println("Building Ontology, please wait...It might take some time...");
        System.out.println("All hospitals size: " + hospitals.size());
        System.out.println("Filtered hospitals size: " + filteredHospitals.size());

        timer = new Stopwatch();
        addHospitalToModel(filteredHospitals);
        System.out.println("Building Hospital Ontology - Complete - Time: " + timer.elapsedTime());

        timer = new Stopwatch();
        addStateToModel(new ArrayList<>(states.values()));
        System.out.println("Building State Ontology - Complete - Time: " + timer.elapsedTime());

        addNationToModel();
    }


    private static void addHospitalToModel(List<Hospital> hospitals) throws UnsupportedEncodingException {
        final String STAT_YEAR = "2018";
        int total = hospitals.size();
//        Stopwatch timer;

        OntClass hospital = createClassIfAbsent(Class_Name.Hospital.getURI());
        OntClass state = createClassIfAbsent(Class_Name.State.getURI());
        OntClass country = createClassIfAbsent(Class_Name.Country.getURI());
        OntClass type = createClassIfAbsent(Class_Name.Type.getURI());
        OntClass ownership = createClassIfAbsent(Class_Name.Ownership.getURI());

        ObjectProperty hasID = createPropIfAbsent(Prop_Name.hasFacilityID.getURI());
        ObjectProperty hasFacilityName = createPropIfAbsent(OurModel.Prop_Name.hasFacilityName.getURI());
        ObjectProperty hasEmergencyService = createPropIfAbsent(OurModel.Prop_Name.hasEmergencyService.getURI());
        ObjectProperty hasPhoneNumber = createPropIfAbsent(OurModel.Prop_Name.hasPhoneNumber.getURI());
        ObjectProperty hasScore = createPropIfAbsent(OurModel.Prop_Name.hasScore.getURI());
        ObjectProperty hasRating = createPropIfAbsent(OurModel.Prop_Name.hasRating.getURI());
        ObjectProperty hasHospitalAverageMedicareSpending = createPropIfAbsent(OurModel.Prop_Name.hasHospitalAverageMedicareSpending.getURI());
        ObjectProperty hasCity = createPropIfAbsent(OurModel.Prop_Name.hasCity.getURI());
        ObjectProperty hasState = createPropIfAbsent(OurModel.Prop_Name.hasState.getURI());
        ObjectProperty hasCountry = createPropIfAbsent(OurModel.Prop_Name.hasCountry.getURI());
        ObjectProperty hasZipCode = createPropIfAbsent(OurModel.Prop_Name.hasZipcode.getURI());
        ObjectProperty hasAddress = createPropIfAbsent(OurModel.Prop_Name.hasAddress.getURI());
        ObjectProperty hasOwnership = createPropIfAbsent(OurModel.Prop_Name.hasOwnership.getURI());
        ObjectProperty hasType = createPropIfAbsent(OurModel.Prop_Name.hasType.getURI());
        ObjectProperty hasYear = createPropIfAbsent(OurModel.Prop_Name.hasYear.getURI());


        for (Hospital h : hospitals) {
//            timer = new Stopwatch();
            Individual hospitalInstance = hospital.createIndividual(OurModel.sourceURI + h.getIDAsURI());
            Individual stateInstance = createIndividualIfAbsent(state, OurModel.sourceURI + h.getStateAsURI());
            Individual usa = createIndividualIfAbsent(country, OurModel.sourceURI + h.getCountryAsURI());

//            Individual averagemedicarespendingInstance = getIndividual(averagemedicarespending, OntModel.NS + h.getMedicareAmountAsURI());
//            Individual scoreInstance = getIndividual(score, OntModel.NS + h.getScoreAsURI());
//            Individual ratingInstance = getIndividual(rating, OntModel.NS + h.getRatingAsURI());
//            Individual yearInstance = getIndividual(year, OntModel.NS + "2018");
//            Individual statisticsInstance = getIndividual(statistics, OurModel.sourceURI + h.getIDAsURI() + yearStr);
//            System.out.println("Individual time: " + timer.elapsedTime());

//            timer = new Stopwatch();

//            System.out.println("Property time: " + timer.elapsedTime());

//            timer = new Stopwatch();
            model.add(hospitalInstance, hasFacilityName, h.getName());
            model.add(hospitalInstance, hasID, h.getID());
            model.add(hospitalInstance, hasEmergencyService, String.valueOf(h.getHasEmergency()));
            model.add(hospitalInstance, hasPhoneNumber, h.getPhoneNumber());
            model.add(hospitalInstance, hasAddress, h.getAddress());
            model.add(hospitalInstance, hasCountry, usa);
            model.add(hospitalInstance, hasCity, h.getCity());
            model.add(hospitalInstance, hasState, stateInstance);
            model.add(hospitalInstance, hasZipCode, h.getZipcode());
            model.add(hospitalInstance, hasScore, model.createTypedLiteral(h.getScoreParsed().orElse(-1)));
            model.add(hospitalInstance, hasRating, model.createTypedLiteral(h.getRatingParsed().orElse(-1)));
            model.add(hospitalInstance, hasHospitalAverageMedicareSpending, model.createTypedLiteral(h.getMedicareAmountParsed().orElse(-1.0)));
            model.add(hospitalInstance, hasYear, model.createTypedLiteral(STAT_YEAR));

            model.add(hospitalInstance, hasOwnership, model.createTypedLiteral(h.getOwnership()));
            model.add(hospitalInstance, hasType, model.createTypedLiteral(h.getType()));
//            System.out.println("Model Adding time: " + timer.elapsedTime());
            System.out.println("Remained: " + (--total) + " - Added " + h.getID());
        }
    }

    private static void addStateToModel(List<State> states) {
        int total = states.size();
        OntClass state = createClassIfAbsent(OurModel.Class_Name.State.getURI());
        ObjectProperty hasStateAverageMedicareSpending = createPropIfAbsent(Prop_Name.hasStateAverageMedicareSpending.getURI());

        for (State s : states) {
            Individual stateInstance = createIndividualIfAbsent(state, OurModel.sourceURI + s.getAbbr());
            stateInstance.addComment(s.getFullStateName(), "EN");

            model.add(stateInstance, hasStateAverageMedicareSpending, s.getAverageMedicareAmount());
            System.out.println("Remained: " + (--total) + " - Added " + s.getAbbr());
        }
    }

    private static void addNationToModel() {
        OntClass country = createClassIfAbsent(OurModel.Class_Name.Country.getURI());
        ObjectProperty hasNationalAverageSpending = createPropIfAbsent(OurModel.Prop_Name.hasNationalAverageSpending.getURI());

        Individual nationInstance = country.createIndividual(OurModel.sourceURI + "USA");
        nationInstance.addLabel("United State of America", "EN");
        model.add(nationInstance, hasNationalAverageSpending, nationalAverage);
        System.out.println("Added national averaged.");
    }

    //== == Utilities == ==//
    public static void writeModelToFile(String fileName) throws IOException {
        Path filePath = FileSystems.getDefault().getPath("").toAbsolutePath()
                .resolve("deliverables")
                .resolve(owlFileName);

        BufferedWriter out = new BufferedWriter(new FileWriter(filePath.toString()));

        System.out.println("Writing to file " + owlFileName + " ...");
        Stopwatch timer = new Stopwatch();
        model.write(out);
        System.out.println("Writing to file - Complete - Time: " + timer.elapsedTime());
    }

    public static void writeModelToFile() throws IOException {
        writeModelToFile(owlFileName);
    }

    //== Private methods ==//
    private static void addClasses() {
        // Create classes
        OntClass hospital = createClassIfAbsent(sourceURI + Class_Name.Hospital);
        OntClass statistics = createClassIfAbsent(sourceURI + Class_Name.Statistics);
//        OntClass location = model.createClass(NS + Classes.Location);
        OntClass country = createClassIfAbsent(sourceURI + Class_Name.Country);
        OntClass address = createClassIfAbsent(sourceURI + Class_Name.Address);
        OntClass zipcode = createClassIfAbsent(sourceURI + Class_Name.Zipcode);
        OntClass city = createClassIfAbsent(sourceURI + Class_Name.City);
        OntClass type = createClassIfAbsent(sourceURI + Class_Name.Type);
        OntClass ownership = createClassIfAbsent(sourceURI + Class_Name.Ownership);
        OntClass phonenumber = createClassIfAbsent(sourceURI + Class_Name.PhoneNumber);
        OntClass facilityid = createClassIfAbsent(sourceURI + Class_Name.FacilityID);
        OntClass facilityname = createClassIfAbsent(sourceURI + Class_Name.FacilityName);
        OntClass emergencyservices = createClassIfAbsent(sourceURI + Class_Name.EmergencyServices);
        OntClass averagemedicarespending = createClassIfAbsent(sourceURI + Class_Name.AverageMedicareSpending);
        OntClass score = createClassIfAbsent(sourceURI + Class_Name.Score);
        OntClass rating = createClassIfAbsent(sourceURI + Class_Name.Rating);
//        OntClass year = model.createClass(NS + Class_Name.Year);
        OntClass medicaremetadata = createClassIfAbsent(sourceURI + Class_Name.MedicareMetadata);

        // For State
        OntClass state = createClassIfAbsent(sourceURI + Class_Name.State);

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
    }

    private static void addProps() {
        /* Create properties */
        ObjectProperty hasFacilityID = createPropIfAbsent(sourceURI + Prop_Name.hasFacilityID);
        hasFacilityID.addComment("has a particular facility ID", "EN");
        hasFacilityID.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        hasFacilityID.setRDFType(OWL.FunctionalProperty);
        hasFacilityID.addRange(XSD.positiveInteger);
        ObjectProperty isFacilityIDOf = createPropIfAbsent(sourceURI + Prop_Name.isFacilityIDOf);
        isFacilityIDOf.addInverseOf(hasFacilityID);
        isFacilityIDOf.addComment("is facility ID of", "EN");
        isFacilityIDOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasFacilityName = createPropIfAbsent(sourceURI + Prop_Name.hasFacilityName);
        hasFacilityName.addComment("has the facility's name", "EN");
        hasFacilityName.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        hasFacilityName.addRange(XSD.xstring);
        hasFacilityName.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isFacilityNameOf = createPropIfAbsent(sourceURI + Prop_Name.isFacilityNameOf);
        isFacilityNameOf.addInverseOf(hasFacilityName);
        isFacilityNameOf.addComment("is facility's name of", "EN");
        isFacilityNameOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasEmergencyService = createPropIfAbsent(sourceURI + Prop_Name.hasEmergencyService);
        hasEmergencyService.addComment("A boolean whether a facility has emergency service", "EN");
        hasEmergencyService.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        hasEmergencyService.addRange(XSD.xboolean);
        hasEmergencyService.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isEmergencyServiceOf = createPropIfAbsent(sourceURI + Prop_Name.isEmergencyServiceOf);
        isEmergencyServiceOf.addInverseOf(hasEmergencyService);
        isEmergencyServiceOf.addComment("is a boolean whether a facility has emergency service", "EN");

        ObjectProperty hasPhoneNumber = createPropIfAbsent(sourceURI + Prop_Name.hasPhoneNumber);
        hasPhoneNumber.addComment("A phone number of a facility", "EN");
        hasPhoneNumber.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        hasPhoneNumber.addRange(XSD.xstring);
        hasPhoneNumber.setRDFType(OWL.FunctionalProperty);
        ObjectProperty isPhoneNumberOf = createPropIfAbsent(sourceURI + Prop_Name.isFacilityNameOf);
        isPhoneNumberOf.addInverseOf(hasPhoneNumber);
        isPhoneNumberOf.addComment("is a boolean whether a facility has emergency service", "EN");
        isPhoneNumberOf.setRDFType(OWL.InverseFunctionalProperty);

        ObjectProperty hasAverageMedicareSpending = createPropIfAbsent(sourceURI + Prop_Name.hasHospitalAverageMedicareSpending);
        hasAverageMedicareSpending.addComment("has a hospital's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasAverageMedicareSpending.addRange(XSD.xdouble);
        hasAverageMedicareSpending.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isHospitalAverageSpendingOf = createPropIfAbsent(sourceURI + Prop_Name.isAverageMedicareSpendingOf);
        isHospitalAverageSpendingOf.addComment("is a hospital's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isHospitalAverageSpendingOf.addInverseOf(hasAverageMedicareSpending);

        ObjectProperty hasScore = createPropIfAbsent(sourceURI + Prop_Name.hasScore);
        hasScore.addComment("has a hospital's score", "EN");
        hasScore.addRange(XSD.integer);
        hasScore.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isScoreOf = createPropIfAbsent(sourceURI + Prop_Name.isScoreOf);
        isScoreOf.addComment("is a hospital's score", "EN");
        isScoreOf.addInverseOf(hasScore);

        ObjectProperty hasRating = createPropIfAbsent(sourceURI + Prop_Name.hasRating);
        hasRating.addComment("has a hospital's rating from 1-5", "EN");
        Literal ratingLow = model.createTypedLiteral(1);
        Literal ratingHigh = model.createTypedLiteral(5);
        DataRange ratingRange = model.createDataRange(model.createList(ratingLow, ratingHigh));
        hasRating.addRange(ratingRange);
        hasRating.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isRatingOf = createPropIfAbsent(sourceURI + Prop_Name.isRatingOf);
        isRatingOf.addComment("is a hospital's rating from 1-5", "EN");
        isRatingOf.addInverseOf(hasRating);

        ObjectProperty hasYear = createPropIfAbsent(sourceURI + Prop_Name.hasYear);
        hasYear.addComment("has a statistic's year", "EN");
//        hasYear.addRange(year);
        hasYear.addRange(XSD.xstring);
        hasYear.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isYearOf = createPropIfAbsent(sourceURI + Prop_Name.isYearOf);
        isYearOf.addComment("is a statistic's year", "EN");
        isYearOf.addInverseOf(hasYear);

//        ObjectProperty hasLocation = model.createObjectProperty(NS + Props.hasLocation);
//        hasLocation.addComment("has a hospital's location", "EN");
//        hasLocation.addRange(location);
//        hasLocation.addDomain(hospital);
//        ObjectProperty isLocationOf = model.createObjectProperty(NS + Props.isLocationOf);
//        isLocationOf.addComment("is a hospital's location", "EN");
//        isLocationOf.addInverseOf(hasLocation);

        ObjectProperty hasAddress = createPropIfAbsent(sourceURI + Prop_Name.hasAddress);
        hasAddress.addComment("has a hospital location's address", "EN");
        hasAddress.addRange(XSD.xstring);
//        hasAddress.addDomain(location);
        hasAddress.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isAddressOf = createPropIfAbsent(sourceURI + Prop_Name.isAddressOf);
        isAddressOf.addComment("is a hospital location's address", "EN");
        isAddressOf.addInverseOf(hasAddress);

        ObjectProperty hasZipcode = createPropIfAbsent(sourceURI + Prop_Name.hasZipcode);
        hasZipcode.addComment("has a hospital location's Zipcode", "EN");
        hasZipcode.addRange(XSD.xstring);
//        hasZipcode.addDomain(location);
        hasZipcode.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isZipcodeOf = createPropIfAbsent(sourceURI + Prop_Name.isZipcodeOf);
        isZipcodeOf.addComment("is a hospital location's Zipcode", "EN");
        isZipcodeOf.addInverseOf(hasZipcode);

        ObjectProperty hasCity = createPropIfAbsent(sourceURI + Prop_Name.hasCity);
        hasCity.addComment("has a hospital location's city", "EN");
        hasCity.addRange(XSD.xstring);
//        hasCity.addDomain(location);
        hasCity.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isCityOf = createPropIfAbsent(sourceURI + Prop_Name.isCityOf);
        isCityOf.addComment("is a hospital location's city", "EN");
        isCityOf.addInverseOf(hasCity);

        ObjectProperty hasCountry = createPropIfAbsent(sourceURI + Prop_Name.hasCountry);
        hasCountry.addComment("has a hospital location's country", "EN");
        hasCountry.addRange(createClassIfAbsent(sourceURI + Class_Name.Country));
//        hasCountry.addDomain(location);
        hasCountry.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isCountryOf = createPropIfAbsent(sourceURI + Prop_Name.isCountryOf);
        isCountryOf.addComment("is a hospital location's country", "EN");
        isCountryOf.addInverseOf(hasCountry);

        ObjectProperty hasState = createPropIfAbsent(sourceURI + Prop_Name.hasState);
        hasState.addComment("has a hospital location's state", "EN");
        hasState.addRange(createClassIfAbsent(sourceURI + Class_Name.State));
//        hasState.addDomain(location);
        hasState.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isStateOf = createPropIfAbsent(sourceURI + Prop_Name.isStateOf);
        isStateOf.addComment("is a hospital location's state", "EN");
        isStateOf.addInverseOf(hasState);

        ObjectProperty hasType = createPropIfAbsent(sourceURI + Prop_Name.hasType);
        hasType.addComment("has a hospital's type", "EN");
        hasType.addRange(createClassIfAbsent(sourceURI + Class_Name.Type));
        hasType.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isTypeOf = createPropIfAbsent(sourceURI + Prop_Name.isTypeOf);
        isTypeOf.addComment("is a hospital's type", "EN");
        isTypeOf.addInverseOf(hasType);

        ObjectProperty hasOwnership = createPropIfAbsent(sourceURI + Prop_Name.hasOwnership);
        hasOwnership.addComment("has a hospital's ownership", "EN");
        hasOwnership.addRange(createClassIfAbsent(sourceURI + Class_Name.Ownership));
        hasOwnership.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isOwnershipOf = createPropIfAbsent(sourceURI + Prop_Name.isOwnershipOf);
        isOwnershipOf.addComment("is a hospital's ownership", "EN");
        isOwnershipOf.addInverseOf(hasOwnership);

        ObjectProperty hasStatistics = createPropIfAbsent(sourceURI + Prop_Name.hasStatistics);
        hasStatistics.addComment("has MedicareMetadata's statistics", "EN");
        hasStatistics.addRange(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        hasStatistics.addDomain(createClassIfAbsent(sourceURI + Class_Name.MedicareMetadata));
        ObjectProperty isStatisticsOf = createPropIfAbsent(sourceURI + Prop_Name.isStatisticsOf);
        isStatisticsOf.addComment("is MedicareMetadata's statistics", "EN");
        isStatisticsOf.addInverseOf(hasStatistics);
    }

    //== == Utilities methods == ==//
    private static OntClass createClassIfAbsent(String URI) {
        return classCache.merge(URI, model.createClass(URI), (prev, curr) -> prev);
    }

    private static ObjectProperty createPropIfAbsent(String URI) {
        return propCache.merge(URI, model.createObjectProperty(URI), (prev, curr) -> prev);
    }

    private static Individual createIndividualIfAbsent(OntClass instanceClass, String URI) {
        return individualCache.merge(URI, instanceClass.createIndividual(URI), (prev, curr) -> prev);
    }
}
