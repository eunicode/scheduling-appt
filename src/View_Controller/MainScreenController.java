/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import static View_Controller.LoginScreenController.getLocale;

import Model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

  // @FXML
  // private Button exitButton;

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
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // ResourceBundle userLanguage;
    // Locale current = getLocale();
    // userLanguage =
    //   ResourceBundle.getBundle("scheduler/Nat", current);

    // welcomeLabel.setText(userLanguage.getString("welcomeMain"));
    // customerLabel1.setText(userLanguage.getString("customer"));
    // customerTableButton.setText(userLanguage.getString("customerButton"));
    // customerLabel1.setText(userLanguage.getString("customerText1"));
    // customerLabel2.setText(userLanguage.getString("customerText2"));
    // appointmentTableButton.setText(userLanguage.getString("appointmentButton"));
    // appointmentLabel.setText(userLanguage.getString("appointment"));
    // appointmentLabel1.setText(userLanguage.getString("appointmentText1"));
    // appointmentLabel2.setText(userLanguage.getString("appointmentText2"));
    // reportLabel.setText(userLanguage.getString("generateReport"));
    // reportTableButton.setText(userLanguage.getString("generateButton"));
    // reportLabel1.setText(userLanguage.getString("generateText1"));
    // reportLabel2.setText(userLanguage.getString("generateText2"));
    // reportLabel3.setText(userLanguage.getString("generateText3"));
    // reportLabel4.setText(userLanguage.getString("generateText4"));
    // logoutButton.setText(userLanguage.getString("logout"));
    // exitButton.setText(userLanguage.getString("exit"));

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
    // Object scene = FXMLLoader.load(
    //   getClass().getResource("/View_Controller/GenerateReport.fxml")
    // );
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/ReportScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  // @FXML
  // private void exitHandler(ActionEvent event) {
  //   System.exit(0);
  // }

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
