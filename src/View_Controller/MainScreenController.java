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
  @FXML
  private Button customerTableButton;

  @FXML
  private Button appointmentTableButton;

  @FXML
  private Button logoutButton;

  @FXML
  private Button reportTableButton;

  @FXML
  private Label welcomeLabel;

  @FXML
  private Label optionsLabel;

  @FXML
  private Label customerLabel;

  @FXML
  private Label appointmentLabel;

  @FXML
  private Label reportLabel;

  @FXML
  private Label customerLabel1;

  @FXML
  private Label appointmentLabel1;

  @FXML
  private Label appointmentLabel2;

  @FXML
  private Label customerLabel2;

  @FXML
  private Label reportLabel1;

  @FXML
  private Label reportLabel2;

  @FXML
  private Label reportLabel3;

  @FXML
  private Label reportLabel4;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Appointment checkAppointments = new Appointment();
    checkAppointments.setAppointmentAlert();
  }

  @FXML
  private void customerTableHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/CustomerTable.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  private void appointmentTableHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/AppointmentScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  void reportTableHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/ReportScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @FXML
  private void dashboardLogButtonHandler(ActionEvent event) throws IOException {
    String filename = "user_login_log.txt";
    File file = new File(filename);
    Desktop desktop = Desktop.getDesktop();
    desktop.open(file);
  }

  @FXML
  private void logoutHandler(ActionEvent event) throws IOException {
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
