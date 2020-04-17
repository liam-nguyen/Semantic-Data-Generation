package project2;

import project2.Helper.Hospital;
import project2.Ontology.OurModel;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        // Filtered hospital
        pool.execute(() -> {
            System.out.println("Build a filtered hospital list by name, score and medicare ");
            try {
                new OurModel()
                        .build(List.of(
                                Hospital.isWithName(),
                                Hospital.isWithScore(),
                                Hospital.isWithMedicareSpending()))
                        .writeModelToFile("Filtered_Hospital.owl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Full hospital
//        pool.execute(() -> {
//            System.out.println("Build a full (non-filtered) hospital list");
//            try {
//                new OurModel().build().writeModelToFile("Full_Hospital.owl");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }});

        // Clean up
        System.out.println("Jobs completed");
        pool.shutdown();
    }
}
