/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Database.DBConnection;
import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import java.io.IOException;
import java.net.URL;
//import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
//import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class CustomerTableController implements Initializable {
  @FXML
  private TableView<Customer> customerTableView;

  @FXML
  private TableColumn<Customer, Integer> customerIDTable;

  @FXML
  private TableColumn<Customer, String> customerNameTable;

  @FXML
  private TableColumn<Customer, String> customerAddressTable;

  @FXML
  private TableColumn<Customer, String> customerCityTable;

  @FXML
  private TableColumn<Customer, String> customerCountryTable;

  @FXML
  private TableColumn<Customer, String> customerPostalCodeTable;

  @FXML
  private TableColumn<Customer, String> customerPhoneTable;

  @FXML
  private Button customerTableAddButton;

  @FXML
  private Button customerTableModifyButton;

  @FXML
  private Button customerTableDeleteButton;

  @FXML
  private Button backButton;

  @FXML
  private Button appointmentTableAddButton;

  Stage stage;
  Parent scene;
  private Customer selectedCustomer;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    DataProvider.getAllCustomersTableList().clear();
    customerTableView.setItems(DataProvider.getAllCustomersTableList());

    DataProvider populateCustomers = new DataProvider();
    populateCustomers.populateCustomerTable();

    //Lambda expression to populate customer table
    customerTableView.getSelectionModel().selectFirst();

    customerIDTable.setCellValueFactory(
      new PropertyValueFactory<>("customerID")
    );
    
    customerNameTable.setCellValueFactory(
      customer ->
        new SimpleStringProperty(customer.getValue().getCustomerName())
    );
    customerAddressTable.setCellValueFactory(
      customer -> new SimpleStringProperty(customer.getValue().getAddress())
    );
    customerCityTable.setCellValueFactory(
      customer -> new SimpleStringProperty(customer.getValue().getCity())
    );
    customerCountryTable.setCellValueFactory(
      customer -> new SimpleStringProperty(customer.getValue().getCountry())
    );
    customerPostalCodeTable.setCellValueFactory(
      customer -> new SimpleStringProperty(customer.getValue().getPostalCode())
    );
    customerPhoneTable.setCellValueFactory(
      customer -> new SimpleStringProperty(customer.getValue().getPhone())
    );
  }

  @FXML
  private void addButtonHandler(ActionEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene =
      FXMLLoader.load(
        getClass().getResource("/View_Controller/AddCustomer.fxml")
      );
    stage.setScene(new Scene(scene));
    stage.show();
  }

  @FXML
  private void modifyButtonHandler(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(
      getClass().getResource("/View_Controller/ModifyCustomer.fxml")
    );
    loader.load();
    
    ModifyCustomerController controller = loader.getController();
    
    Customer customer = customerTableView.getSelectionModel().getSelectedItem();

    int index = customerTableView.getSelectionModel().getSelectedIndex();
    controller.setCustomer(customer, index);

    stage = (Stage) customerTableModifyButton.getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  @FXML
  private void deleteButtonHandler(ActionEvent event) {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "This will delete the customer record, do you want to continue?"
    );
    alert.setTitle("Confirmation of Deletion");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      ObservableList<Customer> allCustomers, singleCustomer;
      allCustomers = customerTableView.getItems();
      singleCustomer = customerTableView.getSelectionModel().getSelectedItems();
      singleCustomer.forEach(allCustomers::remove);
      selectedCustomer =
        customerTableView.getSelectionModel().getSelectedItem();

      deleteCustomer(selectedCustomer.getCustomerID());
    }
  }

  @FXML
  void appointmentTableAddHandler(ActionEvent event)
    throws IOException, SQLException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
      getClass().getResource("/View_Controller/AddAppointment.fxml")
    );
    loader.load();
    AddAppointmentController controller = loader.getController();

    int customerIDTransfer = customerTableView
      .getSelectionModel()
      .getSelectedItem()
      .getCustomerID();

    DataProvider.setSelectedAppointmentsForCustomer(customerIDTransfer);
    AddAppointmentController setCustomer = new AddAppointmentController();
    setCustomer.setSelectedCustomerId(customerIDTransfer);

    stage = (Stage) customerTableModifyButton.getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  @FXML
  private void backButtonHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  public static void deleteCustomer(int selectedID) {
    ++selectedID;

    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String deleteCustomer =
        "DELETE FROM customer WHERE addressId =" + selectedID;
      int deletedCustomer = statement.executeUpdate(deleteCustomer);

      if (deletedCustomer == 1) {
        String deleteAddress =
          "DELETE FROM address WHERE addressId =" + selectedID;
        int deletedAddress = statement.executeUpdate(deleteAddress);

        if (deletedAddress == 1) {
          System.out.println(
            "Customer record was successfully deleted from the database!"
          );
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
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
