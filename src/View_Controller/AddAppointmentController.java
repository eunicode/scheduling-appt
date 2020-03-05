/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.fxml.Initializable;

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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class AddAppointmentController implements Initializable {
  @FXML
  private ComboBox<String> addAppointmentNameCombo;

  @FXML
  private ComboBox<String> addAppointmentTypeText;

  @FXML
  private ComboBox<String> addAppointmentStartTimeComboBox;

  @FXML
  private ComboBox<String> addAppointmentEndTimeComboBox;

  @FXML
  private DatePicker addAppointmentDatePicker;

  @FXML
  private TextField addAppointmentLocationText;

  @FXML
  private TextField addAppointmentDescriptionText;

  @FXML
  private TextField addCustomerContactText;

  @FXML
  private TextField addAppointmentTitleText;

  @FXML
  private Button saveAppointmentButton;

  @FXML
  private Button backAppointmentButton;

  @FXML
  private TableView<Appointment> addCustomerTableView;

  @FXML
  private TableColumn<Appointment, Integer> addAppointmentCustomerIDColumn;

  @FXML
  private TableColumn<Appointment, Integer> addAppointmentIDColumn;

  @FXML
  private TableColumn<Appointment, String> addAppointmentLocationColumn;

  @FXML
  private TableColumn<Appointment, String> addAppointmentLocalDateColumn;

  @FXML
  private TableColumn<Appointment, String> addAppointmentUTCDateColumn;

  Stage stage;
  Parent scene;
  DataProvider addCustomer;
  Appointment selectedCustomer;
  Appointment selectedAppointment;

  private static int customerId = 0;
  public static int userId;

  private ObservableList<String> nameData = FXCollections.observableArrayList();

  ObservableList<String> appointmentType = FXCollections.observableArrayList(
    "Presentation",
    "Scrum"
  );

  ObservableList<String> appointmentTime = FXCollections.observableArrayList(
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
  public void initialize(URL url, ResourceBundle rb) {
    // Set options for Customer
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet nameListRS = statement.executeQuery(
        "SELECT customerName FROM customer"
      );

      while (nameListRS.next()) {
        nameData.add(nameListRS.getString("customerName"));
      }
    } catch (SQLException e) {
      System.out.println(
        "Error building customer name list for appointment dropdown."
      );
    }

    // Set options for customer
    addAppointmentNameCombo.setItems(nameData);
    // Set options for type
    addAppointmentTypeText.setItems(appointmentType);
    // Set options for Time ComboBox
    addAppointmentStartTimeComboBox.setItems(appointmentTime);
    addAppointmentEndTimeComboBox.setItems(appointmentTime);

    // DatePicker - Set default day to today
    addAppointmentDatePicker.setValue(LocalDate.now());
    // Disable selecting weekends, past dates
    Callback<DatePicker, DateCell> dayCellFactory = this.disableWeekend();
    addAppointmentDatePicker.setDayCellFactory(dayCellFactory);
    // Disable text field editing
    addAppointmentDatePicker.getEditor().setDisable(true);
  }

  /* -------------------------------------------------------------- */
  public void setSelectedUserId(String selectedUserName) { // Called in login screen
    try {
      Connection conn = DBConnection.getConnection();

      PreparedStatement getUserId = conn.prepareStatement(
        "SELECT userId FROM user WHERE userName = '" + selectedUserName + "'"
      );

      ResultSet resultSetStatement = getUserId.executeQuery();

      while (resultSetStatement.next()) {
        userId = resultSetStatement.getInt(1);
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  private boolean validateName(int id) {
    if (id == 0) {
      Alert alert = new Alert(
        Alert.AlertType.WARNING,
        "Customer is unselected"
      );
      return false;
    } else {
      return true;
    }
  }

  /* -------------------------------------------------------------- */
  private boolean validateType(String type) {
    if (type == null) {
      Alert alert = new Alert(
        Alert.AlertType.WARNING,
        "Type is unselected"
      );
      return false;
    } else {
      return true;
    }
  }

  /* -------------------------------------------------------------- */
  private boolean validateStartAndEnd(String start, String end) {
    if (start == null || end == null) {
      Alert alert = new Alert(
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
  public static boolean validateAppointmentStart(
    String appointmentStartTime,
    String appointmentEndTime
  ) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");

    LocalDateTime startDateTime = LocalDateTime.parse(
      appointmentStartTime,
      format
    );
    LocalDateTime endDateTime = LocalDateTime.parse(appointmentEndTime, format);

    // Alerts for invalid start and end times
    boolean earlyAppointment = endDateTime.isBefore(startDateTime);
    boolean sameAppointment = endDateTime.isEqual(startDateTime);

    if (sameAppointment) {
      Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Cannot have same start and end times"
      );
      alert.showAndWait();
      return false;
    } else if (earlyAppointment) {
      Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Cannot have end time before start time"
      );
      alert.showAndWait();
      return false;
    }

    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String currentAppointments =
        "SELECT * FROM appointment WHERE ('" +
        appointmentStartTime +
        "' BETWEEN start AND end OR '" +
        appointmentEndTime +
        "' BETWEEN start AND end OR '" +
        appointmentStartTime +
        "' > start AND '" +
        appointmentEndTime +
        "' < end)";

      ResultSet checkAppointmentTimes = statement.executeQuery(
        currentAppointments
      );

      if (checkAppointmentTimes.next()) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "These times are invalid"
        );
        alert.showAndWait();
        return false; // boolean function
      }
    } catch (SQLException ex) {
      ex.getMessage();
    }
    return true;
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void saveAppointmentHandler(ActionEvent event)
    throws IOException, ParseException, ClassNotFoundException, SQLException {
    // Assign customer ID from selected drop list item.
    // Customer drop list corresponds to customer table
    customerId =
      addAppointmentNameCombo.getSelectionModel().getSelectedIndex() + 1;

    String title = addAppointmentTitleText.getText();
    String description = addAppointmentDescriptionText.getText();
    // String description = addAppointmentDescriptionText
    //   .getSelectionModel()
    //   .getSelectedItem();
    String location = addAppointmentLocationText.getText();
    // String location = addAppointmentLocationText
    //   .getSelectionModel()
    //   .getSelectedItem();
    String assignedContact = addCustomerContactText.getText();

    String type = addAppointmentTypeText.getSelectionModel().getSelectedItem();
    // String type = addAppointmentTypeText.getText();

    String url = "";

    LocalDate appointmentDate = addAppointmentDatePicker.getValue();

    // String startTime = addAppointmentStartTimeComboBox.getValue();
    String startTime = addAppointmentStartTimeComboBox
      .getSelectionModel()
      .getSelectedItem();

    String endTime = addAppointmentEndTimeComboBox
      .getSelectionModel()
      .getSelectedItem();

    // Validate data  
    validateName(customerId);
    validateType(type);
    validateStartAndEnd(startTime, endTime);
      
    String selectedStartDateTime = appointmentDate + " " + startTime;
    String selectedEndDateTime = appointmentDate + " " + endTime;

    System.out.println(selectedStartDateTime);

    ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());

    DateTimeFormatter format = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss"
    );

    LocalDateTime startDateTime = LocalDateTime.parse(
      selectedStartDateTime,
      format
    );
    LocalDateTime endDateTime = LocalDateTime.parse(
      selectedEndDateTime,
      format
    );

    ZonedDateTime zonedStartLocal = ZonedDateTime.of(
      startDateTime,
      localZoneId
    );
    ZonedDateTime zonedStartUTC = zonedStartLocal.withZoneSameInstant(
      ZoneId.of("UTC")
    );

    ZonedDateTime zonedEndLocal = ZonedDateTime.of(endDateTime, localZoneId);
    ZonedDateTime zonedEndUTC = zonedEndLocal.withZoneSameInstant(
      ZoneId.of("UTC")
    );

    int startUTCYear = zonedStartUTC.getYear();
    int startUTCMonth = zonedStartUTC.getMonthValue();
    int startUTCDay = zonedStartUTC.getDayOfMonth();
    int startUTCHour = zonedStartUTC.getHour();
    int startUTCMinute = zonedStartUTC.getMinute();

    String testZonedStart =
      Integer.toString(startUTCYear) +
      "-" +
      Integer.toString(startUTCMonth) +
      "-" +
      Integer.toString(startUTCDay) +
      " " +
      Integer.toString(startUTCHour) +
      ":" +
      Integer.toString(startUTCMinute) +
      ":" +
      Integer.toString(startUTCMinute);

    int endUTCYear = zonedEndUTC.getYear();
    int endUTCMonth = zonedEndUTC.getMonthValue();
    int endUTCDay = zonedEndUTC.getDayOfMonth();
    int endUTCHour = zonedEndUTC.getHour();
    int endUTCMinute = zonedEndUTC.getMinute();

    String testZonedEnd =
      Integer.toString(endUTCYear) +
      "-" +
      Integer.toString(endUTCMonth) +
      "-" +
      Integer.toString(endUTCDay) +
      " " +
      Integer.toString(endUTCHour) +
      ":" +
      Integer.toString(endUTCMinute) +
      ":" +
      Integer.toString(endUTCMinute);

    String startConstructorValue = zonedStartLocal.toString();
    String endConstructorValue = zonedEndLocal.toString();

    // If there is no overlap
    if (validateAppointmentStart(testZonedStart, testZonedEnd)) {
      Statement statement = DBConnection.getConnection().createStatement();

      String appointmentQuery =
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
        testZonedStart +
        "', '" +
        testZonedEnd +
        "', NOW(), " + // createDate
        "'test'" + // createdBy
        ", NOW(), " + // lastUpdate
        "'test')"; // lastUpdateBy

      int appointmentExecuteUpdate = statement.executeUpdate(appointmentQuery);

      // If one row was affected
      if (appointmentExecuteUpdate == 1) {
        System.out.println("Insert into appointment table was succcessful!");
      }

      Appointment appointment = new Appointment(
        1, // appointmentId
        customerId,
        userId,
        title,
        description,
        location,
        assignedContact,
        type,
        url,
        startConstructorValue,
        endConstructorValue
      );

      appointment.setAppointmentId(1);
      appointment.setCustomerId(customerId);
      appointment.setUserId(userId);
      appointment.setTitle(title);
      appointment.setDescription(description);
      appointment.setLocation(location);
      appointment.setContact(assignedContact);
      appointment.setType(type);
      appointment.setUrl(url);
      appointment.setStart(selectedStartDateTime);
      appointment.setEnd(selectedEndDateTime);

      //
      DataProvider.addAppointment(appointment);

      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

      // Return to appointment screen
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/AppointmentScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void backAppointmentHandler(ActionEvent event) throws IOException {
    Alert alert = new Alert(
      AlertType.CONFIRMATION,
      "You will lose any entered data. Continue?",
      ButtonType.YES
    );

    Optional<ButtonType> result = alert.showAndWait();

    // user chooses YES
    if (result.get() == ButtonType.YES) {
      
      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/AppointmentScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    } 
    // user closes the dialog
    else {
      
    }
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
