package model;

public class Customer {
    private int Id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String country;
    private String division;
    private int divisionId;

    public Customer(int Id, String name,String address, String postalCode, String phoneNumber, String country, String division, int divisionId){
        this.Id = Id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.division = division;
        this.divisionId = divisionId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {this.division = division;}

    public int getDivisionId() {return divisionId;}

    public void setDivisionId(int divisionId) {this.divisionId = divisionId;}
}
