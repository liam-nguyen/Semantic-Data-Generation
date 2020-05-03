package project2.Ontology;

import lombok.Getter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;
import project2.Helper.CSVData;
import project2.Helper.Hospital;
import project2.Helper.State;
import Util.Stopwatch;

import java.io.*;
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
        MedicareMetadata, Hospital, State, Statistics, Location, Nation, Address,
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
        isOwnershipOf, hasYear, isYearOf, hasStatistics, isStatisticsOf, hasCounty, isCountyOf,
        hasStateAverageMedicareSpending, isStateAverageMedicareSpendingOf, hasStateName, isStateNameOf,
        hasNationalAverageSpending, isNationalAverageSpendingOf, hasNationName, isNationNameOf;

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
    private static final Path ROOT_PATH = FileSystems.getDefault().getPath("").toAbsolutePath();

    @Getter
    private OntModel model;

    @Getter private Map<String, Hospital> hospitals; // String = facilityID
    private Map<String, State> states; // String = full State's name
    private double nationalAverage; // National Average Medicare Spending

    private Map<String, OntClass> classCache;
    private Map<String, ObjectProperty> propCache;
    private Map<String, Individual> individualCache; // Cache for faster Ontology's instance look-up time

    //== Constructor ==//
    public OurModel() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF); //rule-based reasoner with OWL rules
        model.setNsPrefix("ds", sourceURI); // set namespace prefix
        model.setNsPrefix("du", duURI);

        classCache = new HashMap<>();
        propCache = new HashMap<>();
        individualCache = new HashMap<>();

        states = CSVData.getStates();
        hospitals = CSVData.getHospitals();
        nationalAverage = CSVData.getNationalAverage();
    }

    //== Public methods ==//
    public void build(List<Predicate<Hospital>> hospitalInstancePred) {
        addClasses();
        addProps();
        addInstances(hospitalInstancePred);
    }

    public void build() {
        this.build(new ArrayList<>());
    }

    public void writeModelToFile(String fileName) throws IOException {
        String filePath = ROOT_PATH.resolve("deliverables").resolve(fileName).toString();

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath))) {
            System.out.println("Writing to file " + fileName + " ...");
            Stopwatch timer = new Stopwatch();
            model.write(out);
            System.out.println("Writing to file - Complete - Time: " + timer.elapsedTime());
        };
    }

    public void writeModelToFile() throws IOException {
        writeModelToFile(owlFileName);
    }

    //== Private methods ==//
    //== == Build ontology instances == ==//
    private void addInstances(List<Predicate<Hospital>> preds) {
        Stopwatch timer;
        List<Hospital> filteredHospitals = hospitals
                .values()
                .stream()
                .filter(preds.stream().reduce(x->true, Predicate::and))
                .collect(Collectors.toList());

        System.out.println("Building Ontology, please wait...It might take some time...");
        System.out.println("Building Hospitals Size: " + filteredHospitals.size());

        timer = new Stopwatch();
        addHospitalToModel(filteredHospitals);
        System.out.println("Hospital Ontology - Complete time: " + timer.elapsedTime());

        timer = new Stopwatch();
        System.out.println("Adding states. Size: " + hospitals.size());
        addStateToModel(new ArrayList<>(states.values()));
        System.out.println("State Ontology - Complete - Time: " + timer.elapsedTime());

        System.out.println("Adding nation. Size: " + 1);
        addNationToModel();
    }

    private void addHospitalToModel(List<Hospital> hospitals) {
        final String STAT_YEAR = "2018";
        int total = hospitals.size();
//        Stopwatch timer;

        OntClass hospital = createClassIfAbsent(Class_Name.Hospital.getURI());
        OntClass state = createClassIfAbsent(Class_Name.State.getURI());
        OntClass country = createClassIfAbsent(Class_Name.Nation.getURI());

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
        ObjectProperty hasCounty = createPropIfAbsent(OurModel.Prop_Name.hasCounty.getURI());
        ObjectProperty hasZipCode = createPropIfAbsent(OurModel.Prop_Name.hasZipcode.getURI());
        ObjectProperty hasAddress = createPropIfAbsent(OurModel.Prop_Name.hasAddress.getURI());
        ObjectProperty hasOwnership = createPropIfAbsent(OurModel.Prop_Name.hasOwnership.getURI());
        ObjectProperty hasType = createPropIfAbsent(OurModel.Prop_Name.hasType.getURI());
        ObjectProperty hasYear = createPropIfAbsent(OurModel.Prop_Name.hasYear.getURI());


        for (Hospital h : hospitals) {
            Individual hospitalInstance = hospital.createIndividual(OurModel.sourceURI + h.getIDAsURI());
            Individual stateInstance = createIndividualIfAbsent(state, OurModel.sourceURI + h.getStateAsURI());
            Individual usa = createIndividualIfAbsent(country, OurModel.sourceURI + h.getCountryAsURI());

            model.add(hospitalInstance, hasFacilityName, h.getHospitalName());
            model.add(hospitalInstance, hasID, h.getID());
            model.add(hospitalInstance, hasEmergencyService, String.valueOf(h.getHasEmergency()));
            model.add(hospitalInstance, hasPhoneNumber, h.getPhoneNumber());
            model.add(hospitalInstance, hasAddress, h.getAddress());
            model.add(hospitalInstance, hasCountry, usa);
            model.add(hospitalInstance, hasCounty, h.getCountyAsURI());
            model.add(hospitalInstance, hasCity, h.getCity());
            model.add(hospitalInstance, hasState, stateInstance);
            model.add(hospitalInstance, hasZipCode, h.getZipcode());
            model.add(hospitalInstance, hasScore, model.createTypedLiteral(h.getScore()));
            model.add(hospitalInstance, hasRating, model.createTypedLiteral(h.getRating()));
            model.add(hospitalInstance, hasHospitalAverageMedicareSpending, model.createTypedLiteral(h.getMedicareAmount()));
            model.add(hospitalInstance, hasYear, model.createTypedLiteral(STAT_YEAR));
            model.add(hospitalInstance, hasOwnership, model.createTypedLiteral(h.getOwnershipName()));
            model.add(hospitalInstance, hasType, model.createTypedLiteral(h.getType()));
            System.out.println("Remained: " + (--total) + " - Added " + h.getID());
        }
    }

    private void addStateToModel(List<State> states) {
        int total = states.size();
        OntClass state = createClassIfAbsent(OurModel.Class_Name.State.getURI());
        ObjectProperty hasStateAverageMedicareSpending = createPropIfAbsent(Prop_Name.hasStateAverageMedicareSpending.getURI());
        ObjectProperty hasStateName = createPropIfAbsent(Prop_Name.hasStateName.getURI());

        for (State s : states) {
            Individual stateInstance = createIndividualIfAbsent(state, OurModel.sourceURI + s.getAbbr());

            model.add(stateInstance, hasStateAverageMedicareSpending, model.createTypedLiteral(s.getAverageMedicareAmount()));
            model.add(stateInstance, hasStateName, model.createTypedLiteral(s.getFullStateName()));
            System.out.println("Remained: " + (--total) + " - Added " + s.getAbbr());
        }
    }

    private void addNationToModel() {
        OntClass country = createClassIfAbsent(OurModel.Class_Name.Nation.getURI());
        ObjectProperty hasNationalAverageSpending = createPropIfAbsent(OurModel.Prop_Name.hasNationalAverageSpending.getURI());
        ObjectProperty hasNationName = createPropIfAbsent(Prop_Name.hasNationName.getURI());

        Individual nationInstance = country.createIndividual(OurModel.sourceURI + "USA");

        nationInstance.addLabel("United State of America", "EN");
        model.add(nationInstance, hasNationalAverageSpending, model.createTypedLiteral(nationalAverage));
        model.add(nationInstance, hasNationName, model.createTypedLiteral("United State of America"));
        System.out.println("Added national averaged.");
    }

    private void addClasses() {
        OntClass state = createClassIfAbsent(sourceURI + Class_Name.State);
        OntClass country = createClassIfAbsent(sourceURI + Class_Name.Nation);

        state.addComment("One of 50 states in US", "EN");
        country.addComment("A large body of people united by common descent, history, culture, or language, inhabiting a particular country or territory.", "EN");
    }

    private void addProps() {
        /* Create properties */
        //** ** Hospital ** **//
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

        ObjectProperty hasHospitalAverageMedicareSpending = createPropIfAbsent(sourceURI + Prop_Name.hasHospitalAverageMedicareSpending);
        hasHospitalAverageMedicareSpending.addComment("has a hospital's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasHospitalAverageMedicareSpending.addRange(XSD.xdouble);
        hasHospitalAverageMedicareSpending.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isHospitalAverageSpendingOf = createPropIfAbsent(sourceURI + Prop_Name.isAverageMedicareSpendingOf);
        isHospitalAverageSpendingOf.addComment("is a hospital's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isHospitalAverageSpendingOf.addInverseOf(hasHospitalAverageMedicareSpending);

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
        hasYear.addRange(XSD.xstring);
        hasYear.addDomain(createClassIfAbsent(sourceURI + Class_Name.Statistics));
        ObjectProperty isYearOf = createPropIfAbsent(sourceURI + Prop_Name.isYearOf);
        isYearOf.addComment("is a statistic's year", "EN");
        isYearOf.addInverseOf(hasYear);

        ObjectProperty hasAddress = createPropIfAbsent(sourceURI + Prop_Name.hasAddress);
        hasAddress.addComment("has a hospital location's address", "EN");
        hasAddress.addRange(XSD.xstring);
        hasAddress.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isAddressOf = createPropIfAbsent(sourceURI + Prop_Name.isAddressOf);
        isAddressOf.addComment("is a hospital location's address", "EN");
        isAddressOf.addInverseOf(hasAddress);

        ObjectProperty hasZipcode = createPropIfAbsent(sourceURI + Prop_Name.hasZipcode);
        hasZipcode.addComment("has a hospital location's Zipcode", "EN");
        hasZipcode.addRange(XSD.xstring);
        hasZipcode.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isZipcodeOf = createPropIfAbsent(sourceURI + Prop_Name.isZipcodeOf);
        isZipcodeOf.addComment("is a hospital location's Zipcode", "EN");
        isZipcodeOf.addInverseOf(hasZipcode);

        ObjectProperty hasCity = createPropIfAbsent(sourceURI + Prop_Name.hasCity);
        hasCity.addComment("has a hospital location's city", "EN");
        hasCity.addRange(XSD.xstring);
        hasCity.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isCityOf = createPropIfAbsent(sourceURI + Prop_Name.isCityOf);
        isCityOf.addComment("is a hospital location's city", "EN");
        isCityOf.addInverseOf(hasCity);

        ObjectProperty hasCountry = createPropIfAbsent(sourceURI + Prop_Name.hasCountry);
        hasCountry.addComment("has a hospital location's country", "EN");
        hasCountry.addRange(createClassIfAbsent(sourceURI + Class_Name.Nation));
        hasCountry.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isCountryOf = createPropIfAbsent(sourceURI + Prop_Name.isCountryOf);
        isCountryOf.addComment("is a hospital location's country", "EN");
        isCountryOf.addInverseOf(hasCountry);

        ObjectProperty hasState = createPropIfAbsent(sourceURI + Prop_Name.hasState);
        hasState.addComment("has a hospital location's state", "EN");
        hasState.addRange(createClassIfAbsent(sourceURI + Class_Name.State));
        hasState.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isStateOf = createPropIfAbsent(sourceURI + Prop_Name.isStateOf);
        isStateOf.addComment("is a hospital location's state", "EN");
        isStateOf.addInverseOf(hasState);

        ObjectProperty hasType = createPropIfAbsent(sourceURI + Prop_Name.hasType);
        hasType.addComment("has a hospital's type", "EN");
        hasType.addRange(XSD.xstring);
        hasType.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isTypeOf = createPropIfAbsent(sourceURI + Prop_Name.isTypeOf);
        isTypeOf.addComment("is a hospital's type", "EN");
        isTypeOf.addInverseOf(hasType);

        ObjectProperty hasOwnership = createPropIfAbsent(sourceURI + Prop_Name.hasOwnership);
        hasOwnership.addComment("has a hospital's ownership", "EN");
        hasOwnership.addRange(XSD.xstring);
        hasOwnership.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isOwnershipOf = createPropIfAbsent(sourceURI + Prop_Name.isOwnershipOf);
        isOwnershipOf.addComment("is a hospital's ownership", "EN");
        isOwnershipOf.addInverseOf(hasOwnership);

        ObjectProperty hasCounty = createPropIfAbsent(sourceURI + Prop_Name.hasCounty);
        hasCounty.addComment("has a hospital's county", "EN");
        hasCounty.addRange(XSD.xstring);
        hasCounty.addDomain(createClassIfAbsent(sourceURI + Class_Name.Hospital));
        ObjectProperty isCountyOf = createPropIfAbsent(sourceURI + Prop_Name.isCountyOf);
        isCountyOf.addComment("is a hospital's county of", "EN");
        isCountyOf.addInverseOf(hasCounty);

        //** ** State ** **//
        ObjectProperty hasStateAverageMedicareSpending = createPropIfAbsent(Prop_Name.hasStateAverageMedicareSpending.getURI());
        hasStateAverageMedicareSpending.addComment("has a state's average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasStateAverageMedicareSpending.addRange(XSD.xdouble);
        hasStateAverageMedicareSpending.addDomain(createClassIfAbsent(Class_Name.State.getURI()));
        ObjectProperty isStateAverageMedicareSpendingOf = createPropIfAbsent(Prop_Name.isStateAverageMedicareSpendingOf.getURI());
        isStateAverageMedicareSpendingOf.addComment("is a state's average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isStateAverageMedicareSpendingOf.addInverseOf(hasStateAverageMedicareSpending);

        ObjectProperty hasStateName = createPropIfAbsent(Prop_Name.hasStateName.getURI());
        hasStateName.addComment("Full name of a state", "EN");
        hasStateName.addRange(XSD.xstring);
        hasStateName.addDomain(createClassIfAbsent(Class_Name.State.getURI()));
        ObjectProperty isStateNameOf = createPropIfAbsent(Prop_Name.isStateNameOf.getURI());
        isStateNameOf.addComment("is state name of ", "EN");
        isStateNameOf.addInverseOf(hasStateName);

        //** ** Nation ** **//
        ObjectProperty hasNationalAverageSpending = createPropIfAbsent(sourceURI + Prop_Name.hasNationalAverageSpending);
        hasNationalAverageSpending.addComment("has a national average spending per Beneficiary (MSPB) episodes in USD", "EN");
        hasNationalAverageSpending.addRange(XSD.xdouble);
        hasNationalAverageSpending.addDomain(createClassIfAbsent(Class_Name.Nation.getURI()));
        ObjectProperty isNationalAverageSpendingOf = createPropIfAbsent(Prop_Name.isNationalAverageSpendingOf.getURI());
        isNationalAverageSpendingOf.addComment("is a national average spending per Beneficiary (MSPB) episodes in USD of", "EN");
        isNationalAverageSpendingOf.addInverseOf(hasNationalAverageSpending);

        ObjectProperty hasNationName = createPropIfAbsent(Prop_Name.hasNationName.getURI());
        hasNationName.addComment("Name of a nation", "EN");
        hasNationName.addRange(XSD.xstring);
        hasNationName.addDomain(createClassIfAbsent(Class_Name.Nation.getURI()));
        ObjectProperty isNationNameOf = createPropIfAbsent(Prop_Name.isNationNameOf.getURI());
        isNationNameOf.addComment("is a nation name of ", "EN");
        isNationNameOf.addInverseOf(hasNationName);
    }

    //== == Utilities methods == ==//
    private OntClass createClassIfAbsent(String URI) {
        return classCache.merge(URI, model.createClass(URI), (prev, curr) -> prev);
    }

    private ObjectProperty createPropIfAbsent(String URI) {
        return propCache.merge(URI, model.createObjectProperty(URI), (prev, curr) -> prev);
    }

    private Individual createIndividualIfAbsent(OntClass instanceClass, String URI) {
        return individualCache.merge(URI, instanceClass.createIndividual(URI), (prev, curr) -> prev);
    }

    private void clearCache() {
        hospitals = null;
        states= null;
        classCache= null;
        propCache= null;
        individualCache= null;
    }
}
