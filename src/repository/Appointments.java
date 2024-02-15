package repository;

import repository.configuration.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import viewmodels.Appointment;
import viewmodels.Customer;
import utilities.JDBCConnectionHelper;
import utilities.TimeHelper;

import java.sql.*;
import java.time.*;

public class Appointments {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> allTypes = FXCollections.observableArrayList();
    private static ObservableList<String> customerAppointmentExists = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsInDivision = FXCollections.observableArrayList();
    private static ObservableList<LocalDateTime> allStartTimes = FXCollections.observableArrayList();
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static String divisionAppointment;
    private static int contactID;
    private static ObservableList<LocalDateTime> validStartTimes = FXCollections.observableArrayList();
    private static ObservableList<LocalDateTime> validEndTimes = FXCollections.observableArrayList();

    /**
     * query returns a list of all appointments in database
     *
     * @return
     */
    public static ObservableList<Appointment> getAllAppointments() {

        try {
            allAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                    + "FROM client_schedule.appointments AS a "
                    + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                    + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6).toLocalDateTime(),
                        resultSet.getTimestamp(7).toLocalDateTime(),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getString("Division"),
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * query adds input appointments to the database
     *
     * @param appointment
     * @return
     */
    public static boolean addAppointment(Appointment appointment) {

            try {
                String sql =
                        "INSERT INTO appointments (Title, Description, Location, Type, Start, End , Create_Date, " +
                                                  "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES(?,?,?,?,?,?,NOW(),'User',NOW(),'User',?,?,?)";
                PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
                int x = 1;
                preparedStatement.setString(x++,appointment.getTitle());
                preparedStatement.setString(x++,appointment.getDescription());
                preparedStatement.setString(x++,appointment.getLocation());
                preparedStatement.setString(x++,appointment.getType());
                preparedStatement.setTimestamp(x++, Timestamp.valueOf(appointment.getStart()));
                preparedStatement.setTimestamp(x++, Timestamp.valueOf(appointment.getEnd()));
                preparedStatement.setInt(x++, appointment.getCustomerID());
                preparedStatement.setInt(x++, appointment.getUserID());
                preparedStatement.setInt(x++, appointment.getContactID());

                preparedStatement.executeUpdate();
                preparedStatement.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    /**
     * query updates appointments in the database
     *
     * @param appointment
     * @return
     */
    public static boolean modifyAppointment(Appointment appointment) {
        try {
            String sql = "UPDATE client_schedule.appointments SET Create_Date=NOW(),Created_By='User',Last_Update=NOW(),Last_Updated_By='User', Title = ?,Description = ?,Location = ?,Type = ?,Start = ?,END = ?,Customer_ID = ?,User_ID = ?,Contact_ID = ? WHERE Appointment_ID = ?;";
            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            int x = 1;
            statement.setString(x++,appointment.getTitle());
            statement.setString(x++,appointment.getDescription());
            statement.setString(x++,appointment.getLocation());
            statement.setString(x++,appointment.getType());
            statement.setTimestamp(x++, Timestamp.valueOf(appointment.getStart()));
            statement.setTimestamp(x++, Timestamp.valueOf(appointment.getEnd()));
            statement.setInt(x++, appointment.getCustomerID());
            statement.setInt(x++, appointment.getUserID());
            statement.setInt(x++, appointment.getContactID());
            statement.setInt(x++, appointment.getAppointmentID());

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * query returns the latest appointment ID in order to generate an ID in the UI and insert it later
     *
     * @return
     */
    public static int getId() {
        try {
            int lastID = 0;
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT MAX(Appointment_ID) FROM client_schedule.appointments;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt(1) > lastID) {
                    lastID = resultSet.getInt(1);
                } else {
                    //do nothing if the id is less than last id
                }
            }
            lastID++;

            return lastID;
        } catch (SQLException sqlException) {
            return -1;
        }
    }

    /**
     * soft deletes an appointment from the db - sets all values to NULL except a few
     *
     * @param appointment
     * @return
     */
    public static boolean deleteAppointment(Appointment appointment) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?;");
            statement.setInt(1, appointment.getAppointmentID());
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * soft deletes all appointments related to a given customer
     *
     * @param customerId
     */
    public static void deleteAppointment(int customerId) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement("DELETE FROM appointments WHERE Customer_ID = ?;");
            statement.setInt(1, customerId);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * gets all appointments in the upcoming week
     *
     * @return
     */
    public static ObservableList<Appointment> getWeeklyAppointments() {
        try {
            weeklyAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Division"),
                        Contacts.getContactName(resultSet.getInt("Contact_ID")));

                LocalDateTime localDateTime = appointment.getStart();

                ZonedDateTime nowTimeEST = ZonedDateTime.now(ZoneId.of("America/New_York"));
                LocalDateTime est = nowTimeEST.toLocalDateTime();

                if (localDateTime.isBefore(est.plusDays(7)) && localDateTime.isAfter(est)) {

                    weeklyAppointments.add(appointment);
                } else {
                    //does nothing with the appointment if it is past 7 days out
                }
            }
            statement.close();
            return weeklyAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets all appointments in the upcoming month
     *
     * @return
     */
    public static ObservableList<Appointment> getMonthlyAppointments() {
        try {
            monthlyAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                    + "FROM client_schedule.appointments AS a "
                    + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                    + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Division"),
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                LocalDateTime localDateTime = appointment.getStart();
                LocalDateTime nowTime = LocalDateTime.now();
                ZonedDateTime nowTimeEST = nowTime.atZone(ZoneId.of("America/New_York"));
                LocalDateTime est = nowTimeEST.toLocalDateTime();

                if (localDateTime.isBefore(est.plusDays(30)) && localDateTime.isAfter(est)) {
                    monthlyAppointments.add(appointment);
                } else {
                    //does nothing with the appointment if it is past 30 days out
                }
            }
            statement.close();
            return monthlyAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets all unique appointments from the db
     *
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getTypes() throws SQLException {
        allTypes.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT DISTINCT Type FROM client_schedule.appointments;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                allTypes.add(resultSet.getString(1));
            }
            return allTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets a count of all appointments within a given month and appointment type
     *
     * @param localDateTime
     * @param type
     * @return
     */
    public static int getMonthTypeNumber(LocalDateTime localDateTime, String type) {
        int returnNumber = 0;
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                LocalDateTime YearAndMonth = resultSet.getTimestamp("Start").toLocalDateTime();
                String appointmentType = resultSet.getString("Type");
                if ((localDateTime.getYear() == YearAndMonth.getYear()) && (localDateTime.getMonth() == YearAndMonth.getMonth()) && (type.equals(appointmentType))) {
                    returnNumber++;
                } else {
                    // do nothing if the types are not the same
                }
            }
            return returnNumber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * gets all appointments related to a given contact
     *
     * @param contact
     * @return
     */
    public static ObservableList<Appointment> getContactAppointments(String contact) {
        contactID = Contacts.getContactID(contact);
        try {
            contactAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division AS Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID "
                         + "WHERE Contact_ID=" + contactID
                         + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6).toLocalDateTime(),
                        resultSet.getTimestamp(7).toLocalDateTime(),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getString("Division"),
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                contactAppointments.add(appointment);
            }
            statement.close();
            return contactAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * checks to see if a customer has any appointments scheduled
     *
     * @param selectedCustomer
     * @return
     */
    public static boolean checkForAppointments(Customer selectedCustomer) {
        int customerID = selectedCustomer.getId();
        customerAppointmentExists.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments WHERE Customer_ID=" + customerID + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                customerAppointmentExists.add(resultSet.getString(2));
            }
            if (customerAppointmentExists.isEmpty()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * returns a list of appointments for a given division and contact
     *
     * @param selectedDivision
     * @param contact
     * @return
     */
    public static ObservableList<Appointment> getDivisionAppointmentsList(String selectedDivision, String contact) {
        contactID = Contacts.getContactID(contact);

        try {
            appointmentsInDivision.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division AS Division "
                    + "FROM client_schedule.appointments AS a "
                    + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                    + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID "
                    + "WHERE Contact_ID=" + contactID
                    + " AND Title IS NOT NULL"
                    + " AND Division = '" + selectedDivision + "' ;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6).toLocalDateTime(),
                        resultSet.getTimestamp(7).toLocalDateTime(),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getString("Division"),
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                appointmentsInDivision.add(appointment);
            }
            statement.close();
            return appointmentsInDivision;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets all appointments and the division they are related to
     *
     * @param appointmentId
     * @return
     */
    public static String getDivisionsAppointments(int appointmentId) {
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT fld.Division "
                    + "FROM client_schedule.appointments AS a "
                    + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                    + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID "
                    + "WHERE a.Appointment_ID = " + appointmentId
                    + " AND a.Title IS NOT NULL; ";
            ;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                divisionAppointment = resultSet.getString(1);
            }
            return divisionAppointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * gets the soonest appointment in the next 15 minutes
     *
     * @return
     */
    public static Appointment get15MinAppt(LocalDateTime now) {

        try {
            String sql = "SELECT * FROM client_schedule.appointments AS a WHERE Start>=? AND Start<=? ORDER BY Start ASC LIMIT 1; ";
            preparedStatement = JDBC.getConnection().prepareStatement(sql);

            int x = 1;
            preparedStatement.setTimestamp(x++,Timestamp.valueOf(now));
            preparedStatement.setTimestamp(x++,Timestamp.valueOf(now.plusMinutes(15)));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (preparedStatement.getResultSet() == null) {
                return null;
            }

            if (resultSet.next()) {
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6).toLocalDateTime(),
                        resultSet.getTimestamp(7).toLocalDateTime(),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        "",
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                return appointment;
            } else {
                statement.close();
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets all taken start times from the db by date and converts those times from UTC to times on the user's system
     * @param localDate
     * @return
     */
    public static ObservableList<Appointment> getAllTakenAppointmentsByDate(LocalDate localDate) {
        ObservableList<Appointment> allTakenStartTimes = FXCollections.observableArrayList();
        allStartTimes.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.appointments WHERE dayofmonth(start) = dayofmonth('" + localDate + "');";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        "",
                        Contacts.getContactName(resultSet.getInt("Customer_ID")));
                allTakenStartTimes.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return allTakenStartTimes;

    }

    /**
     * accepts dates and returns a list of valid start times
     * @param userPickedDate
     * @return
     */
    public static ObservableList<LocalDateTime> getAllValidStartTimes(LocalDate userPickedDate) {
        validStartTimes.clear();
        ObservableList<LocalDateTime> possibleApptTimes = TimeHelper.getAvailableStartTimes(userPickedDate);
        validStartTimes = possibleApptTimes;
        ObservableList<Appointment> allTakenStartTimes = getAllTakenAppointmentsByDate(userPickedDate);
        ObservableList<Appointment> allTakenEndTimes = getAllTakenAppointmentsByDate(userPickedDate);

        int allTakenStartTimesCount = allTakenStartTimes.size() - 1;
        int x = 0;
        while (x <= allTakenStartTimesCount){
            int y = validStartTimes.size() - 1;
            LocalTime takenStartTime = allTakenStartTimes.get(x).getStartTime();
            LocalTime takenEndTime = allTakenEndTimes.get(x).getEndTime();
            while (y  >= 0){
                LocalTime possibleStartTime = possibleApptTimes.get(y).toLocalTime();
                LocalTime possibleEndTime = possibleStartTime.plusMinutes(30);
                if (possibleStartTime.isBefore(takenEndTime) || possibleStartTime.equals(takenEndTime)){
                    if ((possibleStartTime.isBefore(takenStartTime) || possibleStartTime.equals(takenStartTime)) && possibleEndTime.isAfter(takenStartTime)) {
                        validStartTimes.remove(y);
                    }
                    else if ((possibleStartTime.isAfter(takenStartTime) || possibleStartTime.equals(takenStartTime)) && possibleStartTime.isBefore(takenEndTime)) {
                        validStartTimes.remove(y);
                    }
                    --y;
                }
                else {
                    --y;
                }
            }
            ++x;
        }
        return validStartTimes;
    }

    /**
     * accepts dates and times and returns a list of valid end times
     * @param userPickedDate
     * @param userPickedStartTime
     * @return
     */
    public static ObservableList<LocalDateTime> getAllValidEndTimes(LocalDate userPickedDate, LocalTime userPickedStartTime) {
        validEndTimes.clear();
        LocalDateTime userPickedDateTime = LocalDateTime.of(userPickedDate, userPickedStartTime);
        LocalDateTime possibleEndDateTime = userPickedDateTime.plusMinutes(30);

        ObservableList<LocalDateTime> possibleApptTimes = TimeHelper.getAvailableEndTimes(possibleEndDateTime, userPickedStartTime);
        validEndTimes = possibleApptTimes;
        ObservableList<Appointment> allTakenStartTimes = getAllTakenAppointmentsByDate(userPickedDateTime.toLocalDate());
        ObservableList<Appointment> allTakenEndTimes = getAllTakenAppointmentsByDate(userPickedDateTime.toLocalDate());
        int allTakenEndTimesCount = allTakenEndTimes.size() - 1;
        int x = 0;
        while (x <= allTakenEndTimesCount){
            int y = 0;
            int z = validEndTimes.size() - 1;
            while (z  >= y){
                if (userPickedStartTime.isBefore(allTakenEndTimes.get(x).getEndTime()) || userPickedStartTime.equals(allTakenEndTimes.get(x).getEndTime())){
                    if (validEndTimes.get(z).equals(allTakenEndTimes.get(x)) || validEndTimes.get(z).toLocalTime().isAfter(allTakenEndTimes.get(x).getEndTime())
                      || validEndTimes.get(z).toLocalTime().isAfter(allTakenStartTimes.get(x).getStartTime())){
                        validEndTimes.remove(z);
                    }
                    --z;
                    possibleEndDateTime = possibleEndDateTime.plusMinutes(30);
                }
                else {
                    --z;
                    possibleEndDateTime = possibleEndDateTime.plusMinutes(30);
                }
            }
            ++x;
        }
        return validEndTimes;
    }
}
