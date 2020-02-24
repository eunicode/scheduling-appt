/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import static View_Controller.CustomerTableController.deleteCustomer;

//import Database.DBConnection;
import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import Model.User;
import java.io.IOException;
import java.net.URL;
//import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
//import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
//import ryanhildebrantsoftware2.RyanHildebrantSoftware2;
import scheduler.Scheduler;
import Database.DBConnection;
//import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class AppointmentScreenController implements Initializable {
  @FXML
  private RadioButton viewByMonthRadioButton;

  @FXML
  private ToggleGroup appointmentRadioButtonGroup;

  @FXML
  private RadioButton viewByWeekRadioButton;

  @FXML
  private TableView<Appointment> appointmentTableView;

  @FXML
  private TableColumn<Appointment, String> customerNameColumn;

  @FXML
  private TableColumn<Appointment, String> customerContactColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentTitleColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentTypeColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentLocationColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentDescriptionColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentStartColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentEndColumn;

  @FXML
  private Button backButton;

  @FXML
  private Button modifyAppointmentButton;

  @FXML
  private Button deleteAppointmentButton;

  @FXML
  private ComboBox<String> viewByComboBox;

  @FXML
  private Button searchTableSorterButton;

  Stage stage;
  Parent scene;
  private Appointment selectedAppointment;

  ObservableList<String> viewByMonth = FXCollections.observableArrayList(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  );
  ObservableList<String> viewByWeek = FXCollections.observableArrayList(
    "Previous Week",
    "This Week",
    "Next Week"
  );

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    DataProvider.getAllAppointmentsTableList().clear();
    appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());

    DataProvider populateAppointments = new DataProvider();
    populateAppointments.populateAppointmentTable();

    appointmentTableView.getSelectionModel().selectFirst();
    customerNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    customerContactColumn.setCellValueFactory(
      new PropertyValueFactory<>("contact")
    );
    appointmentTitleColumn.setCellValueFactory(
      new PropertyValueFactory<>("title")
    );
    appointmentTypeColumn.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    appointmentLocationColumn.setCellValueFactory(
      new PropertyValueFactory<>("location")
    );
    appointmentDescriptionColumn.setCellValueFactory(
      new PropertyValueFactory<>("description")
    );
    appointmentStartColumn.setCellValueFactory(
      new PropertyValueFactory<>("start")
    );
    appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
  }

  @FXML
  private void viewByMonthHandler(ActionEvent event) {
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    if (viewByMonthRadioButton.isSelected()) {
      Calendar cal = Calendar.getInstance();
      String month = new SimpleDateFormat("MMMM").format(cal.getTime());

      DataProvider.setMonthlyView(month);
    }

    sortAppointment();
    viewByComboBox.setItems(viewByMonth);
  }

  @FXML
  private void viewByWeekHandler(ActionEvent event) {
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    if (viewByWeekRadioButton.isSelected()) {
      DataProvider.setWeeklyView(0);
    }

    sortAppointment();
    viewByComboBox.setItems(viewByWeek);
  }

  @FXML
  void searchTableSorterHandler(ActionEvent event) {
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    if (viewByMonthRadioButton.isSelected()) {
      String selectedMonth = viewByComboBox.getValue();
      DataProvider.setMonthlyView(selectedMonth);
    }

    if (viewByWeekRadioButton.isSelected()) {
      int numberOfWeeks = 0;

      if (viewByComboBox.getValue() == "Previous Week") {
        numberOfWeeks = -1;
      } else if (viewByComboBox.getValue() == "This Week") {
        numberOfWeeks = 0;
      } else {
        numberOfWeeks = 1;
      }

      DataProvider.setWeeklyView(numberOfWeeks);
    }

    sortAppointment();

    //Lambda expressions to populate appointment table
    customerNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    customerContactColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getContact())
    );
    appointmentTitleColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getTitle())
    );
    appointmentLocationColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getLocation())
    );
    appointmentDescriptionColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getDescription())
    );
    appointmentStartColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getStart())
    );
    appointmentEndColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getEnd())
    );

    if (viewByMonthRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByMonth());
    } else {
      appointmentTableView.setItems(DataProvider.getAppointmentsByWeek());
    }
  }

  @FXML
  void modifyAppointmentHandler(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
      getClass().getResource("/View_Controller/ModifyAppointment.fxml")
    );
    loader.load();
    ModifyAppointmentController controller = loader.getController();
    Appointment appointment = appointmentTableView
      .getSelectionModel()
      .getSelectedItem();

    int index = appointmentTableView.getSelectionModel().getSelectedIndex();
    controller.setAppointment(appointment, index);

    stage = (Stage) modifyAppointmentButton.getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  @FXML
  private void deleteAppointmentHandler(ActionEvent event) {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "This will delete the customer record, do you want to continue?"
    );
    alert.setTitle("Confirmation of Deletion");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      //            ObservableList<Appointment> allAppointments, singleAppointment;
      //            allAppointments = appointmentTableView.getItems();
      //            singleAppointment = appointmentTableView.getSelectionModel().getSelectedItems();
      //            singleAppointment.forEach(allAppointments::remove);
      selectedAppointment =
        appointmentTableView.getSelectionModel().getSelectedItem();

      deleteAppointment(selectedAppointment);
    }
  }

  @FXML
  private void backHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  public void deleteAppointment(Appointment appointment) {
    int selectedID = appointment.getAppointmentId();

    System.out.println(selectedID);

    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String deleteCustomer =
        "DELETE FROM appointment WHERE appointmentId = " + selectedID;
      int deletedCustomer = statement.executeUpdate(deleteCustomer);

      if (deletedCustomer > 0) {
        DataProvider.getAllAppointmentsTableList().remove(appointment);
        System.out.println(
          "Appointment record was successfully deleted from the database!"
        );
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void sortAppointment() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();

      String getCustomerName =
        "SELECT customer.customerName FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
        "GROUP BY appointment.contact, MONTH(start), start";

      ResultSet customerNameResults = statement.executeQuery(getCustomerName);
      while (customerNameResults.next()) {
        String customerName = customerNameResults.toString();
        customerNameColumn.setCellValueFactory(
          new PropertyValueFactory<>(customerName)
        );
      }
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }

    //Lambda expressions to populate appointment table
    customerNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    customerContactColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getContact())
    );
    appointmentTitleColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getTitle())
    );
    appointmentLocationColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getLocation())
    );
    appointmentDescriptionColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getDescription())
    );
    appointmentStartColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getStart())
    );
    appointmentEndColumn.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getEnd())
    );

    if (viewByMonthRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByMonth());
    } else {
      appointmentTableView.setItems(DataProvider.getAppointmentsByWeek());
    }
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
