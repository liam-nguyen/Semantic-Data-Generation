package nyExaples;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import java.io.IOException;

public class Tutorial {
    static String SOURCE = "http://www.eswc2006.org/technologies/ontology";
    static String NS = SOURCE + "#";

    public static void main(String[] args) throws IOException {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        // Create the Classes : animal, plant, sheep, grass, vegetarian;
        OntClass animal = model.createClass(NS + "Animal");
        OntClass plant = model.createClass(NS + "Plant");
        OntClass sheep = model.createClass(NS + "Sheep");
        OntClass grass = model.createClass(NS + "Grass");
        OntClass vegetarian = model.createClass(NS + "Vegetarian");

        // Set sheep as subClass of animal, set grass as subClass of plant;
        animal.addSubClass(sheep);
        plant.addSubClass(grass);

        // Create the object property eat and part_of (there domain and range are owl:Thing);
        ObjectProperty eat = model.createObjectProperty(NS + "eat");
        ObjectProperty partOf = model.createObjectProperty(NS + "partOf");

        // Create Restriction : eatAllGrass, set sheep as its subclass;
        AllValuesFromRestriction avr = model.createAllValuesFromRestriction(null, eat, grass);
        avr.addSubClass(sheep);

        // Create Restriction : partofSomePlant; partofSomeAnimal;
        SomeValuesFromRestriction plantPart = model.createSomeValuesFromRestriction(null, partOf, plant);
        SomeValuesFromRestriction animalPart = model.createSomeValuesFromRestriction(null, partOf, animal);

        // Create the Union Class meat : (animal, partofSomeAnimal);
        RDFNode[] nodes1 = {animal, animalPart};
        RDFList meatList = model.createList(nodes1);
        UnionClass meat = model.createUnionClass(null, meatList);

        // Create the Union Class vegetable : (plant, partofSomePlant):
        RDFNode[] nodes2 = {plant, plantPart};
        RDFList vegetableList = model.createList(nodes2);
        UnionClass vegetable = model.createUnionClass(null, vegetableList);

        model.write(System.out);
//        model.write(System.out, "N3");
    }
}
