package project2.Hospital;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;

import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;
import project2.Hospital.utils.US_States;
import project2.Hospital.utils.TE_Dataset;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class gathers data to build a model
 */
public class OntAPI {
    static final String hospitalGeneralFileName = "Hospital_General_Information.csv";
    static final String hospitalSpendingFileName = "Medicare_Hospital_Spending_by_claim_groupbyID.csv";
    static final String hospitalCareFileName = "Timely_Effective_groupedByID.csv";
    public static org.apache.jena.ontology.OntModel model;
    static List<Hospital> hospitals;
    static Map<String, Hospital> hospitalsMap;
    List<State> states;
    String nationAverage = "-1";
    static Writer writer;



    public OntAPI() {
        model = new OntModel().getModel();
        hospitals = new ArrayList<Hospital>();
        states = new ArrayList<State>();
        hospitalsMap = new HashMap<String,Hospital>();
    }

    public void addState(State s) {
        states.add(s);
    }
    public void addNationMedicareSpending(String amount) {
        nationAverage = amount;
    }

    public static void addHospitalToModel(List<Hospital> hospitals) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("model.owl"), "utf-8"));
            for (Hospital h : hospitals) {
                OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
                Individual instance = hospital.createIndividual(OntModel.NS + h.getHospitalName());
                Property hasID = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityID);
                Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityName);
                Property hasEmergencyService = model.getProperty(OntModel.NS + OntModel.Props.hasEmergencyService);
                Property hasPhoneNumber = model.getProperty(OntModel.NS + OntModel.Props.hasPhoneNumber);
                Property hasScore = model.getProperty(OntModel.NS + OntModel.Props.hasScore);
                Property hasRating = model.getProperty(OntModel.NS + OntModel.Props.hasRating);
                Property hasMedicareSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
                Property hasCity=model.getProperty(OntModel.NS+OntModel.Props.hasCity);
                Property hasState=model.getProperty(OntModel.NS+OntModel.Props.hasState);
                Property hasCountry=model.getProperty(OntModel.NS+OntModel.Props.hasCountry);
                Property hasZipCode=model.getProperty(OntModel.NS+OntModel.Props.hasZipcode);
                Property hasAddress=model.getProperty(OntModel.NS+OntModel.Props.hasAddress);
                Property hasOwnership=model.getProperty(OntModel.NS+OntModel.Props.hasOwnership);
                Property hasType=model.getProperty(OntModel.NS+OntModel.Props.hasType);

                model.add(instance, hasFacilityName, h.getHospitalName());
                model.add(instance, hasID, h.getID());
                model.add(instance, hasEmergencyService, String.valueOf(h.getHasEmergency()));
                model.add(instance, hasPhoneNumber, h.getPhoneNumber());
                model.add(instance, hasScore, h.getRating());
                model.add(instance, hasScore, h.getScore());
                model.add(instance, hasRating, h.getRating());
                model.add(instance, hasMedicareSpending, h.getMedicareAmount());
                model.add(instance, hasAddress,h.getAddress());
                model.add(instance, hasCountry, h.getCountry());
                model.add(instance, hasCity, h.getCity());
                model.add(instance, hasState, h.getState());
                model.add(instance, hasZipCode, h.getZipcode());
                model.add(instance, hasOwnership, h.getOwnershipName());
                model.add(instance, hasType, h.getType());

                //Write model to a file
                model.write(writer);
            }
        } catch(Exception e){
            System.out.println("Exception in writing to a file in addHospitalMethod "+e);
        }
    }

    public static void addStateSpending(String stateName, String amount) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("model.owl"), "utf-8"));
            // Gather classes and properties
            OntClass state = model.getOntClass(OntModel.NS + OntModel.Classes.State);
            Individual stateInstance = model.getIndividual(OntModel.NS + stateName);
            if (stateInstance == null) {
                stateInstance = state.createIndividual(OntModel.NS + stateName);
                stateInstance.addComment(US_States.getFullStateName(stateName), "EN");
            }
            Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
            model.add(stateInstance, hasAverageSpending, amount);
            model.write(writer);
        }catch(Exception e){
            System.out.println("Exception in writing to a file in addStateSpending "+e);
        }
    }

    public static void addNationSpending(String amount) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("model.owl"), "utf-8"));
            // Gather classes and properties
            OntClass nation = model.getOntClass(OntModel.NS + OntModel.Classes.Nation);
            Individual instance = model.getIndividual(OntModel.NS + "USA");
            if (instance == null) {
                instance = nation.createIndividual(OntModel.NS + "USA");
                instance.addLabel("United State of America", "EN");
            }
            Property hasAverageSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);
            model.add(instance, hasAverageSpending, amount);
            model.write(writer);
        }catch(Exception e){
            System.out.println("Exception in writing to a file in addNationSpending "+e.toString());
        }
    }


    public void display() { model.write(System.out); }

    public void writeToFile(Path path) throws IOException {
        Path filePath = path.resolve("Hospital.owl");
        FileWriter out = new FileWriter(filePath.toString());
        model.write(out);
    }
    public void writeToFile() throws IOException {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        writeToFile(root);
    }

//    public void parseCSVs() {
//        Hospital hospital;
//
//        //Read csv file and get all values
//        Path basePath = FileSystems.getDefault().getPath("").toAbsolutePath();
//        Path resourcePath = basePath.resolve(Paths.get("src", "main", "resources"));
//
//        Path hospitalGeneralFilePath = resourcePath.resolve(hospitalGeneralFileName);
//        Path hospitalSpendingFilePath = resourcePath.resolve(hospitalSpendingFileName);
//        Path hospitalCareFilePath = resourcePath.resolve(hospitalCareFileName);
//
//        try {
//            BufferedReader csvReader = new BufferedReader(new FileReader(hospitalGeneralFilePath.toString()));
//            String row = csvReader.readLine();
//            while ((row = csvReader.readLine()) != null) {
//                //  row = csvReader.readLine();
//                String[] data = row.split(",");
//                String facilityId = data[0];
//                String facilityName = data[1];
//                String address=data[2];
//                String city=data[3];
//                String state=data[4];
//                String zipCode=data[5];
//                String countryName=data[6];
//                String phoneNumber=data[7];
//                String hospitalType=data[8];
//                String ownership=data[9];
//                boolean emergencyService=Boolean.getBoolean(data[10]);
//                String rating=data[12];
//                //Creating hospital object
//                hospital=Hospital.create(facilityId).name(facilityName).address(address).city(city).state(state).zipCode(zipCode).country(countryName).
//                        phoneNumber(phoneNumber).type(hospitalType).ownership(ownership).hasEmergency(emergencyService).rating(rating).validate();
//                //Adding hospital to list of hospitals
//                hospitals.add(hospital);
//            }
//
//            //Reading file hospital spending by claim
//            csvReader = new BufferedReader(new FileReader(hospitalSpendingFilePath.toString()));
//            row=csvReader.readLine();
//            while((row=csvReader.readLine())!=null){
//                String[] data = row.split(",");
//                final String facilityID=data[0];
//                String facilityName=data[1];
//                String state=data[2];
//                String period=data[3];
//                String claimType=data[4];
//                String averageSpendingPerEpisodeHopsital=data[5];
//                String averageSpendingPerEpisodeState=data[6];
//                String averageSpendingPerEpisodeNation=data[7];
//
//                //Check if the facility id already exists, if it exists modify the ibject by adding values
//                Iterator<Hospital> iterator = hospitals.iterator();
//                while (iterator.hasNext()) {
//                    Hospital hospital1 = iterator.next();
//                    if (hospital1.getID().equals(facilityID)) {
//                        hospital1.setMedicareAmount(averageSpendingPerEpisodeHopsital);
//                    }
//                }
//            }
//            //Reading file timely effective grouped by id
//            csvReader = new BufferedReader(new FileReader(hospitalCareFilePath.toString()));
//            row = csvReader.readLine();
//            System.out.println(row);
//            while ((row = csvReader.readLine()) != null) {
//                //  row = csvReader.readLine();
//                String[] data = row.split(",");
//                String facilityID = data[0];
//                String facilityName = data[1];
//                String Address = data[2];
//                String city = data[3];
//                String state = data[4];
//                String zipcode = data[5];
//                String countyName = data[6];
//                String PhoneNumber = data[7];
//                String totalScore = data[8];
//                String totalSample = data[9];
//            }
//        } catch(Exception e){
//            System.out.println("Exception in reading file"+ e.toString());
//        }
//    }

    public Hospital getHospital(String ID) {
        return hospitalsMap.containsKey(ID) ? hospitalsMap.get(ID) : Hospital.create(ID);
    }

    public static void main(String[] args) throws IOException {
        OntAPI instanceModel = new OntAPI();
        Hospital hospital;

        //Read csv file and get all values
        Path basePath = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path resourcePath = basePath.resolve(Paths.get("src", "main", "resources"));

        Path hospitalGeneralFilePath = resourcePath.resolve(hospitalGeneralFileName);
        Path hospitalSpendingFilePath = resourcePath.resolve(hospitalSpendingFileName);
        Path hospitalCareFilePath = resourcePath.resolve(hospitalCareFileName);

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(hospitalGeneralFilePath.toString()));
            String row = csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String facilityId = data[0];
                String facilityName = data[1];
                String address=data[2];
                String city=data[3];
                String state=data[4];
                String zipCode=data[5];
                String countryName=data[6];
                String phoneNumber=data[7];
                String hospitalType=data[8];
                String ownership=data[9];
                boolean emergencyService=Boolean.getBoolean(data[10]);
                String rating=data[12];

                //Creating hospital object
                hospital=Hospital.create(facilityId).name(facilityName).address(address).city(city).state(state).zipCode(zipCode).country(countryName).
                        phoneNumber(phoneNumber).type(hospitalType).ownership(ownership).hasEmergency(emergencyService).rating(rating).validate();
                //Adding hospital to the hashmap of hospitals
                hospitalsMap.put(facilityId, hospital);
            }

            //Reading file hospital spending by claim
            csvReader = new BufferedReader(new FileReader(hospitalSpendingFilePath.toString()));
            row=csvReader.readLine();
            while((row=csvReader.readLine())!=null){
                String[] data = row.split(",");
                final String facilityID=data[0];
                String state=data[2];
                String averageSpendingPerEpisodeHopsital=data[5];
                String averageSpendingPerEpisodeState=data[6];
                String averageSpendingPerEpisodeNation=data[7];

                //Check if the facilityId already exists in the map, modify the object by adding averageSpendig
                hospital = instanceModel.getHospital(facilityID);
                if(hospital!=null)
                    hospital.setMedicareAmount(averageSpendingPerEpisodeHopsital);

                //Adding average spending to the state
                addStateSpending(state,averageSpendingPerEpisodeState);

                //Adding average spending to the nation
                addNationSpending(averageSpendingPerEpisodeNation);


            }

            //Reading file timely effective grouped by id
            csvReader = new BufferedReader(new FileReader(hospitalCareFilePath.toString()));
            row = csvReader.readLine();
            System.out.println(row);
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String facilityID = data[0];
                String totalScore = data[8];

                //Check if facility already exists in the hashmap and modify the hospital object
                hospital = instanceModel.getHospital(facilityID);
                if(hospital!=null)
                    hospital.setScore(totalScore);
            }
        } catch(Exception e){
            System.out.println("Exception in reading file"+ e.toString());
        }

        addHospitalToModel(new ArrayList<>(hospitalsMap.values()));
//        instanceModel.writeToFile();
    }
}
