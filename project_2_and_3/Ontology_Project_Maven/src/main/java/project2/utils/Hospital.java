package project2.utils;

import com.google.common.net.UrlEscapers;
import org.apache.http.client.utils.URIBuilder;
import project2.Hospital.OntModel;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
            state = "",
            country = "USA",
            city = "",
            phoneNumber = "";
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
        return state;
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
    public String getIDAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(ID);
    }

    public String getNameAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(hospitalName);
    }

    public String getScoreAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(score);
    }

    public String getRatingAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(rating);
    }

    public String getOwnershipAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(ownershipName);
    }

    public String getTypeAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(type);
    }

    public String getZipcodeAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(zipcode);
    }

    public String getAddressAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(address);
    }

    public String getStateAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(state);
    }

    public String getCountryAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(country);
    }

    public String getCityAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(city);
    }

    public String getPhoneNumberAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(phoneNumber);
    }

    public String getMedicareAmountAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(medicareAmount);
    }

    public String getHasEmergencyAsURI() throws URISyntaxException, UnsupportedEncodingException {
        return escapeURI(hasEmergency);
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
        this.state = state;
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

    //== Private methods ==//
    private String escapeURI(String s) throws UnsupportedEncodingException {
        return UrlEscapers.urlFragmentEscaper().escape(s);
    }

    public static void main(String[] args) {
        List<Predicate<Hospital>> validator = new ArrayList<>();
        validator.add(hospital -> !hospital.hospitalName.equals(""));
        validator.add(hospital -> !hospital.medicareAmount.equals("-1"));
        validator.add(hospital -> !hospital.phoneNumber.equals(""));

        Hospital good = Hospital.create("1")
                .hasName("Good")
                .hasMedicareSpending("50")
                .hasPhoneNumber("111-111-1111");
        Hospital bad1 = Hospital.create("2")
                .hasName("Bad1")
                .hasPhoneNumber("111-111-1111");
        Hospital bad2 = Hospital.create("3")
                .hasPhoneNumber("111-111-1111");

        List<Hospital> hospitals = new ArrayList<>();
        hospitals.add(good);
        hospitals.add(bad1);
        hospitals.add(bad2);

        hospitals.forEach(h -> {
            System.out.print("Hospital " + h.getID() + " is " +
                    validator.stream().reduce(x -> true, Predicate::and).test(h) + "\n");
        });
    }
}
