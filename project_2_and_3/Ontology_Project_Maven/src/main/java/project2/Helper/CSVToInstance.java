package project2.Helper;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import project2.Instance.Hospital;
import project2.Instance.State;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CSVToInstance {
    public static Map<String, Hospital> hospitals = new HashMap<>(); // String = facilityID
    public static Map<String, State> states = new HashMap<>(); // String = full State's name
    public static String nationalAverage = "-1"; // National Average Medicare Spending

    static {
        try {
            parseGeneralCSV();
            parseSpendingCSV();
            parseCareCSV();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Hospital> getHospitals() {
        return hospitals;
    }
    public static Map<String, State> getStates() {
        return states;
    }
    public static String getNationalAverage() {
        return nationalAverage;
    }

    //== Private methods ==//
    //== == CSVs == ==//
    private static void parseGeneralCSV() throws IOException, CsvValidationException {
        final String hospitalGeneralFileName = "Hospital_General_Information.csv";
        Hospital hospital;
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(CSVToInstance.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalGeneralFileName))))
                .withSkipLines(1)
                .build();


        String[] data;
        while ((data = openCsvReader.readNext()) != null) {

            String facilityId = Utils.stripLeadingZeros(data[0]);
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
            String rating = Utils.stripLeadingZeros(data[12]);

            //Creating hospital object
            hospital = getHospital(facilityId)
                    .hasName(facilityName)
                    .hasAddress(address)
                    .hasCity(city)
                    .hasState(state)
                    .hasZipCode(zipCode)
                    .hasCountry(countryName)
                    .hasPhoneNumber(phoneNumber)
                    .hasType(hospitalType)
                    .hasOwnership(ownership)
                    .hasEmergency(emergencyService)
                    .hasRating(rating);

            //Adding hospital to the hashmap of hospitals
            hospitals.put(facilityId, hospital);
        }
    }

    //Reading file hospital spending by claim
    private static void parseSpendingCSV() throws IOException, CsvValidationException {
        final String hospitalSpendingFileName = "Medicare_Hospital_Spending_by_claim_groupbyID.csv";
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(CSVToInstance.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalSpendingFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            final String facilityID = Utils.stripLeadingZeros(data[0]);
            String state = data[2];
            String averageSpendingPerEpisodeHospital = Utils.stripLeadingZeros(data[5]);
            String averageSpendingPerEpisodeState = Utils.stripLeadingZeros(data[6]);
            String averageSpendingPerEpisodeNation = Utils.stripLeadingZeros(data[7]);

            //Check if the facilityId already exists in the map, modify the object by adding averageSpending
            Hospital hospital = getHospital(facilityID);
            hospital.hasMedicareSpending(averageSpendingPerEpisodeHospital);
            hospitals.put(facilityID, hospital);

            //Adding average spending to the state
            State stateInstance = getState(state);
            stateInstance.setAverageMedicareAmount(averageSpendingPerEpisodeState);
            states.put(stateInstance.getAbbr(), stateInstance);

            //Adding average spending to the nation
            nationalAverage = nationalAverage.equals("-1") ? averageSpendingPerEpisodeNation : nationalAverage;
        }
    }

    //Reading file timely effective grouped by id
    private static void parseCareCSV() throws IOException, CsvValidationException {
        final String hospitalCareFileName = "Timely_Effective_groupedByID.csv";
        Hospital hospital;

        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(CSVToInstance.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalCareFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            String facilityID = Utils.stripLeadingZeros(data[0]);
            String totalScore = (data[8]);

            hospital = getHospital(facilityID);
            hospital.hasScore(totalScore);
            hospitals.put(facilityID, hospital);
        }
    }

    private static Hospital getHospital(String ID) {
        return hospitals.containsKey(ID)
                ? hospitals.get(ID)
                : Hospital.create(ID);
    }

    private static State getState(String abbr) {
        return states.containsKey(abbr) ? states.get(abbr) : State.create(abbr);
    }
}
