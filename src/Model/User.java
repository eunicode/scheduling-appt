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

  public static ObservableList<Customer> allUsers = FXCollections.observableArrayList();

  public User() {}

  public User(int userID, String userName, String userPassword, int active) {
    this.userID = userID;
    this.userName = userName;
    this.userPassword = userPassword;
  }

  /* -------------------------------------------------------------- */
  // Getters
  public int getUserID() {
    return userID;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserPassword() {
    return userPassword;
  }

  /* -------------------------------------------------------------- */
  // Setters
  public void setUserID(int userID) {
    this.userID = userID;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
