/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eunice
 */

public class DataProvider {

  public DataProvider() {}

  public static ObservableList<Customer> allCustomersTableList = FXCollections.observableArrayList();

  public static ObservableList<Customer> getAllCustomers() throws SQLException {
    Statement statement = DBConnection.getConnection().createStatement();

    String customerQuery =
      "SELECT customer.customerId, customer.customerName, address.address, address.phone, address.postalCode, city.city, country.country " +
      "FROM customer " +
      "INNER JOIN address ON customer.addressId = address.addressId " +
      "INNER JOIN city ON address.cityId = city.cityId " +
      "INNER JOIN country ON city.countryId = country.countryId";

    ResultSet customerResults = statement.executeQuery(customerQuery);

    while (customerResults.next()) {
      Customer customer = new Customer(
        customerResults.getInt("customerId"),
        customerResults.getString("customerName"),
        customerResults.getString("address"),
        customerResults.getString("city"),
        customerResults.getString("country"),
        customerResults.getString("postalCode"),
        customerResults.getString("phone")
      );
      DataProvider.allCustomersTableList.add(customer);
    }

    return DataProvider.allCustomersTableList;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAllAppointments()
    throws SQLException {
    Statement statement = DBConnection.getConnection().createStatement();
    String appointmentQuery =
      "SELECT customer.customerName, appointment.appointmentId, customer.customerId, user.userId, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end " +
      "FROM user " +
      "INNER JOIN appointment ON user.userId = appointment.userId " +
      "INNER JOIN customer ON appointment.customerId = customer.customerId";

    ResultSet appointmentResults = statement.executeQuery(appointmentQuery);

    while (appointmentResults.next()) {
      Appointment appointment = new Appointment(
        appointmentResults.getString("customerName"),
        appointmentResults.getInt("appointmentId"),
        appointmentResults.getInt("customerId"),
        appointmentResults.getInt("userId"),
        appointmentResults.getString("title"),
        appointmentResults.getString("description"),
        appointmentResults.getString("location"),
        appointmentResults.getString("contact"),
        appointmentResults.getString("type"),
        appointmentResults.getString("url"),
        appointmentResults.getString("start"),
        appointmentResults.getString("end")
      );

      DataProvider.allAppointmentsTableList.add(appointment);
    }

    return DataProvider.allAppointmentsTableList;
  }

  /* -------------------------------------------------------------- */
  private static ObservableList<Appointment> selectedAppointmentsForCustomer = FXCollections.observableArrayList();
  private static ObservableList<Appointment> allAppointmentsTableList = FXCollections.observableArrayList();
  private static ObservableList<Appointment> appointmentsByWeek = FXCollections.observableArrayList();
  private static ObservableList<Appointment> appointmentsByMonth = FXCollections.observableArrayList();

  /* -------------------------------------------------------------- */
  public static void addCustomer(Customer customer) {
    allCustomersTableList.add(customer);
  }

  /* -------------------------------------------------------------- */
  public static void addAppointment(Appointment appointment) {
    allAppointmentsTableList.add(appointment);
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Customer> getAllCustomersTableList() {
    return allCustomersTableList;
  }

  /* -------------------------------------------------------------- */
  public static void populateCustomerTable() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ObservableList<Customer> allCustomers = DataProvider.getAllCustomersTableList();

      ResultSet selectCustomerID = statement.executeQuery(
        "SELECT customerId FROM customer WHERE active = 1"
      );

      ArrayList<Integer> activeCustomerID = new ArrayList<>();

      while (selectCustomerID.next()) {
        activeCustomerID.add(selectCustomerID.getInt(1));
      }

      for (int customerIDLoop : activeCustomerID) {
        Customer customer = new Customer();
        ResultSet customerTableData = statement.executeQuery(
          "SELECT customerId, customerName, addressId FROM customer WHERE customerId = '" +
          customerIDLoop +
          "'"
        );
        customerTableData.next();

        int customerID = customerTableData.getInt("customerId");
        int addressID = customerTableData.getInt("addressId");
        String customerName = customerTableData.getString("customerName");

        ResultSet addressTableData = statement.executeQuery(
          "SELECT address, cityId, postalCode, phone FROM address WHERE addressId = '" +
          addressID +
          "'"
        );
        addressTableData.next();

        String address = addressTableData.getString("address");
        String cityId = addressTableData.getString("cityId");
        String postalCode = addressTableData.getString("postalCode");
        String phone = addressTableData.getString("phone");

        int cityID = addressTableData.getInt("cityId");

        ResultSet cityTableData = statement.executeQuery(
          "SELECT city, countryId FROM city WHERE cityId = '" + cityID + "'"
        );
        cityTableData.next();

        String city = cityTableData.getString("city");
        int countryID = cityTableData.getInt("countryId");

        ResultSet countryTableData = statement.executeQuery(
          "SELECT country FROM country WHERE countryId = '" + countryID + "'"
        );
        countryTableData.next();

        String country = countryTableData.getString("country");

        customer.setCustomerID(customerID);
        customer.setCustomerName(customerName);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setCountry(country);
        customer.setPostalCode(postalCode);
        customer.setPhone(phone);
        allCustomers.add(customer);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static void populateAppointmentTable() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ObservableList<Appointment> allAppointments = DataProvider.getAllAppointmentsTableList();

      ResultSet selectAppointment = statement.executeQuery(
        "SELECT appointmentId FROM appointment"
      );

      ArrayList<Integer> appointmentIDArray = new ArrayList<>();

      while (selectAppointment.next()) {
        appointmentIDArray.add(selectAppointment.getInt(1));
      }

      for (int appointmentIDLoop : appointmentIDArray) {
        Appointment appointment = new Appointment();
        ResultSet appointmentTableData = statement.executeQuery(
          "SELECT customer.customerName, appointmentId, appointment.customerId, userId, title, description, location, contact, type, url, start, end " +
          "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
          "WHERE appointmentId = " +
          appointmentIDLoop
        );
        appointmentTableData.next();

        int appointmentId = appointmentTableData.getInt("appointmentId");
        int customerId = appointmentTableData.getInt("customerId");
        String customerName = appointmentTableData.getString("customerName");
        int userId = appointmentTableData.getInt("userId");
        String title = appointmentTableData.getString("title");
        String description = appointmentTableData.getString("description");
        String location = appointmentTableData.getString("location");
        String contact = appointmentTableData.getString("contact");
        String type = appointmentTableData.getString("type");
        String url = appointmentTableData.getString("url");
        String start = appointmentTableData.getString("start");
        String end = appointmentTableData.getString("end");

        start = start.substring(0, start.length() - 2);
        end = end.substring(0, end.length() - 2);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId UTCZoneID = ZoneId.of("UTC");

        LocalDateTime startDateTime = LocalDateTime.parse(start, format);
        LocalDateTime endDateTime = LocalDateTime.parse(end, format);

        ZonedDateTime zonedStartLocal = startDateTime.atZone(UTCZoneID);
        ZonedDateTime zonedEndLocal = endDateTime.atZone(UTCZoneID);

        ZonedDateTime convertedStartTime = zonedStartLocal.withZoneSameInstant(
          localZoneId
        );
        ZonedDateTime convertedEndTime = zonedEndLocal.withZoneSameInstant(
          localZoneId
        );

        LocalDateTime startFinalTime = convertedStartTime.toLocalDateTime();
        LocalDateTime endFinalTime = convertedEndTime.toLocalDateTime();

        String adjustedStart = startFinalTime.toString();
        String adjustedEnd = endFinalTime.toString();

        String finalStartTime = adjustedStart.replace("T", " ");
        String finalEndTime = adjustedEnd.replace("T", " ");

        appointment.setAppointmentId(appointmentId);
        appointment.setCustomerId(customerId);
        appointment.setCustomerName(customerName);
        appointment.setUserId(userId);
        appointment.setTitle(title);
        appointment.setDescription(description);
        appointment.setLocation(location);
        appointment.setContact(contact);
        appointment.setType(type);
        appointment.setUrl(url);
        appointment.setStart(finalStartTime);
        appointment.setEnd(finalEndTime);
        allAppointments.add(appointment);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static int nextAppointmentId()
    throws ClassNotFoundException, SQLException {
    Statement statement = DBConnection.getConnection().createStatement();

    ResultSet selectAppointment = statement.executeQuery(
      "SELECT appointmentId FROM appointment"
    );

    int max = 0;

    while (selectAppointment.next()) {
      max = selectAppointment.getInt("Auto_increment");
    }

    return max;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getSelectedAppointmentsForCustomer() {
    return selectedAppointmentsForCustomer;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAllAppointmentsTableList() {
    return allAppointmentsTableList;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAppointmentsByWeek() {
    return appointmentsByWeek;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAppointmentsByMonth() {
    return appointmentsByMonth;
  }

  /* -------------------------------------------------------------- */
  public static void setSelectedAppointmentsForCustomer(int selectedCustomer) {
    try {
      ArrayList<Integer> selectedCustomerAppointments = new ArrayList<>();

      Statement statement = DBConnection.getConnection().createStatement();
      ResultSet selectedAppointments = statement.executeQuery(
        "SELECT * FROM appointment WHERE customerId =" + selectedCustomer
      );

      while (selectedAppointments.next()) {
        selectedCustomerAppointments.add(selectedAppointments.getInt(1));
      }

      getSelectedAppointmentsForCustomer().clear();

      ResultSet associatedAppointments = statement.executeQuery(
        "SELECT customerId, appointmentId, location, start FROM appointment WHERE customerId =" +
        selectedCustomer
      );

      while (associatedAppointments.next()) {
        int customerId = associatedAppointments.getInt(1);
        int appointmentId = associatedAppointments.getInt(2);
        String location = associatedAppointments.getString(3);
        String start = associatedAppointments.getString(4);

        start = start.substring(0, start.length() - 2);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId UTCZoneID = ZoneId.of("UTC");

        LocalDateTime startDateTime = LocalDateTime.parse(start, format);

        ZonedDateTime zonedStartLocal = startDateTime.atZone(UTCZoneID);

        ZonedDateTime convertedStartTime = zonedStartLocal.withZoneSameInstant(
          localZoneId
        );

        LocalDateTime startFinalTime = convertedStartTime.toLocalDateTime();

        String adjustedStart = startFinalTime.toString();

        String url = adjustedStart.replace("T", " ");

        Appointment appointment = new Appointment();

        appointment.setCustomerId(customerId);
        appointment.setAppointmentId(appointmentId);
        appointment.setLocation(location);
        appointment.setStart(start);
        appointment.setUrl(url);
        selectedAppointmentsForCustomer.add(appointment);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static void setWeeklyView() {
    // weekForReference
    try {
      ArrayList<Integer> selectedAppointmentsByWeek = new ArrayList<>();

      Statement statement = DBConnection.getConnection().createStatement();

      String query = 
      "SELECT appointmentId " +
      "FROM appointment " +
      "WHERE YEARWEEK(start, 1) = YEARWEEK(CURDATE(), 1) " +
      "ORDER BY start ASC";

      ResultSet weeklyAppointments = statement.executeQuery(
        query
        // "SELECT appointmentId FROM appointment WHERE year(start) = year(date_add(curdate(), interval " +
        // weekForReference +
        // " WEEK)) AND weekofyear(start) = weekofyear(date_add(curdate(),interval " +
        // weekForReference +
        // " WEEK));"
      );

      while (weeklyAppointments.next()) {
        selectedAppointmentsByWeek.add(weeklyAppointments.getInt(1));
      }

      for (int appointmentId : selectedAppointmentsByWeek) {
        ResultSet selectAppointment = statement.executeQuery(
          "SELECT customer.customerName, customer.customerId, contact, title, type, location, description, start, end FROM appointment JOIN customer ON customer.customerId = appointment.customerId WHERE appointmentId =" +
          appointmentId
        );

        selectAppointment.next();

        Appointment appointment = new Appointment();

        String customerName = selectAppointment.getString(1);
        int customerId = selectAppointment.getInt(2);
        String contact = selectAppointment.getString(3);
        String title = selectAppointment.getString(4);
        String type = selectAppointment.getString(5);
        String location = selectAppointment.getString(6);
        String description = selectAppointment.getString(7);
        String start = selectAppointment.getString(8);
        String end = selectAppointment.getString(9);

        appointment.setCustomerName(customerName);
        appointment.setContact(contact);
        appointment.setTitle(title);
        appointment.setType(type);
        appointment.setLocation(location);
        appointment.setDescription(description);
        appointment.setStart(start);
        appointment.setEnd(end);
        appointmentsByWeek.add(appointment);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static void setMonthlyView() {
  // public static void setMonthlyView(String monthForReference) {
    ArrayList<Integer> selectedAppointmentsByMonth = new ArrayList<>();

    try {
      Statement statement = DBConnection.getConnection().createStatement();

      String query = 
      "SELECT appointmentId " +
      "FROM appointment " +
      "WHERE start >= LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH " +
      "AND start < LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY " +
      "ORDER BY start ASC";

      ResultSet monthlyAppointments = statement.executeQuery(
        query
        // "SELECT appointmentId FROM appointment WHERE monthname(start) = '" +
        // monthForReference +
        // "'"
      );

      while (monthlyAppointments.next()) {
        selectedAppointmentsByMonth.add(monthlyAppointments.getInt(1));
      }

      for (int appointmentId : selectedAppointmentsByMonth) {
        ResultSet selectAppointment = statement.executeQuery(
          "SELECT customer.customerName, customer.customerId, contact, title, type, location, description, start, end FROM appointment JOIN customer ON customer.customerId = appointment.customerId WHERE appointmentId =" +
          appointmentId
        );

        selectAppointment.next();
        Appointment appointment = new Appointment();

        String customerName = selectAppointment.getString(1);
        int customerId = selectAppointment.getInt(2);
        String contact = selectAppointment.getString(3);
        String title = selectAppointment.getString(4);
        String type = selectAppointment.getString(5);
        String location = selectAppointment.getString(6);
        String description = selectAppointment.getString(7);
        String start = selectAppointment.getString(8);
        String end = selectAppointment.getString(9);

        appointment.setCustomerName(customerName);
        appointment.setContact(contact);
        appointment.setTitle(title);
        appointment.setType(type);
        appointment.setLocation(location);
        appointment.setDescription(description);
        appointment.setStart(start);
        appointment.setEnd(end);

        appointmentsByMonth.add(appointment);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  
  /* -------------------------------------------------------------- */

}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
MySQL Select Date Equal to Today
https://stackoverflow.com/questions/12677707/mysql-select-date-equal-to-today

mysql select dates in 30-day range
https://stackoverflow.com/questions/12098899/mysql-select-dates-in-30-day-range

MySQL DATE_ADD() Function
https://www.w3schools.com/sql/func_mysql_date_add.asp

MySQL DATE_FORMAT() Function
https://www.w3schools.com/sql/func_mysql_date_format.asp

--------------------------------------------------------------------
"SELECT appointmentId, date_format(start, '%Y-%m-%d') " +
      "FROM appointment " +
      "WHERE (year(start) = YEAR(curdate()) AND weekofyear(start) = weekofyear(date_add(curdate(),interval 7 day))) " +
      "OR (start = curdate())";

      "SELECT appointmentId " +
      "FROM appointment " +
      "WHERE start >= NOW() AND start < NOW() + INTERVAL 30 DAY " +
      "ORDER BY start ASC";

--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
