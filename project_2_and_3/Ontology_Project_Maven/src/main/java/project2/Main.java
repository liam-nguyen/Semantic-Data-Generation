package project2;

import org.apache.jena.ontology.OntModel;
import project2.Helper.CSVData;
import project2.Helper.Hospital;
import project2.Ontology.OurModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        ExecutorService pool = Executors.newCachedThreadPool();

        // Filtered hospital
//        pool.execute(() -> {
//            System.out.println("Build a filtered hospital list by name, score and medicare ");
//            try {
//                new OurModel()
//                        .build(List.of(
//                                Hospital.isWithName(),
//                                Hospital.isWithScore(),
//                                Hospital.isWithMedicareSpending()))
//                        .writeModelToFile("Filtered_Hospital.owl");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        Map<String, Hospital> hospitals = CSVData.getHospitals();
        System.out.println("Count of hospital with medicare spending == 0: " + hospitals
                .values()
                .stream()
                .filter(hospital -> hospital.getMedicareAmount() == 0)
                .count());
        System.out.println("Count of hospital with medicare spending == -1: " + hospitals
                .values()
                .stream()
                .filter(hospital -> hospital.getMedicareAmount() == -1)
                .count());
        System.out.println("Count of hospital with score == 0: " + hospitals
                .values()
                .stream()
                .filter(hospital -> hospital.getScore() == -1)
                .count());
        System.out.println("Count of hospital with score == -1: " + hospitals
                .values()
                .stream()
                .filter(hospital -> hospital.getScore() == -1)
                .count());


        // Clean up
//        System.out.println("Jobs completed");
//        pool.shutdown();
    }
}
