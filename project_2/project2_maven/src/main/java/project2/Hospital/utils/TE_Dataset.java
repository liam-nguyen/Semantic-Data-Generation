package project2.Hospital.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author D.Phuc
 */
public class TE_Dataset {
    private String facilityID, facilityName, address, city, state, zipCode, countyName, phoneNumber, score, sample = "";

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    private static Function<String, TE_Dataset> csv2TE_DatasetObj = (line) -> {
        String[] record = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); //split by comma but regex rule is to ignore comma(s) inside a record
        TE_Dataset ex = new TE_Dataset();
        boolean passed = true;
        for (int i=0; i<=13; i++) {
            if(i==8 || i==9 || i==10) continue;
            if(record[i] == null || record[i].trim().length() < 0) passed = false;
        }
        if(passed) {
            String regex = "[0-9]+";
            ex.setFacilityID(record[0]);
            ex.setFacilityName(record[1]);
            ex.setAddress(record[2]);
            ex.setCity(record[3]);
            ex.setState(record[4]);
            ex.setZipCode(record[5]);
            ex.setCountyName(record[6]);
            ex.setPhoneNumber(record[7]);
            if(record[11].trim().length() == 0 || !(record[11].matches(regex))) ex.setScore("0"); 
            else ex.setScore(record[11]);
            if(record[12].trim().length() == 0 || !(record[12].matches(regex))) ex.setSample("0");
            else ex.setSample(record[12]);
            
        }
        return ex;
    };
    
    
    private static List<TE_Dataset> readCSV(String filePath) {
        List<TE_Dataset> TE_DatasetList = new ArrayList<TE_Dataset>();
        try {
            File f = new File(filePath);
            InputStream is = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            TE_DatasetList = br.lines().skip(1).map(csv2TE_DatasetObj).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return TE_DatasetList;
    } 
    
    public static Map<String, List<TE_Dataset>> IdForTE_Dataset(List<TE_Dataset> exList){
        return exList.stream().collect(Collectors.groupingBy(TE_Dataset::getFacilityID));
    }
    
    public static List<TE_Dataset> processFile(String filePath) {
        return TE_Dataset.readCSV(filePath);
    }
    
    public static void writeFile(Map<String, List<TE_Dataset>> groupbyMap) throws IOException {
        FileWriter fw = new FileWriter("new_TE_Dataset.csv");
        fw.append("Facility ID");	
        fw.append(",");
        fw.append("Facility Name");
        fw.append(",");
        fw.append("Address");
        fw.append(",");
        fw.append("City");
        fw.append(",");
        fw.append("State");
        fw.append(",");
        fw.append("ZIP Code");
        fw.append(",");
        fw.append("County Name");
        fw.append(",");
        fw.append("Phone Number");
        fw.append(",");
        fw.append("Total Score");
        fw.append(",");
        fw.append("Total Sample");
        fw.append("\n");
        Iterator<String> keySet = groupbyMap.keySet().iterator();
        int totalScore = 0;
        int totalSample = 0;
        TE_Dataset ex = new TE_Dataset();
        while(keySet.hasNext()){
            String key = keySet.next();
            Iterator<TE_Dataset> iter = groupbyMap.get(key).iterator();
            while(iter.hasNext()) {
                ex = iter.next();
                totalScore += Integer.valueOf(ex.getScore());
                totalSample += Integer.valueOf(ex.getSample());
            }
            fw.append(ex.getFacilityID());
            fw.append(",");
            fw.append(ex.getFacilityName());
            fw.append(",");
            fw.append(ex.getAddress());
            fw.append(",");
            fw.append(ex.getCity());
            fw.append(",");
            fw.append(ex.getState());
            fw.append(",");
            fw.append(ex.getZipCode());
            fw.append(",");
            fw.append(ex.getCountyName());
            fw.append(",");
            fw.append(ex.getPhoneNumber());
            fw.append(",");
            fw.append(totalScore == 0 ? "-1" : String.valueOf(totalScore));
            fw.append(",");
            fw.append(totalSample == 0 ? "-1" : String.valueOf(totalSample));
            fw.append("\n");
            totalScore = 0;
            totalSample = 0;
        }
        fw.flush();
        fw.close();
    }
}