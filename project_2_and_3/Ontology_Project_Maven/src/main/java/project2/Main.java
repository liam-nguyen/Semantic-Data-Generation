package project2;

import project2.Helper.Hospital;
import project2.Ontology.OurModel;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Build another owl in different thread
        Thread t = new Thread(() -> {
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
        t.start();

        // Build full owl in main thread
        System.out.println("Build a non-filtered hospital list");
        try {
            new OurModel().build().writeModelToFile("Full_Hospital.owl");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait for the other thread to finish
        t.join();

        System.out.println("All threads exited");
    }
}
