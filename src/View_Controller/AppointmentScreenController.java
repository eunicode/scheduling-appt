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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class AppointmentScreenController implements Initializable {
  private ToggleGroup weekOrMonthToggleGroup;

  @FXML
  private RadioButton weekViewRadioButton;

  @FXML
  private RadioButton monthViewRadioButton;

  @FXML
  private RadioButton allViewRadioButton;

  @FXML
  private TableView<Appointment> appointmentTable;

  @FXML
  private TableColumn<Appointment, String> apptCustomerCol;

  @FXML
  private TableColumn<Appointment, String> apptTypeCol;

  @FXML
  private TableColumn<Appointment, String> apptStartCol;

  @FXML
  private TableColumn<Appointment, String> apptEndCol;

  @FXML
  private TableColumn<Appointment, String> apptTitleCol;

  @FXML
  private TableColumn<Appointment, String> apptLocationCol;

  @FXML
  private TableColumn<Appointment, String> apptDescriptionCol;

  @FXML
  private TableColumn<Appointment, String> apptContactCol;

  @FXML
  private Button apptEditButton;

  Stage stage;
  Parent scene;
  private Appointment selectedAppointment;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Create ToggleGroup for radio buttons with Java instead of FXML
    weekOrMonthToggleGroup = new ToggleGroup();
    this.weekViewRadioButton.setToggleGroup(weekOrMonthToggleGroup);
    this.monthViewRadioButton.setToggleGroup(weekOrMonthToggleGroup);
    this.allViewRadioButton.setToggleGroup(weekOrMonthToggleGroup);

    // Populate appointments table
    DataProvider.getAppointmentsAllList().clear();
    appointmentTable.setItems(DataProvider.getAppointmentsAllList()); // 
    DataProvider.createAppointmentObjectObservableList(); // investigate

    // Have first appointment selected by default
    appointmentTable.getSelectionModel().selectFirst();

    // Bind columns to values from customer object
    apptCustomerCol.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    apptContactCol.setCellValueFactory(
      new PropertyValueFactory<>("contact")
    );
    apptTitleCol.setCellValueFactory(
      new PropertyValueFactory<>("title")
    );
    apptTypeCol.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    apptLocationCol.setCellValueFactory(
      new PropertyValueFactory<>("location")
    );
    apptDescriptionCol.setCellValueFactory(
      new PropertyValueFactory<>("description")
    );
    apptStartCol.setCellValueFactory(
      new PropertyValueFactory<>("start")
    );
    apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void weekViewHandler(ActionEvent event) {
    // Clear observablelist of appointment objects
    DataProvider.getAppointmentsAllList().clear();
    DataProvider.getAppointmentsMonth().clear();
    DataProvider.getAppointmentsWeek().clear();

    if (weekViewRadioButton.isSelected()) {
      DataProvider.createAppointmentWeekList();
    }

    // Lambda: A lambda is used to bind columns to values.
    // A lambda can also be used if PropertyValueFactory() cannot be used because the row is an ArrayList instead of an object,
    // and therefore does not have a property getter.
    apptCustomerCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getCustomerName())
    );
    apptContactCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getContact())
    );
    apptTitleCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getTitle())
    );
    apptLocationCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getLocation())
    );
    apptDescriptionCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getDescription())
    );
    apptStartCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getStart())
    );
    apptEndCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getEnd())
    );

    // Change table view depending on selected radio button
    if (weekViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsWeek());
    } else if (monthViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsMonth());
    } else if (allViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsAllList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void monthViewHandler(ActionEvent event) {
    DataProvider.getAppointmentsAllList().clear();
    DataProvider.getAppointmentsMonth().clear();
    DataProvider.getAppointmentsWeek().clear();

    if (monthViewRadioButton.isSelected()) {
      DataProvider.createAppointmentMonthList();
    }

    apptCustomerCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getCustomerName())
    );
    apptContactCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getContact())
    );
    apptTitleCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getTitle())
    );
    apptLocationCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getLocation())
    );
    apptDescriptionCol.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getDescription())
    );
    apptStartCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getStart())
    );
    apptEndCol.setCellValueFactory(
      appointment -> new SimpleStringProperty(appointment.getValue().getEnd())
    );

    if (weekViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsWeek());
    } else if (monthViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsMonth());
    } else if (allViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsAllList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void allViewHandler(ActionEvent event) {
    DataProvider.getAppointmentsAllList().clear(); 
    DataProvider.getAppointmentsMonth().clear();
    DataProvider.getAppointmentsWeek().clear();

    DataProvider populateAppointments = new DataProvider();
    populateAppointments.createAppointmentObjectObservableList();

    apptCustomerCol.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    apptContactCol.setCellValueFactory(
      new PropertyValueFactory<>("contact")
    );
    apptTitleCol.setCellValueFactory(
      new PropertyValueFactory<>("title")
    );
    apptTypeCol.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    apptLocationCol.setCellValueFactory(
      new PropertyValueFactory<>("location")
    );
    apptDescriptionCol.setCellValueFactory(
      new PropertyValueFactory<>("description")
    );
    apptStartCol.setCellValueFactory(
      new PropertyValueFactory<>("start")
    );
    apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));

    if (weekViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsWeek());
    } else if (monthViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsMonth());
    } else if (allViewRadioButton.isSelected()) {
      appointmentTable.setItems(DataProvider.getAppointmentsAllList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void apptAddButtonHandler(ActionEvent event)
    throws IOException {
    Parent parent = FXMLLoader.load(
      getClass().getResource("AppointmentAdd.fxml")
    );
    Scene scene = new Scene(parent);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.hide();
    stage.setScene(scene);
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  void apptEditHandler(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
      getClass().getResource("/View_Controller/AppointmentEdit.fxml")
    );
    loader.load();

    AppointmentEditController controller = loader.getController();

    // Get selected appointment object
    Appointment appointment = appointmentTable
      .getSelectionModel()
      .getSelectedItem();

    // Get index of selected row  
    int index = appointmentTable.getSelectionModel().getSelectedIndex();
    // Pass appointment object and its index to edit appointment screen
    controller.setAppointment(appointment, index);

    stage = (Stage) apptEditButton.getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void apptDeleteHandler(ActionEvent event) {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "Are you sure you want to delete this customer and its data?"
    );

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      // Get selected row (Appointment object)
      selectedAppointment =
        appointmentTable.getSelectionModel().getSelectedItem();

      // Run SQL command to delete appointment record
      deleteAppt(selectedAppointment);
    }
  }

  /* -------------------------------------------------------------- */
  public void deleteAppt(Appointment appointment) {
    int selectedID = appointment.getAppointmentId();

    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String SQL =
        "DELETE FROM appointment WHERE appointmentId = " + selectedID;
      int checkDeleteSuccess = statement.executeUpdate(SQL);

      if (checkDeleteSuccess == 1) {
        DataProvider.getAppointmentsAllList().remove(appointment);

        System.out.println(
          "Appointment(s) were deleted from appointment table"
        );
      }

    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void apptBackHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
PropertyValueFactory<S,T>
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/cell/PropertyValueFactory.html
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableColumn.html

A convenience implementation of the Callback interface, designed specifically for use within the TableColumn cell value factory. 

ObservableList<Person> data = ...
TableView<Person> tableView = new TableView<Person>(data);

TableColumn<Person,String> firstNameCol = new TableColumn<Person,String>("First Name");
firstNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));

In this example, the "firstName" string, 
is used as a reference to an assumed firstNameProperty() method, 
in the Person class type (which is the class type of the TableView items list). 
Additionally, this method must return a Property instance. 
If a method meeting these requirements is found, then the TableCell is populated with this ObservableValue. 
In addition, the TableView will automatically add an observer to the returned value, 
such that any changes fired will be observed by the TableView, resulting in the cell immediately updating.

If no method matching this pattern exists, there is fall-through support for attempting to call get<property>() or is<property>() 
(that is, getFirstName() or isFirstName() in the example above). 

--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
