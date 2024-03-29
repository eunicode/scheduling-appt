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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
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
  private String customerName;
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

  // Constructor
  public Appointment() {} // no args

  // Constructor overloading
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

  // Constructor overloading
  public Appointment( // 11 params
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
  // Getters
  public int getAppointmentId() {
    return appointmentId;
  }

  public int getUserId() {
    return userId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public String getType() {
    return type;
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

  public String getUrl() {
    return url;
  }

  /* -------------------------------------------------------------- */
  // Setters
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

  /* -------------------------------------------------------------- */
  public void upcomingAppointmentAlert() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet todaysAppointmentsRS = statement.executeQuery(
        "SELECT customer.customerName, start " +
        "FROM appointment " +
        "INNER JOIN customer " +
        "ON customer.customerId = appointment.customerId " +
        "WHERE DATE(start) = curdate()"
      );

      LocalTime currentLocalTime = LocalTime.now();

      // Iterate upcoming appointments
      while (todaysAppointmentsRS.next()) {
        String startTime = todaysAppointmentsRS.getString("start");

        DateTimeFormatter dateFormatMask = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss.S"
        );

        LocalDateTime localTimeF = LocalDateTime.parse(
          startTime,
          dateFormatMask
        );

        ZonedDateTime zonedTimeF_UTC = localTimeF.atZone(ZoneId.of("UTC"));

        ZoneId zoneIdLocal = ZoneId.systemDefault();

        ZonedDateTime zonedTimeUTC = zonedTimeF_UTC.withZoneSameInstant(
          zoneIdLocal
        );

        DateTimeFormatter formatMaskHrMin = DateTimeFormatter.ofPattern(
          "kk:mm"
        );

        LocalTime localTime = LocalTime.parse(
          zonedTimeUTC.toString().substring(11, 16),
          formatMaskHrMin
        );

        long difference = ChronoUnit.MINUTES.between(
          currentLocalTime,
          localTime
        );

        // Alert if there's an appointment 15 minutes within login
        if (difference > 0 && difference <= 15) {
          // If system locale is Korean
          if (Locale.getDefault().toString().equals("ko_KR")) {
            Alert alert = new Alert(
              Alert.AlertType.INFORMATION,
              "15 분 내에 약속이 있습니다."
            );
            alert.showAndWait();
            break;
          } 
          // If system locale is not Koream
          else {
            Alert alert = new Alert(
              Alert.AlertType.INFORMATION,
              "You have an appointment within 15 minutes."
            );
            alert.showAndWait();
            break;
          }
        }
      } // while
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
1 Using JavaFX Properties and Binding
https://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm#JFXBD107

Defining a Property
Define a variable to store the property
Define a getter for the property's value
Define a setter for the property's value
Define a getter for the property itself

--------------------------------------------------------------------
How to set javafx datepicker value correctly?
https://stackoverflow.com/questions/36968122/how-to-set-javafx-datepicker-value-correctly

--------------------------------------------------------------------
Retrieving and Modifying Values from Result Sets
https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html

ZonedDateTime withZoneSameInstant()
https://www.geeksforgeeks.org/zoneddatetime-withzonesameinstant-method-in-java-with-examples/

*/
/* -------------------------------------------------------------- */
