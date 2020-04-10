package project2.Helper;


import java.util.function.Predicate;

import lombok.Getter;
import Util.URI_Helper;

public class Hospital {
    //== Instance fields ==//
    @Getter private String
            ID,
            hospitalName = "",
            ownershipName = "",
            type = "",
            hasEmergency = "",
            zipcode = "",
            address = "",
            country = "USA",
            county = "",
            city = "",
            phoneNumber = "";
    @Getter private int score, rating;
    @Getter private double medicareAmount;
    private State state;

    //== Constructor ==//
    private Hospital(String ID) {
        this.ID = ID;
    }

    //== Public method ==//
    //== == Creation == ==//
    public static Hospital create(String ID) {
        return new Hospital(ID);
    }

    //== == Getters for URI == ==//
    public String getIDAsURI() {
        return URI_Helper.escapeURI(ID);
    }
    public String getStateAsURI() {
        return URI_Helper.escapeURI(state.getAbbr());
    }
    public String getCountryAsURI() {
        return URI_Helper.escapeURI(country);
    }
    public String getCountyAsURI() {return URI_Helper.escapeURI(county);}

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
        try {
            this.medicareAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
//            System.out.println("Unknown medicare spending value: " + amount);
            this.medicareAmount = -1;
        }
        return this;
    }

    public Hospital hasScore(String score) {
        try {
            this.score = Integer.parseInt(score);
        } catch (NumberFormatException e) {
//            System.out.println("Unknown score: " + score);
            this.score = -1;
        }
        return this;
    }

    public Hospital hasRating(String rating) {
        try {
            this.rating = Integer.parseInt(rating);
        } catch (NumberFormatException e) {
//            System.out.println("Unknown rating: " + rating);
            this.rating = -1;
        }
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

    public Hospital hasCounty(String countyName) {
        this.county = countyName;
        return this;
    }

    //== == Predicates == ==//
    public static Predicate<Hospital> isWithMedicareSpending() {
        return hospital -> hospital.getMedicareAmount() != -1;
    }

    public static Predicate<Hospital> isWithName() {
        return hospital -> !hospital.getHospitalName().equals("") || hospital.getHospitalName() != null;
    }

    public static Predicate<Hospital> isWithScore() {
        return hospital -> hospital.getScore() != -1;
    }

    //== == Overrides == ==//
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


}
