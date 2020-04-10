package project2.Helper;

<<<<<<< Updated upstream
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import project2.Util.URI_Helper;
=======
import java.util.function.Predicate;

import lombok.Getter;
import Util.URI_Helper;
>>>>>>> Stashed changes

public class Hospital {
    //== Instance fields ==//
    private String ID,
            hospitalName = "",
            score = "-1",
            rating = "-1",
            ownershipName = "",
            type = "";
    private String zipcode = "",
            address = "",
            country = "USA",
            city = "",
            phoneNumber = "";
    private State state;
    private String medicareAmount = "-1";
    private String hasEmergency = "";

    //== Constructor ==//
    private Hospital(String ID) {
        this.ID = ID;
    }

    //== Public method ==//
    //== == Creation == ==//
    public static Hospital create(String ID) {
        return new Hospital(ID);
    }

    //== == Getters == ==//
    public String getID() {
        return ID;
    }

    public String getName() {
        return hospitalName;
    }

    public String getScore() {
        return score;
    }

    public String getRating() {
       return rating;
    }

    public String getOwnership() {
        return ownershipName;
    }

    public String getType() {
        return type;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state.getAbbr();
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMedicareAmount() {
        return medicareAmount;
    }

    public String getHasEmergency() {
        return hasEmergency;
    }

    //== == Getters for URI == ==//
    public String getIDAsURI() {
        return URI_Helper.escapeURI(ID);
    }

    public String getNameAsURI() {
        return URI_Helper.escapeURI(hospitalName);
    }

    public String getScoreAsURI() {
        return URI_Helper.escapeURI(score);
    }

    public String getRatingAsURI() {
        return URI_Helper.escapeURI(rating);
    }

    public String getOwnershipAsURI() {
        return URI_Helper.escapeURI(ownershipName);
    }

    public String getTypeAsURI() {
        return URI_Helper.escapeURI(type);
    }

    public String getZipcodeAsURI() {
        return URI_Helper.escapeURI(zipcode);
    }

    public String getAddressAsURI() {
        return URI_Helper.escapeURI(address);
    }

    public String getStateAsURI() {
        return URI_Helper.escapeURI(state.getAbbr());
    }

    public String getCountryAsURI() {
        return URI_Helper.escapeURI(country);
    }

    public String getCityAsURI() {
        return URI_Helper.escapeURI(city);
    }

    public String getPhoneNumberAsURI() {
        return URI_Helper.escapeURI(phoneNumber);
    }

    public String getMedicareAmountAsURI() {
        return URI_Helper.escapeURI(medicareAmount);
    }

    public String getHasEmergencyAsURI() {
        return URI_Helper.escapeURI(hasEmergency);
    }

    //== == Parsed getter == ==//
    public Optional<Integer> getScoreParsed() {
        try {
            return Optional.of(Integer.parseInt(score));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getRatingParsed() {
        try {
            return Optional.of(Integer.parseInt(rating));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Double> getMedicareAmountParsed() {
        try {
            return Optional.of(Double.parseDouble(medicareAmount));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }


    //== == Method to build a hospital object == ==//
    public Hospital hasName(String n) {
        this.hospitalName = n;
        return this;
    }

    public Hospital hasEmergency(String b) {
        this.hasEmergency = b;
        return this;
    }

    public Hospital hasZipCode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public Hospital hasAddress(String address) {
        this.address = address;
        return this;
    }

    public Hospital hasState(String state) {
        this.state = State.create(state);
        return this;
    }

    public Hospital hasCountry(String country) {
        this.country = country;
        return this;
    }

    public Hospital hasCity(String city) {
        this.city = city;
        return this;
    }

    public Hospital hasMedicareSpending(String amount) {
        this.medicareAmount = amount;
        return this;
    }

    public Hospital hasScore(String score) {
        this.score = score;
        return this;
    }

    public Hospital hasRating(String rating) {
        this.rating = rating;
        return this;
    }

    public Hospital hasType(String type) {
        this.type = type;
        return this;
    }

    public Hospital hasOwnership(String ownershipName) {
        this.ownershipName = ownershipName;
        return this;
    }

    public Hospital hasPhoneNumber(String number) {
        this.phoneNumber = number;
        return this;
    }

    //== == Other public methods == ==//
    @Override
    public String toString() {
        return "Hospital{" +
                "ID='" + ID + '\'' +
                ", name='" + hospitalName + '\'' +
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

    /**
     * Check if a hospital object is valid against a list of validators
     * @param validators a list of predicates
     * @return true if hospital is valid, else false
     */
    public boolean isValid(List<Predicate<Hospital>> validators) {
        return validators.stream().reduce(x->true, Predicate::and).test(this);
    }

    /**
     * heck if a hospital object is valid against a default list of validators
     * @return true if hospital is valid, else false
     */
    public boolean isValid() {
        List<Predicate<Hospital>> validators = new ArrayList<>();

        // List of validators
        validators.add(hospital -> !hospital.getName().equals("") || hospital.getName() != null);
        validators.add(hospital -> !hospital.getScore().equals("-1"));
        validators.add(hospital -> !hospital.getMedicareAmount().equals("-1"));

        return isValid(validators);
    }
}
