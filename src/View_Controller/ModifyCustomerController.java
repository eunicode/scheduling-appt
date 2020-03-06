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
import java.sql.ResultSet;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class ModifyCustomerController implements Initializable {
  @FXML
  private TextField customerEditName;

  @FXML
  private TextField customerEditAddress;

  @FXML
  private TextField customerEditZipCode;

  @FXML
  private TextField customerEditPhone;

  @FXML
  private TextField customerEditCity;

  @FXML
  private TextField customerEditCountry;

  @FXML
  private Button saveCustomerEditButton;

  @FXML
  private Button cancelCustomerEditButton;

  Customer selectedCustomer;
  int selectedIdx;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @FXML
  void saveModifiedCustomerHandler(ActionEvent event)
    throws IOException, SQLException {
    int customerId = selectedCustomer.getCustomerID();
    // Initialize variables (will overwritten later)
    int customerCountryId = 1;
    int customerCity = 1;
    int addressId = 1;

    // Get modified user input
    String customerName = customerEditName.getText();
    String customerAddress = customerEditAddress.getText();
    String customerCityChoice = customerEditCity.getText();
    String customerCountry = customerEditCountry.getText();
    String customerZipCode = customerEditZipCode.getText();
    String customerPhone = customerEditPhone.getText();

    // Validate modified user input
    AddCustomerController getValidateFxns = new AddCustomerController();

    if (!getValidateFxns.checkCustomerName(customerName)) {
      return;
    } else if (!getValidateFxns.checkAddress(customerAddress)) {
      return;
    } else if (!getValidateFxns.checkCity(customerCityChoice)) {
      return;
    } else if (!getValidateFxns.checkCountry(customerCountry)) {
      return;
    } else if (!getValidateFxns.checkZipcode(customerZipCode)) {
      return;
    } else if (!getValidateFxns.checkPhone(customerPhone)) {
      return;
    }

    try {
      Customer customer = new Customer(
        customerId,
        customerName,
        customerAddress,
        customerCityChoice,
        customerCountry,
        customerZipCode,
        customerPhone
      );

      DataProvider.getCustomersAllList().set(selectedIdx, customer);

      // Create Statement objects
      Statement statement = DBConnection.getConnection().createStatement();
      Statement statement2 = DBConnection.getConnection().createStatement();

      // Get selected customer's addressId
      ResultSet selectCustomer = statement.executeQuery(
        "SELECT * FROM customer where customerId= " + customerId + ""
      );

      // If a row is returned
      if (selectCustomer.next()) {
        addressId = selectCustomer.getInt("addressId");
      }

      // Check if new country exists in country table
      ResultSet countryIdRS = statement.executeQuery(
        "SELECT countryId from country " +
        "WHERE country = " +
        "'" +
        customerCountry +
        "'"
      );

      // Find max countryId
      ResultSet countryIdMaxRS = statement2.executeQuery(
        "SELECT MAX(countryId) FROM country"
      );

      // If new country exists in country table, use existing countryId key
      if (countryIdRS.next()) {
        customerCountryId = countryIdRS.getInt(1);
      }
      // Else create new unique countryId key
      else {
        countryIdMaxRS.next();
        customerCountryId = countryIdMaxRS.getInt(1);
        customerCountryId += 1;

        // Insert new country into country table
        String countryInsertQuery =
          "INSERT INTO country SET countryId=" +
          customerCountryId +
          ", " +
          "country='" +
          customerCountry +
          "'" +
          ", " +
          "createDate=NOW(), createdBy='test', lastUpdate=NOW(), lastUpdateBy='test'";
        statement.executeUpdate(countryInsertQuery);
      }

      // Check if new city exists in city table
      ResultSet cityResultSet = statement.executeQuery(
        "SELECT cityId FROM city " +
        "WHERE city = " +
        "'" +
        customerCityChoice +
        "'"
      );

      // Find max cityId
      ResultSet cityResultSetMax = statement2.executeQuery(
        "SELECT MAX(cityId) FROM city"
      );

      // If city exists in city table, use existing cityId key
      if (cityResultSet.next()) {
        customerCity = cityResultSet.getInt(1); // statement1
      }
      // Else create a new unique cityId key
      else {
        cityResultSetMax.next(); // Call next() to move to row 1
        customerCity = cityResultSetMax.getInt(1); // statement2
        customerCity += 1;

        // Insert new city into city table
        String cityInsertQuery =
          "INSERT INTO city (cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) " +
          "VALUES(" +
          customerCity +
          ", " +
          "'" +
          customerCityChoice +
          "', " +
          "(SELECT countryId FROM country WHERE country=" +
          "'" +
          customerCountry +
          "'" +
          "), " +
          "createDate=NOW(), createdBy='test', lastUpdate=NOW(), lastUpdateBy='test')";
        statement.executeUpdate(cityInsertQuery);
      }

      // Update Address
      String updateAddress =
        "UPDATE address SET " +
        "address = '" +
        customerAddress +
        "', " +
        "cityId = " +
        customerCity +
        ", " +
        "postalCode = '" +
        customerZipCode +
        "', phone = '" +
        customerPhone +
        "' WHERE addressID = " +
        addressId;
      int updatedAddress = statement.executeUpdate(updateAddress);

      // Update Customer
      String updateCustomer =
        "UPDATE customer SET customerName = '" +
        customerName +
        "' WHERE customerId = " +
        customerId +
        "";

      int updatedCustomer = statement.executeUpdate(updateCustomer);
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
    }

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/CustomerTable.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void cancelModifiedHandler(ActionEvent event) throws IOException {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "You will lose any changes. Continue?"
    );

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/CustomerTable.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    }
  }

  /* -------------------------------------------------------------- */
  // Fill in form w/ previously saved data
  public void setCustomer(Customer customer, int index) {
    selectedCustomer = customer;
    selectedIdx = index;

    Customer newCustomer = (Customer) customer;

    this.customerEditName.setText(newCustomer.getCustomerName());
    this.customerEditAddress.setText((newCustomer.getAddress()));
    this.customerEditCity.setText((newCustomer.getCity()));
    this.customerEditCountry.setText((newCustomer.getCountry()));
    this.customerEditZipCode.setText((newCustomer.getPostalCode()));
    this.customerEditPhone.setText((newCustomer.getPhone()));
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
getSelectionModel()
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableView.html#getSelectionModel--
Gets the value of the property selectionModel.
Property description:
The SelectionModel provides the API through which it is possible to select single or multiple items within a TableView, as well as inspect which items have been selected by the user.

Getting selected item from a JavaFX TableView
https://stackoverflow.com/questions/17388866/getting-selected-item-from-a-javafx-tableview

--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
