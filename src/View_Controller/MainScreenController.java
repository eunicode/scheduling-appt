/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class MainScreenController implements Initializable {

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Show upcoming appointment alert at dashboard
    Appointment appt = new Appointment();
    appt.upcomingAppointmentAlert();
  }

  @FXML
  private void customerHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/CustomerScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  private void appointmentHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/AppointmentScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  void reportHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/ReportScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  private void dashboardLogHandler(ActionEvent event) throws IOException {
    String filename = "user_login_log.txt";
    File file = new File(filename);
    Desktop desktop = Desktop.getDesktop();
    desktop.open(file);
  }

  @FXML
  private void logoutButtonHandler(ActionEvent event) throws IOException {
    // Back to login screen
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/LoginScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
