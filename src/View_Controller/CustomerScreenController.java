/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Model.DataProvider;
import Utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class CustomerScreenController implements Initializable {
  @FXML
  private TableView<Customer> customerTable;

  @FXML
  private TableColumn<Customer, Integer> customerIdCol;

  @FXML
  private TableColumn<Customer, String> customerNameCol;

  @FXML
  private TableColumn<Customer, String> customerAddressCol;

  @FXML
  private TableColumn<Customer, String> customerCityCol;

  @FXML
  private TableColumn<Customer, String> customerCountryCol;

  @FXML
  private TableColumn<Customer, String> customerZipCodeCol;

  @FXML
  private TableColumn<Customer, String> customerPhoneCol;

  @FXML
  private Button customerEditButton;

  Stage stage;
  Parent scene;
  private Customer selectedCustomer;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Populate customer table
    try {
      DataProvider.getCustomersAllList().clear();
    } catch (Exception e) {
      System.out.println("Error1: " + e.getMessage());
    }
    try {
      customerTable.setItems(DataProvider.getCustomersAllList());
    } catch(Exception e) {
      System.out.println("Error2: " + e.getMessage());
    }
    try {
      DataProvider.createCustomerObjectObservableList(); // investigate
    } catch (Exception e) {
      System.out.println("Error3: " + e.getMessage());
    }

    customerTable.getSelectionModel().selectFirst();

    // Bind columns to values
    customerIdCol.setCellValueFactory(
      new PropertyValueFactory<>("customerID")
    );
    customerNameCol.setCellValueFactory(
      new PropertyValueFactory<>("customerName")
    );
    customerAddressCol.setCellValueFactory(
      new PropertyValueFactory<>("address")
    );
    customerCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
    customerCountryCol.setCellValueFactory(
      new PropertyValueFactory<>("country")
    );
    customerZipCodeCol.setCellValueFactory(
      new PropertyValueFactory<>("postalCode")
    );
    customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void addButtonHandler(ActionEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene =
      FXMLLoader.load(
        getClass().getResource("/View_Controller/CustomerAdd.fxml")
      );
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void editButtonHandler(ActionEvent event) throws IOException {
    // Create FXMLLoader instance
    FXMLLoader loader = new FXMLLoader();
    // Find location of FXML file to load
    loader.setLocation(
      getClass().getResource("/View_Controller/CustomerEdit.fxml")
    );
    loader.load();

    // Create controller
    CustomerEditController controller = loader.getController();
    // Get selected customer object
    Customer customer = customerTable.getSelectionModel().getSelectedItem();

    int index = customerTable.getSelectionModel().getSelectedIndex();
    // Pass customer object and its index to edit customer screen
    controller.setCustomer(customer, index);

    stage = (Stage) customerEditButton.getScene().getWindow();
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
        customerTable.getSelectionModel().getSelectedItem();
      // Run SQL command to delete customer record
      deleteCustomer(selectedCustomer);

      // Refresh customer table
      DataProvider.getCustomersAllList().clear();
      customerTable.setItems(DataProvider.getCustomersAllList());
      DataProvider.createCustomerObjectObservableList();
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
    int selectedID = customer.getCustomerID();

    try {
      Statement statement = DBConnection.getConnection().createStatement();

      // Delete rows in appointment table that reference customer-to-be-deleted
      String delApptQuery =
        "DELETE FROM appointment WHERE customerId = " + selectedID;

      statement.executeUpdate(delApptQuery);

      // Delete
      String deleteCustomer =
        "DELETE FROM customer WHERE customerId = " + selectedID;

      int deletedCustomer = statement.executeUpdate(deleteCustomer);

      // Delete deleted customer's address
      if (deletedCustomer == 1) {
        String deleteAddress =
          "DELETE FROM address WHERE addressId =" + selectedID;
        // "DELETE FROM address WHERE addressId =" + selectedID;

        int deletedAddress = statement.executeUpdate(deleteAddress);

        if (deletedAddress == 1) {
          System.out.println("One customer and associated appointments were deleted from the database");
        }
      }
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
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
