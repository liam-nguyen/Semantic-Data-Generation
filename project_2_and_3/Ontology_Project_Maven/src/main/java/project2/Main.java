package project2;

import project2.Helper.Hospital;
import project2.Ontology.OurModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;


public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            System.out.println("Build a filtered hospital list by name, score and medicare ");
            try {
                new OurModel()
                        .build(List.of(
                                Hospital.isWithName(),
                                Hospital.isWithScore(),
                                Hospital.isWithMedicareSpending()))
                        .writeModelToFile("Hospital.owl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            System.out.println("Build a non-filtered hospital list");
            try {
                new OurModel().build().writeModelToFile("Full_Hospital.owl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
    }
}
