/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Utilities.DBConnection;
import Utilities.trackLoggedInUser;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class LoginScreenController implements Initializable {
  @FXML
  private Label loginTitle;

  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordTextField;

  @FXML
  private Button submitButton;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label passwordLabel;

  private static String loggedInUser;

  /**
   * Initializes the Locale on Login screen.
   * @return
   */

  public static Locale getLocale() {
    return Locale.getDefault();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    ResourceBundle userLanguage = ResourceBundle.getBundle(
      "scheduler/Nat",
      Locale.getDefault()
    );

    loginTitle.setText(userLanguage.getString("title"));
    usernameLabel.setText(userLanguage.getString("username"));
    passwordLabel.setText(userLanguage.getString("password"));
    submitButton.setText(userLanguage.getString("login"));
  }

  @FXML
  private void submitButonHandler(ActionEvent event)
    throws IOException, Exception {
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
      } else if (getLocale().toString().equals("ko_KR")) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "사용자 이름 및 / 또는 비밀번호가 잘못되었습니다."
        );
        alert.showAndWait();

        trackLoggedInUser.trackLog(username, false);
      }
    }
  }

  public static Boolean attemptLogin(String username, String password)
    throws Exception {
    try {
      // First connection in program
      Statement statement = DBConnection.makeConnection().createStatement();

      ResultSet userListRS = statement.executeQuery("SELECT * FROM user");

      // If (username, pw) matches a row in user table, log in
      while (userListRS.next()) {
        if (
          userListRS.getString("userName").equals(username) &&
          userListRS.getString("password").equals(password)
        ) return true;
      }

      return false;
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return false;
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
