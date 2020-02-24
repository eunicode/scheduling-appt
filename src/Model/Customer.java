/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Database.DBConnection;
import View_Controller.LoginScreenController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eunice
 */

public class Customer {
  private int customerID;
  private String customerName;
  private int active;
  private String address;
  private String address2;
  private String city;
  private String Country;
  private String postalCode;
  private String phone;

  public ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();

  public Customer() {}

  public Customer(
    int customerID,
    String customerName,
    String address,
    String city,
    String country,
    String postalCode,
    String phone
  ) {
    this.customerID = customerID;
    this.customerName = customerName;
    this.address = address;
    this.city = city;
    this.Country = country;
    this.postalCode = postalCode;
    this.phone = phone;
  }

  public Customer(
    int customerID,
    String customerName,
    int active,
    String address,
    String address2,
    String city,
    String Country,
    String postalCode,
    String phone
  ) {
    this.customerID = customerID;
    this.customerName = customerName;
    this.active = active;
    this.address = address;
    this.address2 = address2;
    this.city = city;
    this.Country = Country;
    this.postalCode = postalCode;
    this.phone = phone;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public int getActive() {
    return active;
  }

  public void setActive(int active) {
    this.active = active;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return Country;
  }

  public void setCountry(String Country) {
    this.Country = Country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public ObservableList<Appointment> getassociatedAppointments() {
    return associatedAppointments;
  }

  public void addAppointment(Appointment appointment) {
    associatedAppointments.add(appointment);
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
