package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.Division;
import utilities.JDBCConnectionHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Appointments {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> allTypes = FXCollections.observableArrayList();
    private static ObservableList<String> customerAppointmentExists = FXCollections.observableArrayList();
    private static ObservableList<Appointment> divisionAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsInDivision = FXCollections.observableArrayList();
    private static boolean valid = true;
    public static boolean conflict = false;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static int divisionCount;
    private static String divisionAppointment;
    private static int contactID;

    public static ObservableList<Appointment> getAllAppointments(){

        try{
            allAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Appointment appointment = new Appointment(resultSet.getInt(1),
                resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6),
                        resultSet.getTimestamp(7),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp(7).toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division"));
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean addAppointment(Appointment appointment){
        LocalDateTime possibleStart = appointment.getStart().toLocalDateTime();
        LocalDateTime possibleEnd = appointment.getEnd().toLocalDateTime();
        ZonedDateTime possibleStartZoned = possibleStart.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime possibleEndZoned = possibleEnd.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime appStart = possibleStartZoned.toLocalDateTime();
        LocalDateTime appEnd = possibleEndZoned.toLocalDateTime();
        valid = true;
        try{
            customerAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments WHERE customer_ID=" + appointment.getCustomerID() + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Appointment customerAppointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6),
                        resultSet.getTimestamp(7),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp(7).toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division"));
                customerAppointments.add(customerAppointment);
            }
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        while(valid){
            int index = 0;
            while (index < customerAppointments.size()){
                Appointment aptmt = customerAppointments.get(index);

                ZonedDateTime zonedDateTimeStart = aptmt.getStart().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime zonedDateTimeEnd = aptmt.getEnd().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
                LocalDateTime startTime = zonedDateTimeStart.toLocalDateTime();
                LocalDateTime endTime = zonedDateTimeEnd.toLocalDateTime();

                if(((appStart.isAfter(startTime) || appStart.isEqual(startTime))) && appEnd.isBefore(endTime)){
                    return false;
                }
                if(appEnd.isAfter(startTime)&& ((appEnd.isBefore(endTime) || appEnd.isEqual(endTime)))){
                    return false;
                }
                if(((appStart.isBefore(startTime)) || appStart.isEqual(startTime)) && ((appEnd.isAfter(endTime) || appEnd.isEqual(endTime)))){
                    return false;
                }else{
                    index++;
                }
            }
            try{
                PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement("INSERT INTO appointments VALUES(" + appointment.getAppointmentID() + ", '" +
                        appointment.getTitle() + "', '" + appointment.getDescription() + "', '" + appointment.getLocation() + "', '" + appointment.getType() + "', '" + Timestamp.valueOf(appStart)
                        + "', '" + Timestamp.valueOf(appEnd) + "', NOW(), 'User', NOW(), 'User', " + appointment.getCustomerID() + ", " + appointment.getUserID() + ", " + appointment.getContactID() + ");");
                preparedStatement.executeUpdate();
                preparedStatement.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean modifyAppointment(Appointment appointment){
        LocalDateTime possibleStart = appointment.getStart().toLocalDateTime();
        LocalDateTime possibleEnd = appointment.getEnd().toLocalDateTime();
        ZonedDateTime possibleStartZoned = possibleStart.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime possibleEndZoned = possibleEnd.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime testAppStart = possibleStartZoned.toLocalDateTime();
        LocalDateTime testAppEnd = possibleEndZoned.toLocalDateTime();
        valid = true;
        try{
            customerAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments WHERE customer_ID=" + appointment.getCustomerID() + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                conflict = false;
                Appointment customerAppointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6),
                        resultSet.getTimestamp(7),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp(7).toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division"));
                if(customerAppointment.getAppointmentID() == appointment.getAppointmentID()){
                    //does nothing with the appointment if it has the same id
                    conflict = true;
                }
                else if(!conflict){
                    customerAppointments.add(customerAppointment);
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (valid){
            int index = 0;
            while (index < customerAppointments.size()){
                Appointment app = customerAppointments.get(index);

                ZonedDateTime zonedDateTimeStart = app.getStart().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime zonedDateTimeEnd = app.getEnd().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
                LocalDateTime startTime = zonedDateTimeStart.toLocalDateTime();
                LocalDateTime endTime = zonedDateTimeEnd.toLocalDateTime();

                if(((testAppStart.isAfter(startTime) || testAppStart.isEqual(startTime))) && testAppStart.isBefore(endTime)){
                    return false;
                }
                if(testAppEnd.isAfter(startTime) && ((testAppEnd.isBefore(endTime) || testAppEnd.isEqual(endTime)))){
                    return false;
                }
                if(((testAppStart.isBefore(startTime)) || testAppStart.isEqual(startTime)) && ((testAppEnd.isAfter(endTime) || testAppEnd.isEqual(endTime)))){
                    return false;
                }
                else{
                    index++;
                }
            }
            try{
                PreparedStatement statement =JDBC.getConnection().prepareStatement("UPDATE client_schedule.appointments SET Title= '" + appointment.getTitle() + "" +
                        "', Description='" + appointment.getDescription() + "', Location='" + appointment.getLocation() + "', Type='" + appointment.getType() + "', Start='" + testAppStart + "', END='" +
                        testAppEnd + "', Create_Date=NOW(), Created_By='User', Last_Update=NOW(),  Last_Updated_By='USER', Customer_ID=" + appointment.getCustomerID() + ", User_ID=" + appointment.getUserID()
                        + ", Contact_ID=" + appointment.getContactID() + " WHERE Appointment_ID=" + appointment.getAppointmentID() + ";");
                statement.executeUpdate();
                statement.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getId() {
        try {
            int lastID = 0;
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Appointment_ID FROM client_schedule.appointments ORDER BY Appointment_ID;";
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

    public static boolean deleteAppointment(Appointment appointment){
        try{PreparedStatement statement =JDBC.getConnection().prepareStatement("UPDATE client_schedule.appointments SET Title=NULL, Description=NULL, Location=NULL, Type=NULL, Start=NULL, End=NULL," +
                " Create_Date=NULL, Created_By=NULL, Last_Update=NULL,  Last_Updated_By=NULL, Customer_ID=1, User_ID=1, Contact_ID=1 WHERE Appointment_ID=" + appointment.getAppointmentID() + ";");
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Appointment> getWeeklyAppointments(){
        try{
            weeklyAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString("Appointment_ID"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getTimestamp("Start").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("End").toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division"));
                LocalDateTime localDateTime = appointment.getStart().toLocalDateTime();
                LocalDateTime nowTime = LocalDateTime.now();
                ZonedDateTime nowTimeEST = nowTime.atZone(ZoneId.of("America/New_York"));
                LocalDateTime est = nowTimeEST.toLocalDateTime();
                if(localDateTime.isBefore(est.plusDays(7)) && localDateTime.isAfter(est)){
                    weeklyAppointments.add(appointment);
                }
                else {
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

    public static ObservableList<Appointment> getMonthlyAppointments(){
        try{
            monthlyAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString("Appointment_ID"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getTimestamp("Start").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("End").toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division"));
                LocalDateTime localDateTime = appointment.getStart().toLocalDateTime();
                LocalDateTime nowTime = LocalDateTime.now();
                ZonedDateTime nowTimeEST = nowTime.atZone(ZoneId.of("America/New_York"));
                LocalDateTime est = nowTimeEST.toLocalDateTime();
                if(localDateTime.isBefore(est.plusDays(30)) && localDateTime.isAfter(est)){
                    monthlyAppointments.add(appointment);
                }
                else {
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

    public static ObservableList<String> getTypes() throws SQLException {
        allTypes.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Type FROM client_schedule.appointments WHERE Title IS NOT NULL;";
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

    public static int getMonthTypeNumber(LocalDateTime localDateTime, String type){
        int returnNumber = 0;
        try{
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments WHERE Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                LocalDateTime YearAndMonth = resultSet.getTimestamp("Start").toLocalDateTime();
                String appointmentType = resultSet.getString("Type");
                if((localDateTime.getYear() == YearAndMonth.getYear()) && (localDateTime.getMonth() == YearAndMonth.getMonth()) && (type.equals(appointmentType))){
                    returnNumber++;
                }
                else {
                    // do nothing if the types are not the same
                }
            }
            return returnNumber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ObservableList<Appointment> getContactAppointments(String contact){
        contactID = Contacts.getContactID(contact);
        try{
            contactAppointments.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT a.*, fld.Division AS Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID "
                         + "WHERE Contact_ID=" + contactID
                         + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6),
                        resultSet.getTimestamp(7),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp(7).toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division")
                );
                contactAppointments.add(appointment);
            }
            statement.close();
            return contactAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkForAppointments(Customer selectedCustomer){
        int customerID = selectedCustomer.getId();
        customerAppointmentExists.clear();
        try{
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.appointments WHERE Customer_ID=" + customerID + " AND Title IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                customerAppointmentExists.add(resultSet.getString(2));
            }
            if(customerAppointmentExists.isEmpty()){
                return true;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Appointment> getDivisionAppointmentsList(String selectedDivision, String contact) {
        int i = 0;
        contactID = Contacts.getContactID(contact);

        try{
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
            while (resultSet.next()){
                Appointment appointment = new Appointment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6),
                        resultSet.getTimestamp(7),
                        resultSet.getInt(12),
                        resultSet.getInt(13),
                        resultSet.getInt(14),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp(6).toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp(7).toLocalDateTime().toLocalTime(),
                        resultSet.getString("Division")
                );
                appointmentsInDivision.add(appointment);
            }
            statement.close();
            return appointmentsInDivision;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDivisionsAppointments(int appointmentId){
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT fld.Division "
                         + "FROM client_schedule.appointments AS a "
                         + "INNER JOIN client_schedule.customers AS cust ON cust.Customer_ID = a.Customer_ID "
                         + "INNER JOIN client_schedule.first_level_divisions AS fld ON fld.Division_ID = cust.Division_ID "
                         + "WHERE a.Appointment_ID = " + appointmentId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                divisionAppointment = resultSet.getString(1);
            }
            return divisionAppointment;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static int getDivisionCount(){
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT COUNT(Division_ID) FROM client_schedule.first_level_divisions;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                divisionCount = resultSet.getInt(1);
            }
            return divisionCount;
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
}
