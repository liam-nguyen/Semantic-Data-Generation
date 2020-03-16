package project2.Hospital;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import project2.Hospital.utils.Stopwatch;
import java.util.*;

/**
 * This class is responsible to read the CSV file and build OWL instances
 */
public class OntAPI {
    static final String hospitalGeneralFileName = "Hospital_General_Information.csv";
    static final String hospitalSpendingFileName = "Medicare_Hospital_Spending_by_claim_groupbyID.csv";
    static final String hospitalCareFileName = "Timely_Effective_groupedByID.csv";

    public org.apache.jena.ontology.OntModel model;
    Map<String, Hospital> hospitalsMap;
    Map<String, State> statesMap;
    String nationAverage = "-1";

    public Map<String, Individual> cache; // to store all (URI, individuals) to reduce searching time for an individual through model.

    public OntAPI() {
        model = new OntModel().getModel();
        statesMap = new HashMap<>();
        hospitalsMap = new HashMap<>();
        cache = new HashMap<>();
    }

    public void addHospitalToModel(List<Hospital> hospitals) {
        final String yearStr = "2018";
        int total = hospitals.size();
        Stopwatch timer;

        OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
        OntClass state = model.getOntClass(OntModel.NS + OntModel.Classes.State);
        OntClass country = model.getOntClass(OntModel.NS + OntModel.Classes.Country);
        OntClass type = model.getOntClass(OntModel.NS + OntModel.Classes.Type);
        OntClass ownership = model.getOntClass(OntModel.NS + OntModel.Classes.Ownership);
        OntClass statistics = model.getOntClass(OntModel.NS + OntModel.Classes.Statistics);
        OntClass averagemedicarespending = model.getOntClass(OntModel.NS + OntModel.Classes.AverageMedicareSpending);

        for (Hospital h : hospitals) {
//            timer = new Stopwatch();
            Individual hospitalInstance = hospital.createIndividual(OntModel.NS + h.getID());
            Individual stateInstance = getIndividual(state, OntModel.NS + h.getState());
            Individual usa = getIndividual(country, OntModel.NS + "USA");
            Individual typeInstance = getIndividual(type, OntModel.NS + h.getType());
            Individual ownershipInstance = getIndividual(ownership, OntModel.NS + h.getOwnershipName());
            Individual statisticsInstance = getIndividual(statistics, OntModel.NS + h.getID() + yearStr);
//            System.out.println("Individual time: " + timer.elapsedTime());

//            timer = new Stopwatch();
            Property hasID = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityID);
            Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityName);
            Property hasEmergencyService = model.getProperty(OntModel.NS + OntModel.Props.hasEmergencyService);
            Property hasPhoneNumber = model.getProperty(OntModel.NS + OntModel.Props.hasPhoneNumber);
            Property hasScore = model.getProperty(OntModel.NS + OntModel.Props.hasScore);
            Property hasRating = model.getProperty(OntModel.NS + OntModel.Props.hasRating);
            Property hasMedicareSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
            Property hasCity = model.getProperty(OntModel.NS + OntModel.Props.hasCity);
            Property hasState = model.getProperty(OntModel.NS + OntModel.Props.hasState);
            Property hasCountry = model.getProperty(OntModel.NS + OntModel.Props.hasCountry);
            Property hasZipCode = model.getProperty(OntModel.NS + OntModel.Props.hasZipcode);
            Property hasAddress = model.getProperty(OntModel.NS + OntModel.Props.hasAddress);
            Property hasOwnership = model.getProperty(OntModel.NS + OntModel.Props.hasOwnership);
            Property hasType = model.getProperty(OntModel.NS + OntModel.Props.hasType);
            Property hasStatistics = model.getProperty(OntModel.NS + OntModel.Props.hasStatistics);
            Property hasYear = model.getProperty(OntModel.NS + OntModel.Props.hasYear);
//            System.out.println("Property time: " + timer.elapsedTime());

//            timer = new Stopwatch();
            model.add(hospitalInstance, hasFacilityName, h.getHospitalName());
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

            model.add(statisticsInstance, hasScore, h.getScore());
            model.add(statisticsInstance, hasRating, h.getRating());
            model.add(statisticsInstance, hasMedicareSpending, h.getMedicareAmount());
            model.add(statisticsInstance, hasYear, yearStr);
            model.add(hospitalInstance, hasStatistics, statisticsInstance);
//            System.out.println("Model Adding time: " + timer.elapsedTime());
            System.out.println("Remained: " + (--total) + " - Processed " + h.getID());
        }
    }

    public void addStateSpending(List<State> states) {
        OntClass state = model.getOntClass(OntModel.NS + OntModel.Classes.State);
        for (State s : states) {
            Individual stateInstance = model.getIndividual(OntModel.NS + s.getAbbr());
            if (stateInstance == null) {
                stateInstance = state.createIndividual(OntModel.NS + s.getAbbr());
                stateInstance.addComment(s.getFullStateName(), "EN");
            }
            Property hasAverageMedicareSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
            model.add(stateInstance, hasAverageMedicareSpending, s.getAmount());
        }
    }

    public void addNationSpending() {
        OntClass country = model.getOntClass(OntModel.NS + OntModel.Classes.Country);
        Individual instance = country.createIndividual(OntModel.NS + "USA");
        instance.addLabel("United State of America", "EN");
        Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
        model.add(instance, hasAverageSpending, nationAverage);
    }

    public void writeToFile(Path path) throws IOException {
        Path filePath = path.resolve("Hospital.owl");
        BufferedWriter out = new BufferedWriter(new FileWriter(filePath.toString()));
        model.write(out);
    }

    public void writeToFile() throws IOException {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        writeToFile(root);
    }

    public Hospital getHospital(String ID) {
        if (hospitalsMap.containsKey(ID)) return hospitalsMap.get(ID);
//        System.out.println("Can't find hospital ID: " + ID);
        return Hospital.create(ID);
    }

    public State getState(String abbr) {
        return statesMap.containsKey(abbr) ? statesMap.get(abbr) : State.create(abbr);
    }

    public void parseGeneralCSV() throws IOException, CsvValidationException {
        Hospital hospital;
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(getClass()
                                .getClassLoader()
                                .getResourceAsStream(hospitalGeneralFileName))))
                .withSkipLines(1)
                .build();


        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            String facilityId = data[0];
            String facilityName = data[1];
            String address = data[2];
            String city = data[3];
            String state = data[4];
            String zipCode = data[5];
            String countryName = data[6];
            String phoneNumber = data[7];
            String hospitalType = data[8];
            String ownership = data[9];
            String emergencyService = data[10];
            String rating = data[12];

            //Creating hospital object
            hospital = getHospital(facilityId)
                            .name(facilityName)
                            .address(address)
                            .city(city)
                            .state(state)
                            .zipCode(zipCode)
                            .country(countryName)
                            .phoneNumber(phoneNumber)
                            .type(hospitalType)
                            .ownership(ownership)
                            .hasEmergency(emergencyService)
                            .rating(rating);

            //Adding hospital to the hashmap of hospitals
            hospitalsMap.put(facilityId, hospital);
        }
    }

    //Reading file hospital spending by claim
    public void parseSpendingCSV() throws IOException, CsvValidationException {
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(getClass()
                                .getClassLoader()
                                .getResourceAsStream(hospitalSpendingFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            final String facilityID = data[0];
            String state = data[2];
            String averageSpendingPerEpisodeHospital = data[5];
            String averageSpendingPerEpisodeState = data[6];
            String averageSpendingPerEpisodeNation = data[7];

            //Check if the facilityId already exists in the map, modify the object by adding averageSpending
            Hospital hospital = getHospital(facilityID);
            hospital.setMedicareAmount(averageSpendingPerEpisodeHospital);
            hospitalsMap.put(facilityID, hospital);

            //Adding average spending to the state
            State stateInstance = getState(state);
            stateInstance.setAverageMedicareAmount(averageSpendingPerEpisodeState);
            statesMap.put(stateInstance.getAbbr(), stateInstance);

            //Adding average spending to the nation
            nationAverage = nationAverage.equals("-1") ? averageSpendingPerEpisodeNation : nationAverage;
        }
    }

    //Reading file timely effective grouped by id
    public void parseCareCSV() throws IOException, CsvValidationException {
        Hospital hospital;

        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(getClass()
                                .getClassLoader()
                                .getResourceAsStream(hospitalCareFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            String facilityID = data[0];
            String totalScore = data[8];

            hospital = getHospital(facilityID);
            hospital.setScore(totalScore);
            hospitalsMap.put(facilityID, hospital);
        }
    }

    // Filter out hospitals that doesn't have spending or score
    public void filterHospitals() {
        hospitalsMap.entrySet().removeIf(entry -> {
            Hospital h = entry.getValue();
            return  h.getHospitalName().equals("") || // Hospital not in general information dataset
                    h.getScore().equals("-1") ||  // Hospital not in timely and effective dataset
                    h.getRating().equals("-1") || // Hospital doesn't have rating
                    h.getRating().equals("Not Available"); // Hospital doesn't have rating
        });
    }

    /** Utilities **/
    private Individual getIndividual(OntClass instanceClass, String URI) {
        if (!cache.containsKey(URI)) {
            cache.put(URI, instanceClass.createIndividual(URI));
        }
        return cache.get(URI);
    }

    public static void main(String[] args) throws IOException, CsvValidationException {
        OntAPI instanceModel = new OntAPI();

        //Read csv file and get all values
        instanceModel.parseGeneralCSV();
        instanceModel.parseSpendingCSV();
        instanceModel.parseCareCSV();
        instanceModel.filterHospitals();

        // Build models
        System.out.println("Complete adding all hospitals. Size: " + instanceModel.hospitalsMap.size());
        System.out.println("Building Ontology, please wait...It might take some time...");
        Stopwatch timer = new Stopwatch();
        instanceModel.addHospitalToModel(new ArrayList<>(instanceModel.hospitalsMap.values()));
        System.out.println("Building Hospital Ontology - Complete - Time: " + timer.elapsedTime());

        timer = new Stopwatch();
        instanceModel.addStateSpending(new ArrayList<>(instanceModel.statesMap.values()));
        System.out.println("Building State Ontology - Complete - Time: " + timer.elapsedTime());

        instanceModel.addNationSpending();
        timer = new Stopwatch();
        System.out.println("Writing to file Hospital.owl...");
        instanceModel.writeToFile();
        System.out.println("Writing to file Hospital.owl - Complete - Time: " + timer.elapsedTime());
    }
}
