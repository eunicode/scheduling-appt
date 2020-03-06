/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.DataProvider;
import Utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class AppointmentAddController implements Initializable {
  @FXML
  private ComboBox<String> appointmentNameCombo;

  @FXML
  private ComboBox<String> appointmentTypeCombo;

  @FXML
  private ComboBox<String> appointmentStartCombo;

  @FXML
  private ComboBox<String> appointmentEndCombo;

  @FXML
  private DatePicker appointmentDatePicker;

  @FXML
  private TextField appointmentLocation;

  @FXML
  private TextField appointmentDescription;

  @FXML
  private TextField appointmentContact;

  @FXML
  private TextField appointmentTitle;

  @FXML
  private Button saveApptAddButton;

  @FXML
  private Button cancelApptAddButton;

  Stage stage;
  Parent scene;

  private static int customerId = 0;
  private String selectedCustomerName = "";
  public static int userId;

  private final ObservableList<String> nameData = FXCollections.observableArrayList();

  ObservableList<String> appointmentType = FXCollections.observableArrayList(
    "Presentation",
    "Scrum"
  );

  ObservableList<String> apptTimeOptions = FXCollections.observableArrayList(
    "09:00:00",
    "10:00:00",
    "11:00:00",
    "12:00:00",
    "13:00:00",
    "14:00:00",
    "15:00:00",
    "16:00:00",
    "17:00:00"
  );

  // Factory to create Cell of DatePicker
  private Callback<DatePicker, DateCell> disableWeekend() {
    // Lambda: A lambda is used so we can use a callback without an anonymous inner class.
    final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) ->
      new DateCell() {

        @Override
        public void updateItem(LocalDate item, boolean empty) {
          LocalDate today = LocalDate.now();
          super.updateItem(item, empty);

          // Disable weekends
          if (
            item.getDayOfWeek() == DayOfWeek.SATURDAY ||
            item.getDayOfWeek() == DayOfWeek.SUNDAY ||
            item.compareTo(today) < 0
          ) {
            setDisable(true);
          }
        }
      };
    return dayCellFactory;
  }

  /* -------------------------------------------------------------- */
  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(final URL url, final ResourceBundle rb) {
    // Set options for Customer
    try {
      final Statement statement = DBConnection
        .getConnection()
        .createStatement();

      final ResultSet nameListRS = statement.executeQuery(
        "SELECT customerName FROM customer"
      );

      while (nameListRS.next()) {
        nameData.add(nameListRS.getString("customerName"));
      }
    } catch (final SQLException e) {
      System.out.println(
        "Error building customer name list for appointment dropdown."
      );
    }

    // Set options for customer
    appointmentNameCombo.setItems(nameData);
    // Set options for type
    appointmentTypeCombo.setItems(appointmentType);
    // Set options for Time ComboBox
    appointmentStartCombo.setItems(apptTimeOptions);
    appointmentEndCombo.setItems(apptTimeOptions);

    // DatePicker - Set default day to today
    appointmentDatePicker.setValue(LocalDate.now());
    // Disable selecting weekends, past dates
    final Callback<DatePicker, DateCell> dayCellFactory = this.disableWeekend();
    appointmentDatePicker.setDayCellFactory(dayCellFactory);
    // Disable text field editing
    appointmentDatePicker.getEditor().setDisable(true);
  }

  /* -------------------------------------------------------------- */
  // Called in login screen
  public void findUserId(final String uname) {
    try {
      String query =
        "SELECT userId FROM user WHERE userName = '" + uname + "'";

      final Connection conn = DBConnection.getConnection();

      final PreparedStatement pStatement = conn.prepareStatement(query); // has advantages to createStatement()

      final ResultSet userIdRS = pStatement.executeQuery();

      if (userIdRS.next()) {
        userId = userIdRS.getInt(1);
      }
    } catch (final SQLException e) {
      System.out.println("Error finding user ID: " + e.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  private boolean validateName(final int id) {
    if (id == 0) {
      final Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Customer is unselected"
      );
      alert.showAndWait();
      return false;
    } else {
      return true;
    }
  }

  /* -------------------------------------------------------------- */
  private boolean validateType(final String type) {
    if (type == null) {
      final Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Type is unselected"
      );
      alert.showAndWait();
      return false;
    } else {
      return true;
    }
  }

  /* -------------------------------------------------------------- */
  private boolean validateStartAndEnd(final String start, final String end) {
    if (start == null || end == null) {
      final Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Start and/or end time is unselected"
      );
      alert.showAndWait();
      return false;
    } else {
      return true;
    }
  }

  /* -------------------------------------------------------------- */
  public static boolean validateAppointmentTimes(
    final String startString,
    final String endString
  ) {
    final DateTimeFormatter formatMask = DateTimeFormatter.ofPattern(
      "yyyy-M-d H:m:s"
    );

    final LocalDateTime startLocalTimeF = LocalDateTime.parse(
      startString,
      formatMask
    );
    final LocalDateTime endLocalTimeF = LocalDateTime.parse(
      endString,
      formatMask
    );

    // Alerts for invalid start and end times
    final boolean endBeforeStart = endLocalTimeF.isBefore(startLocalTimeF);
    final boolean endSameAsStart = endLocalTimeF.isEqual(startLocalTimeF);

    if (endSameAsStart) {
      final Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Cannot have same start and end times"
      );
      alert.showAndWait();
      return false;
    } else if (endBeforeStart) {
      final Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Cannot have end time before start time"
      );
      alert.showAndWait();
      return false;
    }

    // If start and end times are not the same or invalid, check if the interval is
    // valid
    try {
      final Statement statement = DBConnection
        .getConnection()
        .createStatement();

      final String overlapQuery =
        "SELECT * FROM appointment " +
        "WHERE ('" +
        startString +
        "' BETWEEN start AND end " +
        "OR '" +
        endString +
        "' BETWEEN start AND end " +
        "OR '" +
        startString +
        "' > start AND '" +
        endString +
        "' < end)";

      final ResultSet overlapRS = statement.executeQuery(overlapQuery);

      // If there appointments with the given times, show alert
      if (overlapRS.next()) {
        final Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "These times are unavailable because of overlap. Choose different times."
        );
        alert.showAndWait();
        return false; // boolean function
      }
    } catch (final SQLException ex) {
      ex.getMessage();
    }

    // If the times pass all 3 tests, return true
    return true;
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void saveApptAddHandler(final ActionEvent event)
    throws IOException, ParseException, ClassNotFoundException, SQLException {
    // Assign customer ID from selected drop list item.
    // Customer drop list corresponds to customer table
    customerId =
      appointmentNameCombo.getSelectionModel().getSelectedIndex() + 1; // This is not reliable for getting
    // customerId, but it is being used to
    // validate that customer is selected
    selectedCustomerName = appointmentNameCombo.getValue(); // get selected name from combobox
    final String title = appointmentTitle.getText();
    final String description = appointmentDescription.getText();
    final String location = appointmentLocation.getText();
    final String assignedContact = appointmentContact.getText();
    final String type = appointmentTypeCombo
      .getSelectionModel()
      .getSelectedItem();
    final String url = "";
    final LocalDate appointmentDate = appointmentDatePicker.getValue();

    // String startTime = appointmentStartCombo.getValue(); // either can be used for combobox
    final String startTime = appointmentStartCombo
      .getSelectionModel()
      .getSelectedItem();
    final String endTime = appointmentEndCombo
      .getSelectionModel()
      .getSelectedItem();

    // Validate data
    if (
      !validateName(customerId) ||
      !validateType(type) ||
      !validateStartAndEnd(startTime, endTime)
    ) {
      return;
    }

    final String startDateTime = appointmentDate + " " + startTime;
    final String endDateTime = appointmentDate + " " + endTime;

    final ZoneId zoneIdLocal = ZoneId.of(TimeZone.getDefault().getID());

    final DateTimeFormatter formatMask = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss"
    );

    final LocalDateTime startLocalTimeF = LocalDateTime.parse(
      startDateTime,
      formatMask
    );
    final LocalDateTime endLocalTimeF = LocalDateTime.parse(
      endDateTime,
      formatMask
    );

    final ZonedDateTime zonedStartLocal = ZonedDateTime.of(
      startLocalTimeF,
      zoneIdLocal
    );
    final ZonedDateTime zonedStartUTC = zonedStartLocal.withZoneSameInstant(
      ZoneId.of("UTC")
    );

    final ZonedDateTime zonedEndLocal = ZonedDateTime.of(
      endLocalTimeF,
      zoneIdLocal
    );
    final ZonedDateTime zonedEndUTC = zonedEndLocal.withZoneSameInstant(
      ZoneId.of("UTC")
    );

    final int startYearUTC = zonedStartUTC.getYear();
    final int startMonthUTC = zonedStartUTC.getMonthValue();
    final int startDayUTC = zonedStartUTC.getDayOfMonth();
    final int startHourUTC = zonedStartUTC.getHour();
    final int startMinUTC = zonedStartUTC.getMinute();

    final String startDateTimeUTCString =
      Integer.toString(startYearUTC) +
      "-" +
      Integer.toString(startMonthUTC) +
      "-" +
      Integer.toString(startDayUTC) +
      " " +
      Integer.toString(startHourUTC) +
      ":" +
      Integer.toString(startMinUTC) +
      ":" +
      Integer.toString(startMinUTC);

    final int endYearUTC = zonedEndUTC.getYear();
    final int endMonthUTC = zonedEndUTC.getMonthValue();
    final int endDayUTC = zonedEndUTC.getDayOfMonth();
    final int endHourUTC = zonedEndUTC.getHour();
    final int endMinUTC = zonedEndUTC.getMinute();

    final String endDateTimeUTCString =
      Integer.toString(endYearUTC) +
      "-" +
      Integer.toString(endMonthUTC) +
      "-" +
      Integer.toString(endDayUTC) +
      " " +
      Integer.toString(endHourUTC) +
      ":" +
      Integer.toString(endMinUTC) +
      ":" +
      Integer.toString(endMinUTC);

    // final String zonedStartLocalString = zonedStartLocal.toString();
    // final String zonedEndLocalString = zonedEndLocal.toString();

    // If the times are kosher, and there is no overlap, execute SQL command
    if (
      validateAppointmentTimes(startDateTimeUTCString, endDateTimeUTCString)
    ) {
      final Statement statement = DBConnection
        .getConnection()
        .createStatement();
      final Statement statement2 = DBConnection
        .getConnection()
        .createStatement();

      // Get customer ID from customer name
      final String nameQuery =
        "SELECT customerId " +
        "FROM customer " +
        "WHERE customerName = " +
        "'" +
        selectedCustomerName +
        "'";

      final ResultSet nameRS = statement2.executeQuery(nameQuery);

      if (nameRS.next()) {
        customerId = nameRS.getInt("customerId");
      }

      final String appointmentQuery =
        "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)" +
        " VALUES (" +
        customerId +
        ", " +
        "1" + // consultant/user
        ", '" +
        title +
        "', '" +
        description +
        "', '" +
        location +
        "', '" +
        assignedContact +
        "', '" +
        type +
        "', '" +
        url +
        "', '" +
        startDateTimeUTCString +
        "', '" +
        endDateTimeUTCString +
        "', NOW(), " + // createDate
        "'test'" + // createdBy
        ", NOW(), " + // lastUpdate
        "'test')"; // lastUpdateBy

      final int checkInsertSucess = statement.executeUpdate(
        appointmentQuery
      );

      // If one row was affected
      if (checkInsertSucess == 1) {
        System.out.println("One row was inserted into appointment table");
      }

      // Create appointment object with user input
      final Appointment appointment = new Appointment(
        1, // appointmentId
        customerId,
        userId,
        title,
        description,
        location,
        assignedContact,
        type,
        url,
        startDateTime,
        endDateTime
      );

      // Add appointment object to appointment ObservableList
      DataProvider.addAppointment(appointment);

      // Return to appointment screen
      final Stage stage = (Stage) ((Button) event.getSource()).getScene()
        .getWindow();

      final Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/AppointmentScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    } else {
      return;
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void cancelApptAddHandler(final ActionEvent event)
    throws IOException {
    final Alert alert = new Alert(
      AlertType.CONFIRMATION,
      "You will lose any entered data. Continue?",
      ButtonType.YES
    );

    final Optional<ButtonType> result = alert.showAndWait();

    // user chooses YES
    if (result.get() == ButtonType.YES) {
      final Stage stage = (Stage) ((Button) event.getSource()).getScene()
        .getWindow();
      final Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/AppointmentScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    }
    // user closes the dialog
    else {}
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
TODO

This shows warnings, but does it prevent the appointment from being saved?
I think it does bc the alert pauses the function, so the insert command never gets run.

--------------------------------------------------------------------
How to disable the TextField of a JavaFx DatePicker?
https://stackoverflow.com/questions/41833092/how-to-disable-the-textfield-of-a-javafx-datepicker

Disable dates
https://stackoverflow.com/questions/41001703/how-to-customize-datepicker-in-javafx-so-that-i-can-restrict-the-user-to-not-be
https://stackoverflow.com/questions/35907325/how-to-set-minimum-and-maximum-date-in-datepicker-calander-in-javafx8
https://stackoverflow.com/questions/48238855/how-to-disable-past-dates-in-datepicker-of-javafx-scene-builder

https://www.geeksforgeeks.org/javafx-alert-with-examples/
https://stackoverflow.com/questions/39900229/alert-in-java-fx

--------------------------------------------------------------------

*/
/* -------------------------------------------------------------- */
