/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import Database.DBConnection;
import Model.Customer;
import Model.DataProvider;
import java.io.IOException;
//import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
//import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class AddCustomerController implements Initializable {

    @FXML
    private TextField addCustomerText;
    @FXML
    private TextField addCustomerAddressText;
    @FXML
    private Label addCustomerCountryText;
    @FXML
    private TextField addCustomerZipCodeText;
    @FXML
    private TextField addCustomPhoneText;
    @FXML
    private ComboBox<String> addCustomerCityComboBox;
    @FXML
    private Button saveCustomerButton;
    @FXML
    private Button cancelButton;

    ObservableList<String> cityIDList = FXCollections.observableArrayList("Phoenix, Arizona", " New York, New York", "London, England");
    ObservableList<String> countryIDList = FXCollections.observableArrayList("United States", "United Kingdom");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        addCustomerCityComboBox.setItems(cityIDList);
    }    

    @FXML
    private void addCustomerCityHandler(ActionEvent event) {
        
            int customerCity = addCustomerCityComboBox.getSelectionModel().getSelectedIndex() + 1;
            
            if(customerCity == 3) {
                addCustomerCountryText.setText("United Kingdom");
            }
    }

    @FXML
    private void saveCustomerHandler(ActionEvent event) throws IOException {
        
        int customerID = 1;
        
        for(Customer customer : DataProvider.allCustomersTableList) {

            if(customer.getCustomerID() > customerID)

                customerID = customer.getCustomerID();

        }
        
        int addressId = 1;
        int customerId = customerID;
        int customerCity = addCustomerCityComboBox.getSelectionModel().getSelectedIndex() + 1;
        String customerCityChoice = addCustomerCityComboBox.getSelectionModel().getSelectedItem();
            
        customerID = ++customerID; 
        String customerName = addCustomerText.getText();
        String customerAddress = addCustomerAddressText.getText();
        String customerCityChoiceValue = customerCityChoice;
        String customerCountry = addCustomerCountryText.getText();
        String customerZipCode = addCustomerZipCodeText.getText();
        String customerPhone = addCustomPhoneText.getText();  
        
        try {
            
  

            Statement statement = DBConnection.getConnection().createStatement();
            
            ResultSet addressResultSet = statement.executeQuery("SELECT MAX(addressId) FROM address");
            if(addressResultSet.next()){
                addressId = addressResultSet.getInt(1);
                addressId++;
                }
                
            ResultSet customerResultSet = statement.executeQuery("SELECT MAX(customerId) FROM customer");
            if(customerResultSet.next()){
                customerId = customerResultSet.getInt(1);
                customerId++;
                }
            
            String addressQuery = "INSERT INTO address SET addressId=" + addressId + ", address='" 
                    + customerAddress + "', address2='none', phone='" + customerPhone + "', postalCode='" + customerZipCode + "', cityId= " 
                    + customerCity + ", createDate=NOW(), createdBy='', lastUpdate=NOW(), lastUpdateBy=''";
            
            int addressExecuteUpdate = statement.executeUpdate(addressQuery);
            
            if(addressExecuteUpdate == 1) {
           
                String customerQuery = "INSERT INTO customer SET customerId="+customerId+", customerName='" 
                    + customerName + "', addressId=" + addressId +", active=1, createDate=NOW(), createdBy='', lastUpdate=NOW(), lastUpdateBy=''";
            
                int customerExecuteUpdate = statement.executeUpdate(customerQuery);
            
                if(customerExecuteUpdate == 1) {
                System.out.println("Insert into SQL table was successful!");
                }
            }     
        }
        
        catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        }

        catch(NumberFormatException e){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();
            
        }
        
            if ((validateCustomerName(customerName) && validateAddress(customerAddress)) && (validateZipcode(customerZipCode) && validatePhone(customerPhone))) {
            
                Customer customer = new Customer(customerID, customerName, customerAddress, customerCityChoiceValue, customerCountry, customerZipCode, customerPhone);
                DataProvider.addCustomer(customer);
        
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomerTable.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
            
    }

    @FXML
    private void cancelHandler(ActionEvent event) throws IOException {
            
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field vlaues, do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomerTable.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
    
    public static int getID (int ID) {
        return ID;
    }
    
    public boolean validateCustomerName(String customerName) {
        
        if(customerName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a name for this customer!");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean validateAddress (String address) {
                
        if(address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid address for this customer!");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
        
    }
    
    public boolean validateZipcode (String zipCode) {
        
        if(zipCode.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid zip code for this customer!");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
        
    }
    
    public boolean validatePhone (String phone) {
           
        if(phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid phone number for this customer!");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }    
    }
    
}