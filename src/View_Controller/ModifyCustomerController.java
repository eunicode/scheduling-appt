/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Database.DBConnection;
import Model.Customer;
import Model.DataProvider;
import java.io.IOException;
import java.net.URL;
//import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
//import java.util.ResourceBundle;
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
// import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

  // ObservableList<String> cityIDList = FXCollections.observableArrayList(
  //   "Phoenix, Arizona",
  //   " New York, New York",
  //   "London, England"
  // );

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  // @FXML
  // private void modifyCustomerCityHandler(ActionEvent event) {
  //   int customerCity =
  //     modifyCustomerCityText.getSelectionModel().getSelectedIndex() + 1;

  //   if (customerCity == 1 || customerCity == 2) {
  //     modifyCustomerCountryText.setText("United States");
  //   }

  //   if (customerCity == 3) {
  //     modifyCustomerCountryText.setText("United Kingdom");
  //   }
  // }

  // @FXML
  // private void modifyCustomerCountryHandler(MouseEvent event) {}

  @FXML
  void saveModifiedCustomerHandler(ActionEvent event)
    throws IOException, SQLException {
    int addressId = 0;
    int customerId = selectedCustomer.getCustomerID();

    try {
      // String customerCityChoice = modifyCustomerCityText
      //   .getSelectionModel()
      //   .getSelectedItem();
      String customerCityChoice = modifyCustomerCityText.getText();
      String customerName = modifyCustomerText.getText();
      String customerAddress = modifyCustomerAddressText.getText();
      String customerCountry = modifyCustomerCountryText.getText();
      String customerZipCode = modifyCustomerZipCodeText.getText();
      String customerPhone = modifyCustomPhoneText.getText();

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

      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet selectCustomer = statement.executeQuery(
        "SELECT * FROM customer where customerId= " + customerId + ""
      );

      while (selectCustomer.next()) {
        addressId = selectCustomer.getInt("addressId");
        System.out.println(addressId);
      }

      String updateAddress =
        "UPDATE address SET address = '" +
        customerAddress +
        "', postalCode = '" +
        customerZipCode +
        "', phone = '" +
        customerPhone +
        "' WHERE addressID = " +
        addressId;
      int updatedAddress = statement.executeUpdate(updateAddress);

      String updateCustomer =
        "UPDATE customer SET customerName = '" +
        customerName +
        "' WHERE customerId = " +
        customerId +
        "";
      int updatedCustomer = statement.executeUpdate(updateCustomer);
    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning Dialog");
      alert.setContentText("Please enter a valid value for each text field.");
      alert.showAndWait();
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
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
