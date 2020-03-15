package project2.Hospital.utils;

public class Hospital {
        public String ID,hospitalName,score = "-1",rating = "-1",sampleSize = "-1",ownershipName,type;
        public String zipcode, address, state, country = "USA", city, phoneNumber;
        public String medicareAmount = "-1";
        boolean hasEmergency;

        private Hospital(String ID) {
            this.ID = ID;
        }

        public static Hospital create(String ID) {
            return new Hospital(ID);
        }

        public Hospital name(String n) {
            this.hospitalName = n;
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

        public Hospital phoneNumber(String number) {
            this.phoneNumber = number;
            return this;
        }

        public Hospital sample(String size) {
            this.sampleSize = size;
            return this;
        }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(String sampleSize) {
        this.sampleSize = sampleSize;
    }

    public String getOwnershipName() {
        return ownershipName;
    }

    public void setOwnershipName(String ownershipName) {
        this.ownershipName = ownershipName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMedicareAmount() {
        return medicareAmount;
    }

    public void setMedicareAmount(String medicareAmount) {
        this.medicareAmount = medicareAmount;
    }

    public boolean getHasEmergency() {
        return hasEmergency;
    }

    public void setHasEmergency(boolean hasEmergency) {
        this.hasEmergency = hasEmergency;
    }


    public Hospital validate() {
            return this;
        }

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
