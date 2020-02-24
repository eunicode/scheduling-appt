/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Database.DBConnection;
import Database.DBQuery;
import Database.trackLoggedInUser;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
//import java.io.IOException;
//import java.net.URL;
//import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;

import java.util.Locale;
import java.util.ResourceBundle;
//import java.util.Locale;
//import java.util.ResourceBundle;

import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class LoginScreenController implements Initializable {
  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordTextField;

  @FXML
  private Button submitButton;

  @FXML
  private Label welcomeMessageLabel;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label passwordLabel;

  private static String loggedInUser;

  /**
   * Initializes the Locale on Login screen.
   */

   public static Locale getLocale() {
     return Locale.getDefault();
   }

  // Locale[] localeLanguages = { Locale.ENGLISH, Locale.KOREAN };

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // ResourceBundle userLanguage; // rb
    // Locale current = getLocale();
    // userLanguage =
    //   ResourceBundle.getBundle("scheduler/Nat", current);
    ResourceBundle userLanguage = ResourceBundle.getBundle("scheduler/Nat");

    welcomeMessageLabel.setText(userLanguage.getString("welcome"));
    usernameLabel.setText(userLanguage.getString("username"));
    passwordLabel.setText(userLanguage.getString("password"));
  }

  @FXML
  private void submitButonHandler(ActionEvent event) throws IOException {
    String username = usernameTextField.getText();
    String password = passwordTextField.getText();

    boolean correctCredentials = attemptLogin(username, password);

    if (correctCredentials) {
      loggedInUser = username;

      AddAppointmentController user = new AddAppointmentController();
      user.setSelectedUserId(loggedInUser);

      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/MainScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();

      trackLoggedInUser.trackLog(username, true);
    } else {
      if (getLocale().toString().equals("en_US")) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "The username and/or password is incorrect."
        );
        alert.showAndWait();

        trackLoggedInUser.trackLog(username, false);
      }

      if (getLocale().toString().equals("ko_KR")) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "사용자 이름 및 / 또는 비밀번호가 잘못되었습니다."
        );
        alert.showAndWait();

        trackLoggedInUser.trackLog(username, false);
      }
    }
  }

  public static Boolean attemptLogin(String username, String password) {
    try {
      String SQLLogin = "SELECT * FROM user";
      DBQuery.makeQuery(SQLLogin);
      ResultSet queryResult = DBQuery.getResult();

      while (queryResult.next()) {
        if (
          queryResult.getString("userName").equals(username) &&
          queryResult.getString("password").equals(password)
        ) return true;
      }

      return false;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return false;
    }
  }

  public Locale getUserLocale() {
    return Locale.getDefault();
  }

  public static String getLoggedInUser() {
    return loggedInUser;
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
