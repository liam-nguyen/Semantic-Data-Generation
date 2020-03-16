package project2.Hospital;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;
import project2.Hospital.utils.Hospital;
import project2.Hospital.utils.State;
import project2.Hospital.utils.US_States;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class gathers data to build a model
 */
public class OntAPI {
    public static org.apache.jena.ontology.OntModel model;
    static List<Hospital> hospitals;
    List<State> states;
    String nationAverage = "-1";

    public OntAPI() {
        model = new OntModel().getModel();
        hospitals = new ArrayList<Hospital>();
        states = new ArrayList<State>();
    }

    // public void addHospital(Hospital h) {
    //    hospitals.add(h);
    // }
//    Test Commit
    public void addState(State s) {
        states.add(s);
    }
    public void addNationMedicareSpending(String amount) {
        nationAverage = amount;
    }

    public static void addHospitalToModel(List<Hospital> hospitals)  {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("model.rdf"), "utf-8"));

        for (Hospital h:hospitals) {
            // h=hospitals.get(0);
            OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
            Individual instance = hospital.createIndividual(OntModel.NS +h.getHospitalName() );
            Property hasID = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityID);
            Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityName);
            Property hasEmergencyService=model.getProperty(OntModel.NS+OntModel.Props.hasEmergencyService);
            Property hasPhoneNumber=model.getProperty(OntModel.NS+OntModel.Props.hasPhoneNumber);
            Property hasScore=model.getProperty(OntModel.NS+OntModel.Props.hasScore);
            Property hasRating=model.getProperty(OntModel.NS+OntModel.Props.hasRating);
            Property hasMedicareSpending=model.getProperty(OntModel.NS+OntModel.Props.hasAverageMedicareSpending);
            //  RDFNode phoneNumberRange=model.createTypedLiteral(h.getPhoneNumber(), XSDDatatype.XSDstring);
            // hasPhoneNumber.addProperty(RDFS.range,instance);

            model.add(instance, hasFacilityName, h.getHospitalName());
            model.add(instance, hasID, h.getID());
            model.add(instance,hasEmergencyService,String.valueOf(h.getHasEmergency()));
            model.add(instance,hasPhoneNumber,h.getPhoneNumber());
            model.add(instance,hasScore,h.getRating());
            model.add(instance,hasScore,h.getScore());
            model.add(instance,hasRating,h.getRating());
           // System.out.println("Medicare amount"+h.getMedicareAmount());
            model.add(instance,hasMedicareSpending,h.getMedicareAmount());
            //  model.add(instance,hasPhoneNumber,phoneNumberRange);

            //Write model to a file
            model.write(writer);
          //  model.write(System.out);

        }
        }catch(Exception e){
            System.out.println("Exception in writing to a file"+e);
        }
    }

 /*   public void addHospitalName(String ID, String name ) {
        if (Integer.parseInt(ID) < 0) throw new IllegalArgumentException("Only positive ID");
        // Gather classes and properties
        OntClass hospital = model.getOntClass(OntModel.NS + OntModel.Classes.Hospital);
        Individual instance = hospital.createIndividual(OntModel.NS + name);
//        Individual stateInstance = model.getIndividual(NS + inputState);
//        if (stateInstance == null) {
//            OntClass state = model.getOntClass(NS + Classes.State);
//            stateInstance = state.createIndividual(NS + inputState);
//            stateInstance.addComment(US_States.getFullStateName(inputState), "EN");
//        }
        Property hasID = model.getProperty(OntModel.NS + OntModel.Props.hasID);
        Property hasFacilityName = model.getProperty(OntModel.NS + OntModel.Props.hasFacilityName);
//        Property hasLocation = model.getProperty(duURI + "hasLocation");
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);

        // Add to model
        model.add(instance, hasFacilityName, name);
        model.add(instance, hasID, ID);
//        model.add(instance, hasLocation, stateInstance);
//        model.add(instance, hasAverageSpending, amount);
    }

//    public void addStateSpending(String stateName, String amount) {
//        // Gather classes and properties
//        OntClass state = model.getOntClass(NS + Classes.State);
//        Individual stateInstance = model.getIndividual(NS + stateName);
//        if (stateInstance == null) {
//            stateInstance = state.createIndividual(NS + stateName);
//            stateInstance.addComment(US_States.getFullStateName(stateName), "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(stateInstance, hasAverageSpending, amount);
//    }

//    public void addNationSpending(String amount) {
//        // Gather classes and properties
//        OntClass nation = model.getOntClass(NS + Classes.Nation);
//        Individual instance = model.getIndividual(NS + "USA");
//        if (instance == null) {
//            instance = nation.createIndividual(NS + "USA");
//            instance.addLabel("United State of America", "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(instance, hasAverageSpending, amount);
//    }
*/
    public void display() {
        model.write(System.out);
    }

    public void writeToFile(Path path) throws IOException {
        Path filePath = path.resolve("Hospital.owl");
        FileWriter out = new FileWriter(filePath.toString());
        model.write(out);
    }
    public void writeToFile() throws IOException {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        writeToFile(root);
    }

    public static void main(String[] args) throws IOException {
//        OntAPI ont = new OntAPI();
//        ont.display();
        OntAPI instanceModel = new OntAPI();
        Hospital hospital;
        //Read csv file and get all values
        String pathToHospitalSpendigByClaim="/Users/SuchitraMasters/Desktop/CECS-571-master-7/project_2/datasets/Aggregated/Medicare_Hospital_Spending_by_claim_groupbyID.csv";
        String pathToGeneralInformation="/Users/SuchitraMasters/Desktop/CECS-571-master-7/project_2/datasets/Original/Hospital_General_Information.csv";
        String pathToTimelyEffectivelyGroupedById="/Users/SuchitraMasters/Desktop/CECS-571-master-7/project_2/datasets/Aggregated/Timely_Effective_groupedByID.csv";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToGeneralInformation));
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
                Boolean emergencyService=Boolean.getBoolean(data[10]);
                String rating=data[12];
                //Creating hospital object
                hospital=Hospital.create(facilityId).name(facilityName).address(address).city(city).state(state).zipCode(zipCode).country(countryName).
                        phoneNumber(phoneNumber).type(hospitalType).ownership(ownership).hasEmergency(emergencyService).rating(rating).validate();
               //Adding hospital to list of hospitals
                hospitals.add(hospital);
            }

            //Reading file hospital spending by claim
            csvReader = new BufferedReader(new FileReader(pathToHospitalSpendigByClaim));
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

                //Check if the facility id already exists, if it exists modify the ibject by adding values
                Iterator<Hospital> iterator = hospitals.iterator();
                while (iterator.hasNext()) {
                    Hospital hospital1 = iterator.next();
                    if (hospital1.getID().equals(facilityID)) {
                        hospital1.setMedicareAmount(averageSpendingPerEpisodeHopsital);
                    }
                }
            }
            //Reading file timely effective grouped by id
            csvReader = new BufferedReader(new FileReader(pathToTimelyEffectivelyGroupedById));
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
        }catch(Exception e){
            System.out.println("Exception in reading file"+e.toString());
        }

        addHospitalToModel(hospitals);

        //Hospital A = Hospital.create("1234").name("Liam").ownership("Self").validate();
        //instanceModel.addHospitalName(A.ID, A.hospitalName);
        //instanceModel.display();
        //System.out.println(A);
        instanceModel.writeToFile();
    }
}
