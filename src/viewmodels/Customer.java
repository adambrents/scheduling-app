package viewmodels;

public class Customer {
    private int Id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String country;
    private String division;
    private int divisionId;

    /**
     * constructor for customer
     * @param Id
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param country
     * @param division
     * @param divisionId
     */
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

    /**
     * getter/setter for Customer
     * @return
     */
    public int getId() {
        return Id;
    }

    /**
     * getter/setter for Customer
     * @param Id
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getter/setter for Customer
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * getter/setter for Customer
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * getter/setter for Customer
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * getter/setter for Customer
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * getter/setter for Customer
     *
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter/setter for Customer
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     * getter/setter for Customer
     * @param division
     */
    public void setDivision(String division) {this.division = division;}

    /**
     * getter/setter for Customer
     * @return
     */
    public int getDivisionId() {return divisionId;}

    /**
     * getter/setter for Customer
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {this.divisionId = divisionId;}
}
