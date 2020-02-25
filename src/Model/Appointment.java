/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Database.DBConnection;
import View_Controller.LoginScreenController;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author eunice
 */

// Plain Old Java Object class
public class Appointment {
  // Class field names match `Appointment` table columns
  private int appointmentId;
  private int customerId;
  private int userId;
  private String title;
  private String description;
  private String location;
  private String contact;
  private String type;
  private String url;
  private LocalTime time;
  private String start;
  private String end;
  private String customerName;

  // Constructor
  public Appointment() {}

  // Constructor overload
  public Appointment( // 12 params
    String customerName,
    int appointmentId,
    int customerId,
    int userId,
    String title,
    String description,
    String location,
    String contact,
    String type,
    String url,
    String start,
    String end
  ) {
    customerName = this.customerName;
    appointmentId = this.appointmentId;
    customerId = this.customerId;
    userId = this.userId;
    title = this.title;
    description = this.description;
    location = this.location;
    contact = this.contact;
    type = this.type;
    url = this.url;
    start = this.start;
    end = this.end;
  }

  public Appointment( // 11 params - missing customerName
    int appointmentId,
    int customerId,
    int userId,
    String title,
    String description,
    String location,
    String contact,
    String type,
    String url,
    String start,
    String end
  ) {
    appointmentId = this.appointmentId;
    customerId = this.customerId;
    userId = this.userId;
    title = this.title;
    description = this.description;
    location = this.location;
    contact = this.contact;
    type = this.type;
    url = this.url;
    start = this.start;
    end = this.end;
  }

  /* -------------------------------------------------------------- */
  public int getAppointmentId() {
    return appointmentId;
  }

  public int getUserId() {
    return userId;
  }

  public int getCustomerId() {
    return customerId;
  }
  
  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public String getContact() {
    return contact;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }

  public LocalTime getTime() {
    return time;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }

  /* -------------------------------------------------------------- */
  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public void setContact(String contact) {
    this.contact = contact;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
  // public LocalDate getDate(String dateString) {
  //   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  //   LocalDate transformedDate = LocalDate.parse(dateString, formatter);
  //   return transformedDate;
  // }
  /* -------------------------------------------------------------- */  
  public static String pullCustomerName(int customerId) throws SQLException {
    Statement statement = DBConnection.getConnection().createStatement();
    Customer customer = new Customer();

    ResultSet customerTableData = statement.executeQuery(
      "SELECT customerName from customer WHERE customerId =" + customerId
    );

    customerTableData.next();

    String customerName = customerTableData.getString("customerName");

    customer.setCustomerName(customerName);

    return customerName;
  }

  /* -------------------------------------------------------------- */
  public void setAppointmentAlert() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet upcomingAppointments = statement.executeQuery(
        "SELECT customer.customerName, start FROM appointment JOIN customer ON customer.customerId = appointment.customerId WHERE DATE(start) = curdate()"
      );

      LocalTime currentTime = LocalTime.now();

      while (upcomingAppointments.next()) {
        String customerName = upcomingAppointments.getString("customerName");
        String startTime = upcomingAppointments.getString("start");

        DateTimeFormatter formateDateTime = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss.S"
        );

        LocalDateTime localFormattedTime = LocalDateTime.parse(
          startTime,
          formateDateTime
        );

        ZonedDateTime zonedFormattedTime = localFormattedTime.atZone(
          ZoneId.of("UTC")
        );

        ZoneId localZoneId = ZoneId.systemDefault();

        ZonedDateTime zonedUTCTime = zonedFormattedTime.withZoneSameInstant(
          localZoneId
        );
        DateTimeFormatter format = DateTimeFormatter.ofPattern("kk:mm");

        LocalTime localTime = LocalTime.parse(
          zonedUTCTime.toString().substring(11, 16),
          format
        );

        String appointmentTime = localTime.toString();

        long difference = ChronoUnit.MINUTES.between(currentTime, localTime);

        if (difference > 0 && difference <= 15) {
          // Add Korean alternative
          String alertMessage = String.format(
            "Reminder! You have an appointment coming up with %s at %s",
            customerName,
            appointmentTime
          );

          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Appointment Reminder");
          alert.setHeaderText("Upcoming appointment within 15 minutes!");
          alert.setContentText(alertMessage);
          alert.showAndWait();

          break;
        }
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
How to set javafx datepicker value correctly?
https://stackoverflow.com/questions/36968122/how-to-set-javafx-datepicker-value-correctly

--------------------------------------------------------------------
Retrieving and Modifying Values from Result Sets
https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
*/
/* -------------------------------------------------------------- */
