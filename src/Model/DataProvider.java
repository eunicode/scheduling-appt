/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Utilities.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eunice
 */

public class DataProvider {

  // Constructor
  public DataProvider() {}

  // Customer ObservableList
  public static ObservableList<Customer> customersAllList = FXCollections.observableArrayList();

  // Appointment ObservableLists
  private static final ObservableList<Appointment> appointmentsAllList = FXCollections.observableArrayList();
  private static final ObservableList<Appointment> appointmentsWeekList = FXCollections.observableArrayList();
  private static final ObservableList<Appointment> appointmentsMonthList = FXCollections.observableArrayList();

  /* -------------------------------------------------------------- */
  public static void addCustomer(Customer customer) {
    customersAllList.add(customer);
  }

  /* -------------------------------------------------------------- */
  public static void addAppointment(Appointment appointment) {
    appointmentsAllList.add(appointment);
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Customer> getCustomersAllList() {
    return customersAllList;
  }

  /* -------------------------------------------------------------- */
  public static void createCustomerObjectObservableList() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      // Array of customer objects - set two observablelists equal to each other, export
      ObservableList<Customer> customerObjectList = DataProvider.getCustomersAllList();

      ResultSet customerIdRS = statement.executeQuery(
        "SELECT customerId FROM customer"
      );

      // Create array of customer IDs
      ArrayList<Integer> customerIdArray = new ArrayList<>();

      // Add customerIDs to customer ArrayList
      while (customerIdRS.next()) {
        customerIdArray.add(customerIdRS.getInt(1));
      }

      // Iterate all customers
      for (int custID : customerIdArray) {
        // Create customer object
        Customer customer = new Customer();

        // Create customer resultset
        ResultSet customerRS = statement.executeQuery(
          "SELECT customerId, customerName, addressId FROM customer WHERE customerId = '" +
          custID +
          "'"
        );

        customerRS.next();

        // Get data from customer resultset
        int customerID = customerRS.getInt("customerId");
        int addressID = customerRS.getInt("addressId");
        String customerName = customerRS.getString("customerName");

        // Create address resultset
        ResultSet addressRS = statement.executeQuery(
          "SELECT address, cityId, postalCode, phone FROM address WHERE addressId = '" +
          addressID +
          "'"
        );

        addressRS.next();

        // Get data from address resultset
        String address = addressRS.getString("address");
        String postalCode = addressRS.getString("postalCode");
        String phone = addressRS.getString("phone");
        int cityID = addressRS.getInt("cityId");

        // Create city resultset
        ResultSet cityRS = statement.executeQuery(
          "SELECT city, countryId FROM city WHERE cityId = '" + cityID + "'"
        );

        // Get data from city resultset
        cityRS.next();
        String city = cityRS.getString("city");
        int countryID = cityRS.getInt("countryId");

        // Create country resultset
        ResultSet countryRS = statement.executeQuery(
          "SELECT country FROM country WHERE countryId = '" + countryID + "'"
        );
        countryRS.next();

        // Get data from country result set
        String country = cityRS.getString("country");

        // Set properties for customer object
        customer.setCustomerID(customerID);
        customer.setCustomerName(customerName);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setCountry(country);
        customer.setPostalCode(postalCode);
        customer.setPhone(phone);

        // Add customer object to customer observablelist
        customerObjectList.add(customer);
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static void createAppointmentObjectObservableList() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ObservableList<Appointment> appointmentObjectList = DataProvider.getAppointmentsAllList();

      ResultSet appointmentIdRS = statement.executeQuery(
        "SELECT appointmentId FROM appointment"
      );

      ArrayList<Integer> appointmentIdArray = new ArrayList<>();

      while (appointmentIdRS.next()) {
        appointmentIdArray.add(appointmentIdRS.getInt(1));
      }

      for (int apptID : appointmentIdArray) {
        Appointment appointment = new Appointment();

        ResultSet appointmentTableData = statement.executeQuery(
          "SELECT customer.customerName, appointmentId, appointment.customerId, userId, title, description, location, contact, type, url, start, end " +
          "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
          "WHERE appointmentId = " +
          apptID
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

        // Trim time strings
        start = start.substring(0, start.length() - 2);
        end = end.substring(0, end.length() - 2);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );

        ZoneId zoneIdLocal = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId UTCZoneID = ZoneId.of("UTC");

        LocalDateTime startLocalTimeF = LocalDateTime.parse(start, format);
        LocalDateTime endLocalTimeF = LocalDateTime.parse(end, format);

        ZonedDateTime zonedStartLocal = startLocalTimeF.atZone(UTCZoneID);
        ZonedDateTime zonedEndLocal = endLocalTimeF.atZone(UTCZoneID);

        ZonedDateTime convertedStartTime = zonedStartLocal.withZoneSameInstant(
          zoneIdLocal
        );

        ZonedDateTime convertedEndTime = zonedEndLocal.withZoneSameInstant(
          zoneIdLocal
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

        appointmentObjectList.add(appointment);
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAppointmentsAllList() {
    return appointmentsAllList;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAppointmentsWeek() {
    return appointmentsWeekList;
  }

  /* -------------------------------------------------------------- */
  public static ObservableList<Appointment> getAppointmentsMonth() {
    return appointmentsMonthList;
  }

  /* -------------------------------------------------------------- */
  public static void createAppointmentWeekList() {
    try {
      // Create array for appointment IDs
      ArrayList<Integer> appointmentsWeekArray = new ArrayList<>();

      Statement statement = DBConnection.getConnection().createStatement();

      String query =
        "SELECT appointmentId " +
        "FROM appointment " +
        "WHERE YEARWEEK(start, 1) = YEARWEEK(CURDATE(), 1) " +
        "ORDER BY start ASC";

      ResultSet appointmentsWeekRS = statement.executeQuery(query);

      // Add appointment IDs to array
      while (appointmentsWeekRS.next()) {
        appointmentsWeekArray.add(appointmentsWeekRS.getInt(1));
      }

      // Iterate appointment ID array
      for (int appointmentId : appointmentsWeekArray) {
        // Get customer data associated with appointment ID
        ResultSet selectAppointment = statement.executeQuery(
          "SELECT customer.customerName, customer.customerId, contact, title, type, location, description, start, end " +
          "FROM appointment " +
          "INNER JOIN customer " +
          "ON customer.customerId = appointment.customerId " +
          "WHERE appointmentId =" +
          appointmentId
        );

        selectAppointment.next();

        // Create appointment object
        Appointment appointment = new Appointment();

        // Get data from ResultSet
        String customerName = selectAppointment.getString(1);
        int customerId = selectAppointment.getInt(2);
        String contact = selectAppointment.getString(3);
        String title = selectAppointment.getString(4);
        String type = selectAppointment.getString(5);
        String location = selectAppointment.getString(6);
        String description = selectAppointment.getString(7);
        String start = selectAppointment.getString(8);
        String end = selectAppointment.getString(9);

        // Set appointment object properties with gleaned data
        appointment.setCustomerName(customerName);
        appointment.setContact(contact);
        appointment.setTitle(title);
        appointment.setType(type);
        appointment.setLocation(location);
        appointment.setDescription(description);
        appointment.setStart(start);
        appointment.setEnd(end);

        // Add appointment object to outer appointment ObservableList
        appointmentsWeekList.add(appointment);
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  public static void createAppointmentMonthList() {
    ArrayList<Integer> appointmentIdArray = new ArrayList<>();

    try {
      Statement statement = DBConnection.getConnection().createStatement();

      String query =
        "SELECT appointmentId " +
        "FROM appointment " +
        "WHERE start >= LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY - INTERVAL 1 MONTH " +
        "AND start < LAST_DAY(CURRENT_DATE) + INTERVAL 1 DAY " +
        "ORDER BY start ASC";

      ResultSet appointmentsMonthRS = statement.executeQuery(query);

      while (appointmentsMonthRS.next()) {
        appointmentIdArray.add(appointmentsMonthRS.getInt(1));
      }

      for (int appointmentId : appointmentIdArray) {
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

        appointmentsMonthList.add(appointment);
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
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
