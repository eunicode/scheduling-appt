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
  private ComboBox<String> typeCombo;

  @FXML
  private DatePicker dateDatePicker;

  @FXML
  private ComboBox<String> startCombo;

  @FXML
  private ComboBox<String> endCombo;

  @FXML
  private TextField apptEditName;

  @FXML
  private TextField apptEditTitle;

  @FXML
  private TextField apptEditLocation;

  @FXML
  private TextField apptEditDescrip;

  @FXML
  private Button saveApptEditButton;

  @FXML
  private Button cancelApptEditButton;

  Appointment selectedAppointment;
  int selectedIdx;

  // hour
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

  ObservableList<String> appointmentType = FXCollections.observableArrayList(
    "Presentation",
    "Scrum"
  );

  /* -------------------------------------------------------------- */
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
    typeCombo.setItems(appointmentType);
    startCombo.setItems(apptTimeOptions);
    endCombo.setItems(apptTimeOptions);

    // Disable selecting weekends, past dates
    Callback<DatePicker, DateCell> dayCellFactory = this.disableWeekend();
    dateDatePicker.setDayCellFactory(dayCellFactory);
    // Disable text field editing
    dateDatePicker.getEditor().setDisable(true);
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void saveMondifyAppointmentHandler(ActionEvent event)
    throws IOException {
    try {
      int appointmentId = selectedAppointment.getAppointmentId();
      int customerId = selectedAppointment.getCustomerId();
      int userId = selectedAppointment.getUserId();

      String title = apptEditTitle.getText();
      String description = apptEditDescrip.getText();
      String location = apptEditLocation.getText();
      String assignedContact = apptEditName.getText();
      String type = typeCombo.getSelectionModel().getSelectedItem();
      String url = "";

      LocalDate date = dateDatePicker.getValue();

      String startTime = startCombo
        .getSelectionModel()
        .getSelectedItem();
      String endTime = endCombo.getSelectionModel().getSelectedItem();

      // Build date-time string
      String selectedStartTime = date + " " + startTime;
      String selectedEndTime = date + " " + endTime;

      if (typeCombo.getSelectionModel().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Type is unselected");
        alert.showAndWait();
        return;
      }

      if (date == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Date is not selected");
        alert.showAndWait();
        return;
      }

      if (startTime == null || endTime == null) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "Start and/or end time is not selected"
        );
        alert.showAndWait();
        return;
      }

      // Get zone ID of system's default timezone
      ZoneId zoneIdLocal = ZoneId.of(TimeZone.getDefault().getID());

      // Format mask - format used to translate string to LocalDateTime
      DateTimeFormatter format = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss"
      );

      // Parse string to LocalDateTime
      LocalDateTime startLocalTimeF = LocalDateTime.parse(
        selectedStartTime,
        format
      );

      LocalDateTime endLocalTimeF = LocalDateTime.parse(
        selectedEndTime,
        format
      );

      // Get local ZonedDateTime: start
      ZonedDateTime zonedStartLocal = ZonedDateTime.of(
        startLocalTimeF, // LocalDateTime
        zoneIdLocal // ZoneId
      );

      // Get UTC ZonedDateTime: start
      ZonedDateTime zonedStartUTC = zonedStartLocal.withZoneSameInstant(
        ZoneId.of("UTC")
      );

      // Get local ZonedDateTime: end
      ZonedDateTime zonedEndLocal = ZonedDateTime.of(
        endLocalTimeF,
        zoneIdLocal
      );

      // Get UTC zoned date-time: end
      ZonedDateTime zonedEndUTC = zonedEndLocal.withZoneSameInstant(
        ZoneId.of("UTC")
      );

      // Get fields from UTC ZonedDateTime
      int startYearUTC = zonedStartUTC.getYear();
      int startMonthUTC = zonedStartUTC.getMonthValue();
      int startUTCDay = zonedStartUTC.getDayOfMonth();
      int startHourUTC = zonedStartUTC.getHour();
      int startMinUTC = zonedStartUTC.getMinute();

      // Build UTC string
      String startDateTimeUTCString =
        Integer.toString(startYearUTC) +
        "-" +
        Integer.toString(startMonthUTC) +
        "-" +
        Integer.toString(startUTCDay) +
        " " +
        Integer.toString(startHourUTC) +
        ":" +
        Integer.toString(startMinUTC) +
        ":" +
        Integer.toString(startMinUTC);

      // Get fields from UTC ZonedDateTime
      int endYearUTC = zonedEndUTC.getYear();
      int endMonthUTC = zonedEndUTC.getMonthValue();
      int endDayUTC = zonedEndUTC.getDayOfMonth();
      int endHourUTC = zonedEndUTC.getHour();
      int endMinUTC = zonedEndUTC.getMinute();

      // Build UTC datetime string for MySQL command
      String endDateTimeUTCString =
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

      // Convert ZonedDateTime to string
      String zonedStartLocalString = zonedStartLocal.toString();
      String zonedEndLocalString = zonedEndLocal.toString();

      // Check if appointment has overlap
      if (
        AddAppointmentController.validateAppointmentTimes(
          startDateTimeUTCString,
          endDateTimeUTCString
        )
      ) {
        // Create appointment instance
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
          zonedStartLocalString,
          zonedEndLocalString
        );

        //
        DataProvider.getAppointmentsAllList().set(selectedIdx, appointment);

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
          startDateTimeUTCString +
          "', end = '" +
          endDateTimeUTCString +
          "' WHERE appointmentId = " +
          appointmentId;

        // Update db appointment
        int updatedAppointment = statement.executeUpdate(updateAppointment);

        if (updatedAppointment == 1) {
          System.out.println("One appointment was updated");
        }
      }
    } catch (SQLException | NullPointerException | DateTimeParseException e) {
      System.out.println("Error: " + e.getMessage());
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
    selectedIdx = index;

    Appointment newAppointment = (Appointment) appointment;

    String dateFromZonedDateTimeString = newAppointment
      .getStart()
      .substring(0, 10);

    LocalDate dateForCal = LocalDate.parse(dateFromZonedDateTimeString);

    this.apptEditName.setText(newAppointment.getContact());
    this.apptEditTitle.setText(newAppointment.getTitle());
    this.apptEditLocation.setText(newAppointment.getLocation());
    this.apptEditDescrip.setText(newAppointment.getDescription());
    this.dateDatePicker.setValue(dateForCal);

    // Set type to previously selected type
    String selectedType = newAppointment.getType();
    if ("Presentation".equals(selectedType)) {
      this.typeCombo.getSelectionModel().selectFirst();
    } else {
      this.typeCombo.getSelectionModel().select(1);
    }
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
