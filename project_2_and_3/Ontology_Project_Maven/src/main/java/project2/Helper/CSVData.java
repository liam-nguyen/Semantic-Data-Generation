package project2.Helper;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import Util.URI_Helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CSVData {
    public static Map<String, Hospital> hospitals = new HashMap<>(); // String = facilityID
    public static Map<String, State> states = new HashMap<>(); // String = full State's name
    public static double nationalAverage = -1; // National Average Medicare Spending

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
    public static double getNationalAverage() {
        return nationalAverage;
    }

    //== Private methods ==//
    //== == CSVs == ==//
    private static void parseGeneralCSV() throws IOException, CsvValidationException {
        final String hospitalGeneralFileName = "Hospital_General_Information.csv";
        Hospital hospital;
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(CSVData.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalGeneralFileName))))
                .withSkipLines(1)
                .build();


        String[] data;
        while ((data = openCsvReader.readNext()) != null) {

            String facilityId = URI_Helper.stripLeadingZeros(data[0]);
            String facilityName = data[1];
            String address = data[2];
            String city = data[3];
            String state = data[4];
            String zipCode = data[5];
            String countyName = data[6];
            String phoneNumber = data[7];
            String hospitalType = data[8];
            String ownership = data[9];
            String emergencyService = data[10];
            String rating = URI_Helper.stripLeadingZeros(data[12]);

            //Creating hospital object
            hospital = getHospital(facilityId)
                    .hasName(facilityName)
                    .hasAddress(address)
                    .hasCity(city)
                    .hasState(state)
                    .hasZipCode(zipCode)
                    .hasCounty(countyName)
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
                        Objects.requireNonNull(CSVData.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalSpendingFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            final String facilityID = URI_Helper.stripLeadingZeros(data[0]);
            String state = data[2];
            String averageSpendingPerEpisodeHospital = URI_Helper.stripLeadingZeros(data[5]);
            String averageSpendingPerEpisodeState = URI_Helper.stripLeadingZeros(data[6]);
            String averageSpendingPerEpisodeNation = URI_Helper.stripLeadingZeros(data[7]);

            //Check if the facilityId already exists in the map, modify the object by adding averageSpending
            Hospital hospital = getHospital(facilityID).hasMedicareSpending(averageSpendingPerEpisodeHospital);
            hospitals.put(facilityID, hospital);

            //Adding average spending to the state
            State stateInstance = getState(state).setAverageStateMedicareAmount(averageSpendingPerEpisodeState);
            states.put(stateInstance.getAbbr(), stateInstance);

            //Adding average spending to the nation
            try {
                CSVData.nationalAverage = Double.parseDouble(averageSpendingPerEpisodeNation);
            } catch(NumberFormatException e) {
                CSVData.nationalAverage = -1;
            }
        }
    }

    //Reading file timely effective grouped by id
    private static void parseCareCSV() throws IOException, CsvValidationException {
        final String hospitalCareFileName = "Timely_Effective_groupByID_Average.csv";
        Hospital hospital;

        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(CSVData.class
                                .getClassLoader()
                                .getResourceAsStream(hospitalCareFileName))))
                .withSkipLines(1)
                .build();

        String[] data;
        while ((data = openCsvReader.readNext()) != null) {
            String facilityID = URI_Helper.stripLeadingZeros(data[0]);
            String totalScore = data[8];

            hospital = getHospital(facilityID).hasScore(totalScore);
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
