package project2.Hospital.utils;

public class Hospital {
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
