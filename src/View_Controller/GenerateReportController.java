/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Database.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

//import java.io.IOException;
//import java.net.URL;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Collections;
//import java.util.ResourceBundle;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class GenerateReportController implements Initializable {
  @FXML
  private ComboBox<String> selectReportComboBox;

  @FXML
  private TextArea typesOfMonthsText;

  @FXML
  private TextArea scheduleOfConsultantText;

  @FXML
  private TextArea totalAppointmentsThisYearText;

  @FXML
  private Button generateReportButton;

  @FXML
  private Button backButton;

  ObservableList<String> selectedReport = FXCollections.observableArrayList(
    "Number of appointment types by month",
    "Schedule of each consultant",
    "Total number of appointments this year"
  );

  /**
   * Initializes the controller class.
   */

  public void generateFirstReport() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String appointmentTypeQuery =
        "SELECT description, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(START)";

      ResultSet appointmentQueryResults = statement.executeQuery(
        appointmentTypeQuery
      );

      StringBuilder parseReport = new StringBuilder();

      parseReport.append(
        String.format(
          "%1$-60s %2$-60s %3$s \n",
          "Month",
          "Appointment Type",
          "Total"
        )
      );
      parseReport.append(String.join("", Collections.nCopies(163, "-")));
      parseReport.append("\n");

      while (appointmentQueryResults.next()) {
        parseReport.append(
          String.format(
            "%1$-56s %2$-60s %3$s \n",
            appointmentQueryResults.getString("Month"),
            appointmentQueryResults.getString("description"),
            appointmentQueryResults.getInt("Total")
          )
        );
      }

      typesOfMonthsText.setText(parseReport.toString());
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  public void generateSecondReport() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();
      String consultantQueryResults =
        "SELECT appointment.contact, appointment.description, customer.customerName, start, end " +
        "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
        "GROUP BY appointment.contact, MONTH(start), start";

      ResultSet appointmentQueryResults = statement.executeQuery(
        consultantQueryResults
      );

      StringBuilder parseReport = new StringBuilder();
      parseReport.append(
        String.format(
          "%1$-45s %2$-45s %3$-45s %4$-45s %5$s \n",
          "Consultant",
          "Appointment",
          "Customer",
          "Start",
          "End"
        )
      );
      parseReport.append(String.join("", Collections.nCopies(163, "-")));
      parseReport.append("\n");

      while (appointmentQueryResults.next()) {
        parseReport.append(
          String.format(
            "%1$-37s %2$-50s %3$-35s %4$-35s %5$s \n",
            appointmentQueryResults.getString("contact"),
            appointmentQueryResults.getString("description"),
            appointmentQueryResults.getString("customerName"),
            appointmentQueryResults.getString("start"),
            appointmentQueryResults.getString("end")
          )
        );
      }

      scheduleOfConsultantText.setText(parseReport.toString());
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  public void generateThirdReport() {
    try {
      Statement statement = DBConnection.getConnection().createStatement();
      //"SELECT description, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(START)";
      String appointmentQueryResults =
        "SELECT YEAR(start) as 'Year', COUNT(*) as 'Total' FROM appointment GROUP BY YEAR(start)";

      ResultSet yearlyQueryResults = statement.executeQuery(
        appointmentQueryResults
      );

      StringBuilder parseReport = new StringBuilder();
      parseReport.append(String.format("%1$-50s %2$-50s \n", "Year", "Total"));
      parseReport.append(String.join("", Collections.nCopies(163, "-")));
      parseReport.append("\n");

      while (yearlyQueryResults.next()) {
        parseReport.append(
          String.format(
            "%1$-50s %2$-50s \n",
            yearlyQueryResults.getString("Year"),
            yearlyQueryResults.getString("Total")
          )
        );
      }

      totalAppointmentsThisYearText.setText(parseReport.toString());
    } catch (SQLException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  @FXML
  void generateReportHandler(ActionEvent event) {
    if (
      selectReportComboBox.getValue() == "Number of appointment types by month"
    ) {
      generateFirstReport();
    }

    if (selectReportComboBox.getValue() == "Schedule of each consultant") {
      generateSecondReport();
    }

    if (
      selectReportComboBox.getValue() ==
      "Total number of appointments this year"
    ) {
      generateThirdReport();
    }
  }

  @FXML
  void backButtonHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    selectReportComboBox.setItems(selectedReport);
  }
}
