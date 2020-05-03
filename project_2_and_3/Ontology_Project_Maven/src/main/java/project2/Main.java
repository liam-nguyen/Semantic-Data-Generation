package project2;

import org.apache.jena.ontology.OntModel;
import project2.Helper.CSVData;
import project2.Helper.Hospital;
import project2.Ontology.OurModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            System.out.println("Build a filtered hospital list by name, score and medicare ");
            try {
                OurModel model = new OurModel();
                model.build(List.of(
                                Hospital.isWithName(),
                                Hospital.isWithScore(),
                                Hospital.isWithMedicareSpending()));
                model.writeModelToFile("Filtered_Hospital.owl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            System.out.println("Build a filtered hospital list by name, score and medicare ");
            try {
                OurModel model = new OurModel();
                model.build(new ArrayList<>());
                model.writeModelToFile("Full_Hospital.owl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Clean up
        System.out.println("Jobs completed");
        pool.shutdown();
    }
}
