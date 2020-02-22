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

public class User {
  private int userID;
  private String userName;
  private String userPassword;
  private int active;
  public static ObservableList<Customer> allUsers = FXCollections.observableArrayList();

  public User() {}

  public User(int userID, String userName, String userPassword, int active) {
    this.userID = userID;
    this.userName = userName;
    this.userPassword = userPassword;
    this.active = active;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public int getActive() {
    return active;
  }

  public void setActive(int active) {
    this.active = active;
  }
}
