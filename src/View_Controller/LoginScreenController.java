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
  private TextField loginUsername;

  @FXML
  private TextField loginPassword;

  @FXML
  private Button loginButton;

  @FXML
  private Label loginUsernameLabel;

  @FXML
  private Label loginPasswordLabel;

  private static String storeUser;

  /**
   * Initializes the Locale on Login screen.
     * @param url
     * @param rb
   */

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    ResourceBundle rbLang = ResourceBundle.getBundle(
      "scheduler/Nat",
      Locale.getDefault()
    );

    loginTitle.setText(rbLang.getString("title"));
    loginUsernameLabel.setText(rbLang.getString("username"));
    loginPasswordLabel.setText(rbLang.getString("password"));
    loginButton.setText(rbLang.getString("login"));
  }

  @FXML
  private void loginButtonHandler(ActionEvent event)
    throws IOException, Exception {
    String username = loginUsername.getText();
    String password = loginPassword.getText();

    // If (username, password) pair exists in database, show main screen
    if (checkUsernamePasswordPair(username, password)) {
      storeUser = username;

      AddAppointmentController appt = new AddAppointmentController();
      appt.findUserId(storeUser);

      // Go to dashboard screen
      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Object scene = FXMLLoader.load(
        getClass().getResource("/View_Controller/MainScreen.fxml")
      );
      stage.setScene(new Scene((Parent) scene));
      stage.show();

      trackLoggedInUser.trackLog(username, true);
    } 
    // Else show alerts
    else {
      if (Locale.getDefault().toString().equals("en_US")) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "The username and/or password is incorrect."
        );
        alert.showAndWait();

        trackLoggedInUser.trackLog(username, false);
      } else if (Locale.getDefault().toString().equals("ko_KR")) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "사용자 이름 및 / 또는 비밀번호가 잘못되었습니다."
        );
        alert.showAndWait();

        trackLoggedInUser.trackLog(username, false);
      }
    }
  }

  // Checks if (username, password) exists in database
  public static Boolean checkUsernamePasswordPair(String username, String password)
    throws Exception {
    try {
      // First connection in program - makeConnection
      Statement statement = DBConnection.makeConnection().createStatement();

      ResultSet userListRS = statement.executeQuery("SELECT * FROM user");

      // If (username, pw) matches a row in user table, return true
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
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
