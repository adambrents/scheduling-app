package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerID;
    private int userID;
    private int contactID;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String division;
    private String contactName;

    /**
     * constructor for appointments
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @param startDate
     * @param startTime
     * @param endTime
     * @param division
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerID, int userID, int contactID, LocalDate startDate, LocalTime startTime, LocalTime endTime, String division, String contactName) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.division = division;
        this.contactName = contactName;
    }

    /**
     * gets appt id
     *
     * @return
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * sets appt id
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * gets title
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * gets type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * sets type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * gets start
     * @return
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * sets start
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * gets end
     * @return
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * sets end
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * gets customerID
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * sets customer Id
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * gets user ID
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * set UserId
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * get Contact Id
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * sets ContactId
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * gets StartDate
     *
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * sets startDate
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * gets Start Time
     * @return
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * sets Start Time
     * @param startTime
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * gets end Time
     * @return
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * sets end time
     * @param endTime
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * gets division
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     * sets division
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * gets contact name
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * sets contact name
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
