package project2.Hospital;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;
import project2.Hospital.utils.Stopwatch;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class is responsible to read the CSV file and build OWL instances
 */
public class OntAPI {
    static final String hospitalGeneralFileName = "Hospital_General_Information.csv";
    static final String hospitalSpendingFileName = "Medicare_Hospital_Spending_by_claim_groupbyID.csv";
    static final String hospitalCareFileName = "Timely_Effective_groupedByID.csv";
    static final Path basePath = FileSystems.getDefault().getPath("").toAbsolutePath();
    static final Path resourcePath = basePath.resolve(Paths.get("src", "main", "resources"));

    public static org.apache.jena.ontology.OntModel model;
    static Map<String, Hospital> hospitalsMap;
    static Map<String, State> statesMap;
    static String nationAverage = "-1";

    public OntAPI() {
        model = new OntModel().getModel();
        statesMap = new HashMap<>();
        hospitalsMap = new HashMap<>();
    }

    public static void addHospitalToModel(List<Hospital> hospitals) {
        for (Hospital h : hospitals) {
            OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
            Individual instance = hospital.createIndividual(OntModel.NS + h.getID());
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

            model.add(instance, hasFacilityName, h.getHospitalName());
            model.add(instance, hasID, h.getID());
            model.add(instance, hasEmergencyService, String.valueOf(h.getHasEmergency()));
            model.add(instance, hasPhoneNumber, h.getPhoneNumber());
            model.add(instance, hasScore, h.getRating());
            model.add(instance, hasScore, h.getScore());
            model.add(instance, hasRating, h.getRating());
            model.add(instance, hasMedicareSpending, h.getMedicareAmount());
            model.add(instance, hasAddress, h.getAddress());
            model.add(instance, hasCountry, h.getCountry());
            model.add(instance, hasCity, h.getCity());
            model.add(instance, hasState, h.getState());
            model.add(instance, hasZipCode, h.getZipcode());
            model.add(instance, hasOwnership, h.getOwnershipName());
            model.add(instance, hasType, h.getType());
        }
    }

    public static void addStateSpending(List<State> states) {
        OntClass state = model.getOntClass(OntModel.NS + OntModel.Classes.State);
        for (State s : states) {
            Individual stateInstance = model.getIndividual(OntModel.NS + s.getAbbr());
            if (stateInstance == null) {
                stateInstance = state.createIndividual(OntModel.NS + s.getAbbr());
                stateInstance.addComment(s.getFullStateName(), "EN");
            }
            Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
            model.add(stateInstance, hasAverageSpending, s.getAmount());
        }
    }

    public static void addNationSpending() {
        OntClass country = model.getOntClass(OntModel.NS + OntModel.Classes.Country);
        Individual instance = country.createIndividual(OntModel.NS + "USA");
        instance.addLabel("United State of America", "EN");
        Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
        model.add(instance, hasAverageSpending, nationAverage);
    }

    public void display() { model.write(System.out); }

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
        System.out.println("Can't find hospital ID: " + ID);
        return Hospital.create(ID);
    }

    public State getState(String abbr) {
        return statesMap.containsKey(abbr) ? statesMap.get(abbr) : State.create(abbr);
    }

    public void parseGeneralCSV() throws IOException {
        Path hospitalGeneralFilePath = resourcePath.resolve(hospitalGeneralFileName);
        Hospital hospital;
        String row;

        BufferedReader csvReader = new BufferedReader(new FileReader(hospitalGeneralFilePath.toString()));
        csvReader.readLine(); // ignore first line
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
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
    public void parseSpendingCSV() throws IOException {
        Path hospitalSpendingFilePath = resourcePath.resolve(hospitalSpendingFileName);
        BufferedReader csvReader = new BufferedReader(new FileReader(hospitalSpendingFilePath.toString()));
        String row;

        csvReader.readLine(); // ignore first line
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
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
    public void parseCareCSV() throws IOException {
        Path hospitalCareFilePath = resourcePath.resolve(hospitalCareFileName);
        Hospital hospital;
        BufferedReader csvReader = new BufferedReader(new FileReader(hospitalCareFilePath.toString()));
        String row;

        csvReader.readLine(); // ignore first line
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String facilityID = data[0];
            String totalScore = data[8];

            hospital = getHospital(facilityID);
            hospital.setScore(totalScore);
            hospitalsMap.put(facilityID, hospital);
        }
    }

    public static void main(String[] args) throws IOException {
        OntAPI instanceModel = new OntAPI();

        //Read csv file and get all values
        instanceModel.parseGeneralCSV();
        instanceModel.parseSpendingCSV();
        instanceModel.parseCareCSV();

//        for (var e : hospitalsMap.values()) {
//            System.out.println(e);
//        }
        // Build models
//        System.out.println("HospitalsMap - Complete");
//        Stopwatch timer = new Stopwatch();
//        addHospitalToModel(new ArrayList<>(hospitalsMap.values()));
//        System.out.println("Model Hospital - Complete - Time: " + timer.elapsedTime());
//
//        timer = new Stopwatch();
//        addStateSpending(new ArrayList<>(statesMap.values()));
//        System.out.println("Model Hospital - Complete - Time: " + timer.elapsedTime());
//
//        addNationSpending();
//        timer = new Stopwatch();
//        instanceModel.writeToFile();
//        System.out.println("Write Owl - Complete - Time: " + timer.elapsedTime());
    }
}
