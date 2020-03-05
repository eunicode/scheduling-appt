/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

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

  public ObservableList<Appointment> appointmentsForCustomer = FXCollections.observableArrayList();

  // Constructor
  public Customer() {} // no args

  // Constructor overloading
  public Customer( // 7 params
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

  // Constructor overloading
  public Customer( // 9 params
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

  /* -------------------------------------------------------------- */
  // Getters
  public int getCustomerID() {
    return customerID;
  }

  public String getCustomerName() {
    return customerName;
  }

  public String getAddress() {
    return address;
  }

  public String getAddress2() {
    return address2;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return Country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public int getActive() {
    return active;
  }

  /* -------------------------------------------------------------- */
  // Setters
  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }
  
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public void setActive(int active) {
    this.active = active;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String Country) {
    this.Country = Country;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void addAppointment(Appointment appointment) {
    appointmentsForCustomer.add(appointment);
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
