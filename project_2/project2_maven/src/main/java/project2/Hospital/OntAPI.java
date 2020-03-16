package project2.Hospital;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;
import project2.Hospital.utils.Stopwatch;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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

    public OntAPI() {
        model = new OntModel().getModel();
        hospitals = new ArrayList<>();
        states = new ArrayList<>();
        hospitalsMap = new HashMap<>();
    }

     public void addNationMedicareSpending(String amount) {
        nationAverage = amount;
    }

    public static String URLEncoder(String s) {
        String encoded_s = null;
        try {
            encoded_s = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        return encoded_s;
    }

    public static void addHospitalToModel(List<Hospital> hospitals) {
//        try {
//            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("model.rdf"), "utf-8"));

            for (Hospital h : hospitals) {
                OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
                Individual instance = hospital.createIndividual(OntModel.NS + URLEncoder(h.getHospitalName()));
                Property hasID = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityID);
                Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityName);
                Property hasEmergencyService = model.getProperty(OntModel.NS + OntModel.Props.hasEmergencyService);
                Property hasPhoneNumber = model.getProperty(OntModel.NS + OntModel.Props.hasPhoneNumber);
                Property hasScore = model.getProperty(OntModel.NS + OntModel.Props.hasScore);
                Property hasRating = model.getProperty(OntModel.NS + OntModel.Props.hasRating);
                Property hasMedicareSpending = model.getProperty(OntModel.NS + OntModel.Props.hasAverageMedicareSpending);

                model.add(instance, hasFacilityName, URLEncoder(h.getHospitalName()));
                model.add(instance, hasID, h.getID());
                model.add(instance, hasEmergencyService, String.valueOf(h.getHasEmergency()));
                model.add(instance, hasPhoneNumber, h.getPhoneNumber());
                model.add(instance, hasScore, h.getRating());
                model.add(instance, hasScore, h.getScore());
                model.add(instance, hasRating, h.getRating());
                model.add(instance, hasMedicareSpending, h.getMedicareAmount());
//                System.out.println("Done hospital" + h.toString());
            }
//            Write model to a file
//            model.write(writer);
//        } catch (Exception e){
//            System.out.println("Exception in writing to a file"+e);
//        }
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

    public static void main(String[] args) throws IOException, URISyntaxException {
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
                //  row = csvReader.readLine();
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
               //Adding hospital to list of hospitals
//                hospitals.add(hospital);
                hospitalsMap.put(facilityId, hospital);
            }

            //Reading file hospital spending by claim
            csvReader = new BufferedReader(new FileReader(hospitalSpendingFilePath.toString()));
            row=csvReader.readLine();
            while((row=csvReader.readLine())!=null){
                String[] data = row.split(",");
                final String facilityID=data[0];
                String facilityName=data[1];
                String state=data[2];
                String period=data[3];
                String claimType=data[4];
                String averageSpendingPerEpisodeHopsital=data[5];
                String averageSpendingPerEpisodeState=data[6];
                String averageSpendingPerEpisodeNation=data[7];

//                //Check if the facility id already exists, if it exists modify the ibject by adding values
//                Iterator<Hospital> iterator = hospitals.iterator();
//                while (iterator.hasNext()) {
//                    Hospital hospital1 = iterator.next();
//                    if (hospital1.getID().equals(facilityID)) {
//                        hospital1.setMedicareAmount(averageSpendingPerEpisodeHopsital);
//                    }
//                }
                hospital = instanceModel.getHospital(facilityID);
                hospital.setMedicareAmount(averageSpendingPerEpisodeHopsital);
            }

            //Reading file timely effective grouped by id
            csvReader = new BufferedReader(new FileReader(hospitalCareFilePath.toString()));
            row = csvReader.readLine();
            System.out.println(row);
            while ((row = csvReader.readLine()) != null) {
                //  row = csvReader.readLine();
                String[] data = row.split(",");
                String facilityID = data[0];
                String facilityName = data[1];
                String Address = data[2];
                String city = data[3];
                String state = data[4];
                String zipcode = data[5];
                String countyName = data[6];
                String PhoneNumber = data[7];
                String totalScore = data[8];
                String totalSample = data[9];
            }
        } catch(Exception e){
            System.out.println("Exception in reading file"+ e.toString());
        }
//        instanceModel.parseCSVs();
//        addHospitalToModel(hospitals);
        System.out.println("Done building hospitalsMap");
        Stopwatch timer = new Stopwatch();
        addHospitalToModel(new ArrayList<>(hospitalsMap.values()));
        System.out.println("Add Hospital Time: " + timer.elapsedTime());

        timer = new Stopwatch();
        instanceModel.writeToFile();
        System.out.println("Write Owl Time: " + timer.elapsedTime());
//        String base = "https://data.medicare.gov/d/nrth-mfg3#";
//        String badString = "HOSPITAL DISTRICT #1 OF RICE COUNTY";
//        System.out.println(URLEncoder.encode(badString, StandardCharsets.UTF_8.toString()));
    }
}
