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
import javafx.scene.control.ComboBox;
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
  private RadioButton viewByWeekRadioButton;

  @FXML
  private RadioButton viewByMonthRadioButton;

  @FXML
  private RadioButton viewByAllRadioButton;

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

  /**
   * Initializes the controller class.
     * @param url
     * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Create ToggleGroup for radio buttons with Java instead of FXML
    weekOrMonthToggleGroup = new ToggleGroup();
    this.viewByWeekRadioButton.setToggleGroup(weekOrMonthToggleGroup);
    this.viewByMonthRadioButton.setToggleGroup(weekOrMonthToggleGroup);
    this.viewByAllRadioButton.setToggleGroup(weekOrMonthToggleGroup);

    DataProvider.getAllAppointmentsTableList().clear();
    appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());

    DataProvider populateAppointments = new DataProvider();
    DataProvider.populateAppointmentTable();

    // Have first appointment selected by default
    appointmentTableView.getSelectionModel().selectFirst();

    // Bind columns to values from customer object
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

  /* -------------------------------------------------------------- */
  @FXML
  private void viewByWeekHandler(ActionEvent event) {
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    if (viewByWeekRadioButton.isSelected()) {
      DataProvider.setWeeklyView();
    }

    // Lambda: A lambda is used to bind columns to values.
    // A lambda can be used if PropertyValueFactory cannot be used the row is an ArrayList instead of an object,
    // and therefore does not have a property getter.
    customerNameColumn.setCellValueFactory(
      appointment -> 
        new SimpleStringProperty(appointment.getValue().getCustomerName())
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

    // Change table view depending on selected radio button
    if (viewByWeekRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByWeek());
    } else if (viewByMonthRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByMonth());
    } else if (viewByAllRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void viewByMonthHandler(ActionEvent event) {
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    if (viewByMonthRadioButton.isSelected()) {
      DataProvider.setMonthlyView();
    }

    customerNameColumn.setCellValueFactory(
      appointment ->
        new SimpleStringProperty(appointment.getValue().getCustomerName())
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

    if (viewByWeekRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByWeek());
    } else if (viewByMonthRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByMonth());
    } else if (viewByAllRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void viewByAllHandler(ActionEvent event) {
    DataProvider.getAllAppointmentsTableList().clear();
    DataProvider.getAppointmentsByMonth().clear();
    DataProvider.getAppointmentsByWeek().clear();

    DataProvider populateAppointments = new DataProvider();
    populateAppointments.populateAppointmentTable();

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

    if (viewByWeekRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByWeek());
    } else if (viewByMonthRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAppointmentsByMonth());
    } else if (viewByAllRadioButton.isSelected()) {
      appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void appointmentTableAddHandler(ActionEvent event)
    throws IOException {
    Parent parent = FXMLLoader.load(
      getClass().getResource("AddAppointment.fxml")
    );
    Scene scene = new Scene(parent);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.hide();
    stage.setScene(scene);
    stage.show();
  }

  /* -------------------------------------------------------------- */
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

  /* -------------------------------------------------------------- */
  @FXML
  private void deleteAppointmentHandler(ActionEvent event) {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "Are you sure you want to delete this customer and its data?"
    );

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      // Get selected row (Appointment object)
      selectedAppointment =
        appointmentTableView.getSelectionModel().getSelectedItem();

      // Run SQL command to delete appointment record
      deleteAppointment(selectedAppointment);
    }
  }

  /* -------------------------------------------------------------- */
  public void deleteAppointment(Appointment appointment) {
    int selectedID = appointment.getAppointmentId();

    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String deleteCustomer =
        "DELETE FROM appointment WHERE appointmentId = " + selectedID;
      int deletedCustomer = statement.executeUpdate(deleteCustomer);

      if (deletedCustomer > 0) {
        DataProvider.getAllAppointmentsTableList().remove(appointment);
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
  private void backHandler(ActionEvent event) throws IOException {
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
