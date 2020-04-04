package project2.Ontology;

import org.apache.http.client.utils.URIBuilder;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import project2.Helper.CSVData;
import project2.Helper.Hospital;
import project2.Helper.State;
import project2.Util.Stopwatch;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class is responsible to read the CSV file and build OWL instances
 */
public class OntInstanceModel {
    //== Static fields ==//
    public static final String source = "https://data.medicare.gov/d/nrth-mfg3";
    private static final String owlFileName = "hospital.owl";
    final URIBuilder myURIBuilder;

    //== Instance fields ==//
    private org.apache.jena.ontology.OntModel model; // Root model
    private Map<String, Hospital> hospitals; // String = facilityID
    private Map<String, State> states; // String = full State's name
    private String nationalAverage; // National Average Medicare Spending
    private Map<String, Individual> cache; // Cache for faster Ontology's instance look-up time

    //== Constructor ==//
    public OntInstanceModel() throws URISyntaxException {
        states = CSVData.getStates();
        hospitals = CSVData.getHospitals();
        nationalAverage = CSVData.getNationalAverage();

        model = new OntModel().getModel();
        cache = new HashMap<>();
        myURIBuilder = new URIBuilder(source);
    }

    //== Public methods ==//
    //== == Build ontology instances == ==//
    public OntInstanceModel build (Predicate<Hospital> hospitalPred) throws UnsupportedEncodingException, URISyntaxException {
        Stopwatch timer;
        List<Hospital> filteredHospitals = hospitals.values().stream().filter(hospitalPred).collect(Collectors.toList());

        System.out.println("Building Ontology, please wait...It might take some time...");
        System.out.println("All hospitals size: " + hospitals.size());
        System.out.println("Filtered hospitals size: " + filteredHospitals.size());

        timer = new Stopwatch();
        addHospitalToModel(filteredHospitals);
        System.out.println("Building Hospital Ontology - Complete - Time: " + timer.elapsedTime());

        timer = new Stopwatch();
        addStateSpending(new ArrayList<>(states.values()));
        System.out.println("Building State Ontology - Complete - Time: " + timer.elapsedTime());

        addNationSpending();

        return this;
    }


    private void addHospitalToModel(List<Hospital> hospitals) throws UnsupportedEncodingException, URISyntaxException {
        final String yearStr = "2018";
        int total = hospitals.size();
//        Stopwatch timer;

        OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Class_Name.Hospital);
        OntClass state = model.getOntClass(OntModel.NS + OntModel.Class_Name.State);
        OntClass country = model.getOntClass(OntModel.NS + OntModel.Class_Name.Country);
        OntClass type = model.getOntClass(OntModel.NS + OntModel.Class_Name.Type);
        OntClass ownership = model.getOntClass(OntModel.NS + OntModel.Class_Name.Ownership);
        OntClass statistics = model.getOntClass(OntModel.NS + OntModel.Class_Name.Statistics);
        OntClass averagemedicarespending = model.getOntClass(OntModel.NS + OntModel.Class_Name.AverageMedicareSpending);
        OntClass score = model.getOntClass(OntModel.NS + OntModel.Class_Name.Score);
        OntClass rating = model.getOntClass(OntModel.NS + OntModel.Class_Name.Rating);
        OntClass year = model.getOntClass(OntModel.NS + OntModel.Class_Name.Year);

        for (Hospital h : hospitals) {
//            timer = new Stopwatch();
            Individual hospitalInstance = hospital.createIndividual(OntModel.NS + h.getIDAsURI());
            Individual stateInstance = getIndividual(state, OntModel.NS + h.getStateAsURI());
            Individual usa = getIndividual(country, OntModel.NS + h.getCountryAsURI());
            Individual typeInstance = getIndividual(type, OntModel.NS + h.getTypeAsURI());
            Individual ownershipInstance = getIndividual(ownership, OntModel.NS +h.getOwnershipAsURI());
//            Individual averagemedicarespendingInstance = getIndividual(averagemedicarespending, OntModel.NS + h.getMedicareAmountAsURI());
//            Individual scoreInstance = getIndividual(score, OntModel.NS + h.getScoreAsURI());
//            Individual ratingInstance = getIndividual(rating, OntModel.NS + h.getRatingAsURI());
//            Individual yearInstance = getIndividual(year, OntModel.NS + "2018");
            Individual statisticsInstance = getIndividual(statistics, OntModel.NS + h.getIDAsURI() + yearStr);
//            System.out.println("Individual time: " + timer.elapsedTime());

//            timer = new Stopwatch();
            Property hasID = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasFacilityID);
            Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasFacilityName);
            Property hasEmergencyService = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasEmergencyService);
            Property hasPhoneNumber = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasPhoneNumber);
            Property hasScore = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasScore);
            Property hasRating = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasRating);
            Property hasMedicareSpending = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasAverageMedicareSpending);
            Property hasCity = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasCity);
            Property hasState = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasState);
            Property hasCountry = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasCountry);
            Property hasZipCode = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasZipcode);
            Property hasAddress = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasAddress);
            Property hasOwnership = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasOwnership);
            Property hasType = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasType);
            Property hasStatistics = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasStatistics);
            Property hasYear = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasYear);
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
            model.add(hospitalInstance, hasOwnership, ownershipInstance);
            model.add(hospitalInstance, hasType, typeInstance);

            model.add(statisticsInstance, hasScore, model.createTypedLiteral(h.getScoreParsed().orElse(-1)));
            model.add(statisticsInstance, hasRating, model.createTypedLiteral(h.getRatingParsed().orElse(-1)));
            model.add(statisticsInstance, hasMedicareSpending, model.createTypedLiteral(h.getMedicareAmountParsed().orElse(-1.0)));
            model.add(statisticsInstance, hasYear, model.createTypedLiteral("2018"));
            model.add(hospitalInstance, hasStatistics, statisticsInstance);
//            System.out.println("Model Adding time: " + timer.elapsedTime());
            System.out.println("Remained: " + (--total) + " - Processed " + h.getID());
        }
    }

    private void addStateSpending(List<State> states) {
        OntClass state = model.getOntClass(OntModel.NS + OntModel.Class_Name.State);
        for (State s : states) {
            Individual stateInstance = model.getIndividual(OntModel.NS + s.getAbbr());
            if (stateInstance == null) {
                stateInstance = state.createIndividual(OntModel.NS + s.getAbbr());
                stateInstance.addComment(s.getFullStateName(), "EN");
            }
            Property hasAverageMedicareSpending = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasAverageMedicareSpending);
            model.add(stateInstance, hasAverageMedicareSpending, s.getAmount());
        }
    }

    private void addNationSpending() {
        OntClass country = model.getOntClass(OntModel.NS + OntModel.Class_Name.Country);
        Individual instance = country.createIndividual(OntModel.NS + "USA");
        instance.addLabel("United State of America", "EN");
        Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Prop_Name.hasAverageMedicareSpending);
        model.add(instance, hasAverageSpending, nationalAverage);
    }

    //== == Utilities == ==//
    public void writeToFile(String fileName) throws IOException {
        Path filePath = FileSystems.getDefault().getPath("").toAbsolutePath()
                .resolve("deliverables")
                .resolve(owlFileName);

        BufferedWriter out = new BufferedWriter(new FileWriter(filePath.toString()));

        System.out.println("Writing to file " + owlFileName + " ...");
        Stopwatch timer = new Stopwatch();
        model.write(out);
        System.out.println("Writing to file - Complete - Time: " + timer.elapsedTime());
    }

    public void writeToFile() throws IOException {
        writeToFile(owlFileName);
    }

//    // Filter out hospitals that doesn't have spending or score
//    public static Map<String, Hospital> filterHospitals(Map<String, Hospital> hospitals) {
//        return hospitals
//                .entrySet()
//                .stream()
//                .filter(entry -> entry.getValue().isValid())
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }

    // == Private methods == //
    private Individual getIndividual(OntClass instanceClass, String URI) {
        if (!cache.containsKey(URI)) {
            cache.put(URI, instanceClass.createIndividual(URI));
        }
        return cache.get(URI);
    }

//    private static Map<String, Hospital> getHospitalWithBadData(Map<String, Hospital> hospitals) {
//        return hospitals.entrySet().stream()
//                .filter(entry -> !entry.getValue().isValid())
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    //== Small samples for testing purposes ==//
//    private static Map<String, Hospital> getSmallSample(Map<String, Hospital> hospitals) {
//        return hospitals.entrySet().stream().limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
}
