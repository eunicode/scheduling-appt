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
// import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.ResourceBundle;
// import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class ModifyAppointmentController implements Initializable {
  @FXML
  private ComboBox<String> modifyTypeText;

  @FXML
  private DatePicker modifyDate;

  @FXML
  private ComboBox<String> modifyStartComboBox;

  @FXML
  private ComboBox<String> modifyEndComboBox;

  @FXML
  private TextField modifyContactNameText;

  @FXML
  private TextField modifyTitleText;

  @FXML
  private TextField modifyLocationComboBox;

  @FXML
  private TextField modifyURLText;

  @FXML
  private TextField modifyDescriptionComboBox;

  @FXML
  private Button saveModifyAppointmentButton;

  @FXML
  private Button cancelModifyAppointmentButton;

  Appointment selectedAppointment;
  int selectedIndex;

  // hour
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

  ObservableList<String> appointmentType = FXCollections.observableArrayList(
    "Presentation",
    "Scrum"
  );

  /* -------------------------------------------------------------- */
  // Factory to create Cell of DatePicker
  private Callback<DatePicker, DateCell> disableWeekend() {
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
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    modifyTypeText.setItems(appointmentType);
    modifyStartComboBox.setItems(appointmentTime);
    modifyEndComboBox.setItems(appointmentTime);
    //   modifyDescriptionComboBox.setItems(appointmentDescription);
    //   modifyLocationComboBox.setItems(appointmentLocation);

    // Disable selecting weekends, past dates
    Callback<DatePicker, DateCell> dayCellFactory = this.disableWeekend();
    modifyDate.setDayCellFactory(dayCellFactory);
    // Disable text field editing
    modifyDate.getEditor().setDisable(true);
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void saveMondifyAppointmentHandler(ActionEvent event)
    throws IOException {
    try {
      int appointmentId = selectedAppointment.getAppointmentId();
      int customerId = selectedAppointment.getCustomerId();
      int userId = selectedAppointment.getUserId();

      String title = modifyTitleText.getText();
      String description = modifyDescriptionComboBox.getText();
      //   String description = modifyDescriptionComboBox.getSelectionModel().getSelectedItem();
      String location = modifyLocationComboBox.getText();
      //   String location = modifyLocationComboBox.getSelectionModel().getSelectedItem();
      String assignedContact = modifyContactNameText.getText();
      String type = modifyTypeText.getSelectionModel().getSelectedItem();
      //   String type = modifyTypeText.getText();
      //   String url = modifyURLText.getText();
      String url = "";

      LocalDate date = modifyDate.getValue();

      String startTime = modifyStartComboBox
        .getSelectionModel()
        .getSelectedItem();
      String endTime = modifyEndComboBox.getSelectionModel().getSelectedItem();

      // Build date-time string
      String selectedStartTime = date + " " + startTime;
      String selectedEndTime = date + " " + endTime;

      if (modifyTypeText.getSelectionModel().isEmpty()) {
        Alert alert = new Alert(
          Alert.AlertType.WARNING,
          "Type is unselected"
        );
        return;
      } 

      if (date == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Date is not selected");
        alert.showAndWait();
        return;
      }

      if (startTime == null || endTime == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Start and/or end time is not selected");
        alert.showAndWait();
        return;
      }

      // Get current date-time from system clock in default timezone
      LocalDateTime localTime = LocalDateTime.now();

      // Get ID of system's default timezone
      ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());

      // Format mask - format used to translate string to LocalDateTime
      DateTimeFormatter format = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss"
      );

      // Parse string to LocalDateTime
      LocalDateTime startDateTime = LocalDateTime.parse(
        selectedStartTime,
        format
      );

      LocalDateTime endDateTime = LocalDateTime.parse(selectedEndTime, format);

      // Get local ZonedDateTime: start
      ZonedDateTime zonedStartLocal = ZonedDateTime.of(
        startDateTime, // LocalDateTime
        localZoneId // ZoneId
      );

      // Get UTC ZonedDateTime: start
      ZonedDateTime zonedStartUTC = zonedStartLocal.withZoneSameInstant(
        ZoneId.of("UTC")
      );

      // Get local ZonedDateTime: end
      ZonedDateTime zonedEndLocal = ZonedDateTime.of(endDateTime, localZoneId);

      // Get UTC zoned date-time: end
      ZonedDateTime zonedEndUTC = zonedEndLocal.withZoneSameInstant(
        ZoneId.of("UTC")
      );

      // Get fields from UTC ZonedDateTime
      int startUTCYear = zonedStartUTC.getYear();
      int startUTCMonth = zonedStartUTC.getMonthValue();
      int startUTCDay = zonedStartUTC.getDayOfMonth();
      int startUTCHour = zonedStartUTC.getHour();
      int startUTCMinute = zonedStartUTC.getMinute();

      // Build UTC string
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

      // Get fields from UTC ZonedDateTime
      int endUTCYear = zonedEndUTC.getYear();
      int endUTCMonth = zonedEndUTC.getMonthValue();
      int endUTCDay = zonedEndUTC.getDayOfMonth();
      int endUTCHour = zonedEndUTC.getHour();
      int endUTCMinute = zonedEndUTC.getMinute();

      // Build UTC datetime string for MySQL command
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

      // Convert ZonedDateTime to string
      String startConstructorValue = zonedStartLocal.toString();
      String endConstructorValue = zonedEndLocal.toString();

      // Check if appointment has overlap
      if (
        AddAppointmentController.validateAppointmentStart(
          testZonedStart,
          testZonedEnd
        )
      ) {
        // Create appointment instance
        //   Appointment appointment = new Appointment(appointmentId, customerId, userId, title, description, location, assignedContact, type, url, startConstructorValue, endConstructorValue);
        Appointment appointment = new Appointment(
          appointmentId,
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

        //
        DataProvider
          .getAllAppointmentsTableList()
          .set(selectedIndex, appointment);

        Statement statement = DBConnection.getConnection().createStatement();

        // Get appointment data
        ResultSet selectAppointment = statement.executeQuery(
          "SELECT * FROM appointment WHERE appointmentId = " +
          appointmentId +
          ""
        );

        //
        while (selectAppointment.next()) {
          customerId = selectAppointment.getInt("customerId");
        }

        // Build MySQL query - use UTC
        String updateAppointment =
          "UPDATE appointment SET title = '" +
          title +
          "', description = '" +
          description +
          "', location = '" +
          location +
          "', contact = '" +
          assignedContact +
          "', type = '" +
          type +
          "', url = '" +
          url +
          "', start = '" +
          testZonedStart +
          "', end = '" +
          testZonedEnd +
          "' WHERE appointmentId = " +
          appointmentId;

        // Update db appointment
        int updatedAppointment = statement.executeUpdate(updateAppointment);

        if (updatedAppointment == 1) {
          System.out.println("Appointment was updated successfully!");
        }
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    } catch (NullPointerException ex) {
      System.out.println("Error " + ex.getMessage());
    } catch (DateTimeParseException ex) {
      System.out.println("Error " + ex.getMessage());
    }

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/AppointmentScreen.fxml")
    );

    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void cancelModifyAppointmentHandler(ActionEvent event)
    throws IOException {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "You will lose all changes. Continue?"
    );

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/AppointmentScreen.fxml")
      );

      stage.setScene(new Scene((Parent) scene));
      stage.show();
    }
  }

  /* -------------------------------------------------------------- */
  // Populate form with previously saved data
  public void setAppointment(Appointment appointment, int index) {
    selectedAppointment = appointment;
    selectedIndex = index;

    Appointment newAppointment = (Appointment) appointment;

    String dateFromZonedDateTimeString = newAppointment
      .getStart()
      .substring(0, 10);
    System.out.println("Hi" + dateFromZonedDateTimeString);
    LocalDate dateForCal = LocalDate.parse(dateFromZonedDateTimeString);
    // LocalDate startDate = LocalDate.parse(newAppointment.getStart());
    // endDate = LocalDate.parse(newAppointment.getEnd());

    this.modifyContactNameText.setText(newAppointment.getContact());
    this.modifyTitleText.setText(newAppointment.getTitle());
    this.modifyLocationComboBox.setText(newAppointment.getLocation());
    this.modifyDescriptionComboBox.setText(newAppointment.getDescription());
    this.modifyDate.setValue(dateForCal);

    // Set type to previously selected type
    String selectedType = newAppointment.getType();
    if (selectedType == "Presentation") {
      this.modifyTypeText.getSelectionModel().selectFirst();
    } else {
      this.modifyTypeText.getSelectionModel().select(1);
    }
    //   this.modifyURLText.setText(newAppointment.getUrl());
    //   this.modifyTypeText.setText((newAppointment.getType()));

  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
How to set default value in comboBox javafx?
https://stackoverflow.com/questions/34949422/how-to-set-default-value-in-combobox-javafx

How to check if any item is selected in JavaFX ComboBox
https://stackoverflow.com/questions/36963551/how-to-check-if-any-item-is-selected-in-javafx-combobox

--------------------------------------------------------------------
java.time.ZonedDateTime.of() Method Example
https://www.tutorialspoint.com/javatime/javatime_zoneddatetime_of1.htm
https://www.threeten.org/articles/zoned-date-time.html

*/
/* -------------------------------------------------------------- */
