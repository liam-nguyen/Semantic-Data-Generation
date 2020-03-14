package project2.Hospital;

/**
 * This class gathers data to build a model
 */
public class OntAPI {
    public org.apache.jena.ontology.OntModel model;

    public OntAPI() {
        model = new OntModel().getModel();
    }

    private static class Hospital {
         String ID, name, score = "-1", rating = "-1", ownershipName, type;
         String zipcode, address, state, country = "USA", city;
         String medicareAmount = "-1";
         boolean hasEmergency;

        private Hospital(String ID) {
            this.ID = ID;
        }

        public static Hospital create(String ID) {
            return new Hospital(ID);
        }

        public Hospital name(String name) {
            this.name = name;
            return this;
        }

        public Hospital hasEmergency(boolean b) {
            this.hasEmergency = b;
            return this;
        }

        public Hospital zipCode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public Hospital address(String address) {
            this.address = address;
            return this;
        }

        public Hospital state(String state) {
            this.state = state;
            return this;
        }

        public Hospital country(String country) {
            this.country = country;
            return this;
        }

        public Hospital city(String city) {
            this.city = city;
            return this;
        }

        public Hospital medicareSpending(String amount) {
            this.medicareAmount = amount;
            return this;
        }

        public Hospital score(String score) {
            this.score = score;
            return this;
        }

        public Hospital rating(String rating) {
            this.rating = rating;
            return this;
        }

        public Hospital type(String type) {
            this.type = type;
            return this;
        }

        public Hospital ownership(String ownershipName) {
            this.ownershipName = ownershipName;
            return this;
        }

        public Hospital validate() {
            return this;
        }

        @Override
        public String toString() {
            return "Hospital{" +
                    "ID='" + ID + '\'' +
                    ", name='" + name + '\'' +
                    ", score='" + score + '\'' +
                    ", rating='" + rating + '\'' +
                    ", ownershipName='" + ownershipName + '\'' +
                    ", type='" + type + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", address='" + address + '\'' +
                    ", state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", city='" + city + '\'' +
                    ", medicareAmount='" + medicareAmount + '\'' +
                    ", hasEmergency=" + hasEmergency +
                    '}';
        }
    }

    public void addStateMedicareSpending(String stateName, String amount) {}
    public void addNationMedicareSpending(String country, String amount) {}

//    public void addHospitalName(String ID, String name ) {
//        if (Integer.parseInt(ID) < 0) throw new IllegalArgumentException("Only positive ID");
//        // Gather classes and properties
//        OntClass hospital = model.getOntClass(NS + Classes.Hospital);
//        Individual instance = hospital.createIndividual(NS + name);
//        Individual stateInstance = model.getIndividual(NS + inputState);
//        if (stateInstance == null) {
//            OntClass state = model.getOntClass(NS + Classes.State);
//            stateInstance = state.createIndividual(NS + inputState);
//            stateInstance.addComment(US_States.getFullStateName(inputState), "EN");
//        }
//        Property hasID = model.getProperty(NS + Props.hasID);
//        Property hasFacilityName = model.getProperty(NS + Props.hasFacilityName);
//        Property hasLocation = model.getProperty(duURI + "hasLocation");
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//
//        // Add to model
//        model.add(instance, hasFacilityName, name);
//        model.add(instance, hasID, ID);
//        model.add(instance, hasLocation, stateInstance);
//        model.add(instance, hasAverageSpending, amount);
//    }

//    public void addStateSpending(String stateName, String amount) {
//        // Gather classes and properties
//        OntClass state = model.getOntClass(NS + Classes.State);
//        Individual stateInstance = model.getIndividual(NS + stateName);
//        if (stateInstance == null) {
//            stateInstance = state.createIndividual(NS + stateName);
//            stateInstance.addComment(US_States.getFullStateName(stateName), "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(stateInstance, hasAverageSpending, amount);
//    }

//    public void addNationSpending(String amount) {
//        // Gather classes and properties
//        OntClass nation = model.getOntClass(NS + Classes.Nation);
//        Individual instance = model.getIndividual(NS + "USA");
//        if (instance == null) {
//            instance = nation.createIndividual(NS + "USA");
//            instance.addLabel("United State of America", "EN");
//        }
//        Property hasAverageSpending = model.getProperty(NS + Props.hasAverageSpending);
//        model.add(instance, hasAverageSpending, amount);
//    }

    public void display() {
        model.write(System.out);
    }

    public static void main(String[] args) {
//        OntAPI ont = new OntAPI();
//        ont.display();

        Hospital A = OntAPI.Hospital.create("1234").name("Liam").ownership("Self").validate();
        System.out.println(A);
    }
}
