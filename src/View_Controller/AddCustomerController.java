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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class AddCustomerController implements Initializable {
  @FXML
  private TextField customerAddName;

  @FXML
  private TextField customerAddAddress;

  @FXML
  private TextField customerAddCity;

  @FXML
  private TextField customerAddCountry;

  @FXML
  private TextField customerAddZipcode;

  @FXML
  private TextField customerAddPhone;

  @FXML
  private Button saveCustomerAddButton;

  @FXML
  private Button cancelCustomerAddButton;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {}

  /* -------------------------------------------------------------- */
  private static String mySQLEscapeSingleQuote(String s) {
    String escapedString = s.replace("'", "''");
    return escapedString;
  }

    /* -------------------------------------------------------------- */
    public boolean checkCustomerName(String customerName) {
      if (customerName.isEmpty()) {
        Alert alert = new Alert(
          Alert.AlertType.WARNING,
          "Customer name is empty"
        );
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
  
    /* -------------------------------------------------------------- */
    public boolean checkAddress(String address) {
      if (address.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Address is empty");
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
  
    /* -------------------------------------------------------------- */
    public boolean checkCity(String city) {
      if (city.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "City is empty");
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
  
    /* -------------------------------------------------------------- */
    public boolean checkCountry(String country) {
      if (country.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Country is empty");
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
  
    /* -------------------------------------------------------------- */
    public boolean checkZipcode(String zipCode) {
      String pattern = "\\d{5}";
  
      if (zipCode.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Zip code is empty");
        alert.showAndWait();
        return false;
      } else if (!zipCode.matches(pattern)) {
        Alert alert = new Alert(
          Alert.AlertType.WARNING,
          "Zip code is invalid. Must be 5 digits"
        );
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
  
    /* -------------------------------------------------------------- */
    public boolean checkPhone(String phone) {
      String pattern = "\\d{3}-\\d{4}";
  
      if (phone.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Phone number is empty");
        alert.showAndWait();
        return false;
      } else if (!phone.matches(pattern)) {
        Alert alert = new Alert(
          Alert.AlertType.WARNING,
          "Phone number is invalid. Must be 000-0000 format"
        );
        alert.showAndWait();
        return false;
      } else {
        return true;
      }
    }
    
  /* -------------------------------------------------------------- */
  @FXML
  private void saveCustomerAddHandler(ActionEvent event) throws IOException {
    // Initialize values
    int customerId = 1;
    int customerCity = 1;
    int customerCountryId = 1;
    int addressId = 1;

    // Get user input
    String customerName = customerAddName.getText();

    String customerAddress = mySQLEscapeSingleQuote(
      customerAddAddress.getText()
    );

    String customerCityEsc = mySQLEscapeSingleQuote(
      customerAddCity.getText()
    );

    String customerCountry = customerAddCountry.getText();
    String customerZipCode = customerAddZipcode.getText();
    String customerPhone = customerAddPhone.getText();

    // Validate user input
    if (
      checkCustomerName(customerName) &&
      checkAddress(customerAddress) &&
      checkCity(customerCityEsc) &&
      checkCountry(customerCountry) &&
      checkZipcode(customerZipCode) &&
      checkPhone(customerPhone)
    ) { // If user input is valid add customer to customer table
      try {
        // Create Statement object
        Statement statement = DBConnection.getConnection().createStatement();
        // Create new Statement object for concurrent ResultSet
        Statement statement2 = DBConnection.getConnection().createStatement();

        // Find max customerId in customer table
        ResultSet customerIdRS = statement.executeQuery(
          "SELECT MAX(customerId) FROM customer"
        );

        // Use max customerId value to update new customer's customerId key
        if (customerIdRS.next()) { // Way to check if table is non-empty
          customerId = customerIdRS.getInt(1); // col 1
          customerId += 1;
        }

        // Find max addressId in address table
        ResultSet addressIdRS = statement.executeQuery(
          "SELECT MAX(addressId) FROM address"
        );

        // Use max addressId value to update new customer's addressId key
        if (addressIdRS.next()) {
          addressId = addressIdRS.getInt(1);
          addressId += 1;
        }

        // Check if country exists in country table
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
        // If country user input exists in country table, use existing countryId key
        if (countryIdRS.next()) {
          customerCountryId = countryIdRS.getInt(1); // Use statement1's resultset
        }
        // Else create new unique countryId key
        else {
          countryIdMaxRS.next();
          customerCountryId = countryIdMaxRS.getInt(1); // Use statement2's resultset
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

        // Check if city exists in city table
        ResultSet cityResultSet = statement.executeQuery(
          "SELECT cityId FROM city " +
          "WHERE city = " +
          "'" +
          customerCityEsc +
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
            customerCityEsc +
            "', " +
            "(SELECT countryId FROM country WHERE country=" +
            "'" +
            customerCountry +
            "'" +
            "), " +
            "createDate=NOW(), createdBy='test', lastUpdate=NOW(), lastUpdateBy='test')";
          statement.executeUpdate(cityInsertQuery);
        }

        // Update address table
        String addressSQL =
          "INSERT INTO address SET addressId=" +
          addressId +
          ", address='" +
          customerAddress +
          // customerAddressEscaped +
          "', address2='none', phone='" +
          customerPhone +
          "', postalCode='" +
          customerZipCode +
          "', cityId= " +
          customerCity +
          ", createDate=NOW(), createdBy='test', lastUpdate=NOW(), lastUpdateBy='test'";

        int checkInsertSuccess = statement.executeUpdate(addressSQL);

        // Update `Customer` table
        if (checkInsertSuccess == 1) {
          String customerSQL =
            "INSERT INTO customer SET customerId=" +
            customerId +
            ", customerName='" +
            customerName +
            "', addressId=" +
            addressId +
            ", active=1, createDate=NOW(), createdBy='test', lastUpdate=NOW(), lastUpdateBy='test'";

          int checkInsertSuccessCustomer = statement.executeUpdate(customerSQL);

          if (checkInsertSuccessCustomer == 1) {
            System.out.println("One row was inserted into customer table");
          }
        }
      } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
      }

      // Add customer to DataProvider
      Customer customer = new Customer(
        customerId,
        customerName,
        customerAddress,
        customerCityEsc,
        customerCountry,
        customerZipCode,
        customerPhone
      );
      DataProvider.addCustomer(customer);

      // Return to customer table
      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/CustomerTable.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();
    }
  }

  /* -------------------------------------------------------------- */
  @FXML
  private void cancelCustomerAddHandler(ActionEvent event) throws IOException {
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "You will lose all changes. Continue?"
    );

    // Alert - The traditional + Optional API approach
    alert
      .showAndWait()
      .ifPresent(
        response -> {
          if (response == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene()
              .getWindow();
            Object scene = null;
            try {
              scene =
                FXMLLoader.load(
                  getClass().getResource("/View_Controller/CustomerTable.fxml")
                );
            } catch (IOException ex) {
              Logger
                .getLogger(AddCustomerController.class.getName())
                .log(Level.SEVERE, null, ex);
            }
            stage.setScene(new Scene((Parent) scene));
            stage.show();
          }
        }
      );
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
TEMPLATE LITERALS

string concatenation
StringBuilder
Formatted text - String.format()

--------------------------------------------------------------------
How to escape single quotes in MySQL
https://stackoverflow.com/questions/887036/how-to-escape-single-quotes-in-mysql/19819265

--------------------------------------------------------------------
Processing SQL Statements with JDBC
https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html

--------------------------------------------------------------------
Connection interface
https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html

A connection (session) with a specific database. 
SQL statements are executed and results are returned within the context of a connection.

--------------------------------------------------------------------
createStatement() - Connection
Creates a Statement object for sending SQL statements to the database.

--------------------------------------------------------------------
Statement interface
https://docs.oracle.com/javase/8/docs/api/java/sql/Statement.html

The object used for executing a static SQL statement and returning the results it produces.

--------------------------------------------------------------------
executeUpdate() - Statement
https://docs.oracle.com/javase/8/docs/api/java/sql/Statement.html#executeUpdate-java.lang.String-

Executes the given SQL statement
Returns
- the row count for DML statements 
- 0 for SQL statements that return nothing

--------------------------------------------------------------------
ResultSet interface
https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html

A table of data representing a database result set, which is usually generated by executing a statement that queries the database.

--------------------------------------------------------------------
getInt()
https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#getInt-int-

getInt(int columnIndex)

Returns: the column value; if the value is SQL NULL, the value returned is 0

next()
https://www.tutorialspoint.com/java-resultset-next-method-with-example

Call 1 = row 1
Call 2 = row 2
Returns: true if the new current row is valid; false if there are no more rows

--------------------------------------------------------------------
Error Operation not allowed after ResultSet closed

--------------------------------------------------------------------
Java Regular Expressions to Validate phone numbers
https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers/42105140

--------------------------------------------------------------------
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
*/
/* -------------------------------------------------------------- */
