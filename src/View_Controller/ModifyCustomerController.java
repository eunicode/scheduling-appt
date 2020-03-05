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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.xml.validation.ValidatorHandler;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class ModifyCustomerController implements Initializable {
  @FXML
  private TextField modifyCustomerText;

  @FXML
  private TextField modifyCustomerAddressText;

  @FXML
  private TextField modifyCustomerZipCodeText;

  @FXML
  private TextField modifyCustomPhoneText;

  @FXML
  private TextField modifyCustomerCityText;

  // private ComboBox<String> modifyCustomerCityText;

  @FXML
  private TextField modifyCustomerCountryText;

  // private Label modifyCustomerCountryText;

  @FXML
  private Button saveModifiedustomerButton;

  @FXML
  private Button cancelModifiedButton;

  Customer selectedCustomer;
  int selectedIndex;

  /**
   * Initializes the controller class.
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
    String customerName = modifyCustomerText.getText();
    String customerAddress = modifyCustomerAddressText.getText();
    String customerCityChoice = modifyCustomerCityText.getText();
    String customerCountry = modifyCustomerCountryText.getText();
    String customerZipCode = modifyCustomerZipCodeText.getText();
    String customerPhone = modifyCustomPhoneText.getText();

    // Validate modified user input
    AddCustomerController validate = new AddCustomerController();

    if (!validate.validateCustomerName(customerName)) {
      return;
    } else if (!validate.validateAddress(customerAddress)) {
      return;
    } else if (!validate.validateCity(customerCityChoice)) {
      return;
    } else if (!validate.validateCountry(customerCountry)) {
      return;
    } else if (!validate.validateZipcode(customerZipCode)) {
      return;
    } else if (!validate.validatePhone(customerPhone)) {
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

      DataProvider.getAllCustomersTableList().set(selectedIndex, customer);

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
      ResultSet countryResultSet = statement.executeQuery(
        "SELECT countryId from country " +
        "WHERE country = " +
        "'" +
        customerCountry +
        "'"
      );

      // Find max countryId
      ResultSet countryResultSetMax = statement2.executeQuery(
        "SELECT MAX(countryId) FROM country"
      );

      // If new country exists in country table, use existing countryId key
      if (countryResultSet.next()) {
        customerCountryId = countryResultSet.getInt(1);
      }
      // Else create new unique countryId key
      else {
        countryResultSetMax.next();
        customerCountryId = countryResultSetMax.getInt(1);
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
    selectedIndex = index;

    Customer newCustomer = (Customer) customer;

    this.modifyCustomerText.setText(newCustomer.getCustomerName());
    this.modifyCustomerAddressText.setText((newCustomer.getAddress()));
    // this.modifyCustomerCityText.setItems(cityIDList);
    this.modifyCustomerCityText.setText((newCustomer.getCity()));
    this.modifyCustomerCountryText.setText((newCustomer.getCountry()));
    this.modifyCustomerZipCodeText.setText((newCustomer.getPostalCode()));
    this.modifyCustomPhoneText.setText((newCustomer.getPhone()));
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
