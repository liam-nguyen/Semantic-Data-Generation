package project2;

import project2.Instance.Hospital;
import project2.Ontology.OntInstanceModel;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        OntInstanceModel instanceModel = new OntInstanceModel();
        instanceModel.build(Hospital::isValid).writeToFile();
    }
}
