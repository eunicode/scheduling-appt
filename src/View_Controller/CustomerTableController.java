/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import Utilities.DBConnection;
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

    customerTableView.getSelectionModel().selectFirst();

    customerIDTable.setCellValueFactory(
      new PropertyValueFactory<>("customerID")
    );

    //Lambda expression to populate customer table
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

  /* -------------------------------------------------------------- */
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

  /* -------------------------------------------------------------- */
  @FXML
  private void modifyButtonHandler(ActionEvent event) throws IOException {
    // Create FXMLLoader instance
    FXMLLoader loader = new FXMLLoader();
    // Find location of FXML file to load
    loader.setLocation(
      getClass().getResource("/View_Controller/ModifyCustomer.fxml")
    );
    loader.load();

    // Create controller
    ModifyCustomerController controller = loader.getController();

    Customer customer = customerTableView.getSelectionModel().getSelectedItem();

    int index = customerTableView.getSelectionModel().getSelectedIndex();
    controller.setCustomer(customer, index);

    stage = (Stage) customerTableModifyButton.getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void deleteButtonHandler(ActionEvent event) {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "Are you sure you want to delete this customer?"
    );

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      // Get selected Customer object
      selectedCustomer =
        customerTableView.getSelectionModel().getSelectedItem();
      // Run SQL command to delete customer record  
      deleteCustomer(selectedCustomer);

      // Refresh customer table
      DataProvider.getAllCustomersTableList().clear();
      customerTableView.setItems(DataProvider.getAllCustomersTableList());
      DataProvider populateCustomers = new DataProvider();
      populateCustomers.populateCustomerTable();
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void backButtonHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  public static void deleteCustomer(Customer customer) {
    // public static void deleteCustomer(int selectedID) {
    // ++selectedID;
    int selectedID = customer.getCustomerID();
    System.out.println(selectedID);

    try {
      Statement statement = DBConnection.getConnection().createStatement();

      // Delete rows in appointment table that reference customer-to-be-deleted
      String delApptQuery =
        "DELETE FROM appointment WHERE customerId = " + selectedID;

      statement.executeUpdate(delApptQuery);

      // Delete
      String deleteCustomer =
        "DELETE FROM customer WHERE customerId = " + selectedID;
      // "DELETE FROM customer WHERE addressId =" + selectedID;

      int deletedCustomer = statement.executeUpdate(deleteCustomer);

      // Delete deleted customer's address
      if (deletedCustomer == 1) {
        String deleteAddress =
          "DELETE FROM address WHERE addressId =" + selectedID;
        // "DELETE FROM address WHERE addressId =" + selectedID;

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
FXMLLoader getController returns NULL?
https://stackoverflow.com/questions/23461148/fxmlloader-getcontroller-returns-null
https://stackoverflow.com/questions/24429809/fxml-getcontroller-returns-null/24597719

--------------------------------------------------------------------
JavaFX: Accessing the Controller
https://noblecodemonkeys.com/javafx-accessing-the-controller/
https://riptutorial.com/javafx/example/8803/passing-data-to-fxml---accessing-existing-controller

--------------------------------------------------------------------
getController - FXMLLoader
https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/FXMLLoader.html#getController--
Returns the controller associated with the root object.
*/
/* -------------------------------------------------------------- */
