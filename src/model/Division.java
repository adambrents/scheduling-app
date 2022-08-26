package model;

public class Division {

    private String name;
    private int countryID;

    /**
     * constructor for division
     * @param name
     * @param countryID
     */
    public Division(String name, int countryID) {
        this.name = name;
        this.countryID = countryID;
    }

    /**
     * getter/setter for division
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getter/setter for division
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter/setter for division
     * @return
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * getter/setter for division
     * @param countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
