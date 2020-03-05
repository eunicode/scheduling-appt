/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import Utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author eunice
 */

public class ReportScreenController implements Initializable {
  @FXML
  private TableView reportAppointmentTable;

  // private TableView<Appointment> reportAppointmentTable;

  @FXML
  private TableColumn<?, ?> monthApptCol;

  @FXML
  private TableColumn<?, ?> typeApptCol;

  @FXML
  private TableView reportConsultantTable;

  @FXML
  private TableColumn<?, ?> consultantCol;

  @FXML
  private TableColumn<?, ?> customerCol;

  @FXML
  private TableColumn<?, ?> startCol;

  @FXML
  private TableColumn<?, ?> endCol;

  @FXML
  // private TableView<Customer> reportAdditionalTable;
  private TableView reportAdditionalTable;

  @FXML
  private TableColumn<?, ?> nameAddCol;

  @FXML
  private Button backButton;

  private ObservableList<ObservableList> apptData;
  private ObservableList<Appointment> consultantData;
  private ObservableList<ObservableList> additionalData;

  // private

  /* -------------------------------------------------------------- */
  public void buildApptTypeData() {
    apptData = FXCollections.observableArrayList();

    try {
      String apptTypeQuery =
        "SELECT date_format(start, '%M') AS Month, count(*) AS 'Number of appointment types' " +
        "FROM appointment " +
        "GROUP BY date_format(start, '%M')";

      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet apptTypeRS = statement.executeQuery(apptTypeQuery);

      // Create columns and cell value factories
      // Use lambdas. It is more flexible than
      for (int i = 0; i < apptTypeRS.getMetaData().getColumnCount(); i++) {
        final int j = i;

        TableColumn col = new TableColumn(
          apptTypeRS.getMetaData().getColumnName(i + 1)
        );

        col.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

            public ObservableValue<String> call(
              CellDataFeatures<ObservableList, String> param
            ) {
              return new SimpleStringProperty(
                param.getValue().get(j).toString()
              );
            }
          }
        );

        reportAppointmentTable.getColumns().addAll(col);
        // System.out.println("Column [" + i + "] ");
      }

      // Add data to ObservableList
      while (apptTypeRS.next()) {
        // Iterate row
        ObservableList<String> row = FXCollections.observableArrayList();

        for (int i = 1; i <= apptTypeRS.getMetaData().getColumnCount(); i++) {
          // Iterate column
          row.add(apptTypeRS.getString(i));
        }

        apptData.add(row);
      }

      // Add to tableview
      reportAppointmentTable.setItems(apptData);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error building number of appointment types by month");
    }
  }

  /* -------------------------------------------------------------- */
  public void buildConsultantData() {
    DataProvider.getAllAppointmentsTableList().clear();
    reportConsultantTable.setItems(DataProvider.getAllAppointmentsTableList());

    DataProvider populateAppointments = new DataProvider();
    populateAppointments.populateAppointmentTable();

    consultantCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
    endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
    // try {
    //   DataProvider populateAppointments = new DataProvider();
    //   populateAppointments.populateAppointmentTable();

    // } catch(SQLException ce) {
    //   logger.info(ce.toString());
    // }
  }

  /* -------------------------------------------------------------- */
  public void buildAdditionalData() {
    additionalData = FXCollections.observableArrayList();

    try {
      String additionalQuery =
        "SELECT customerName AS 'Customer Name' " +
        "FROM customer " +
        "WHERE customerId NOT IN ( " +
        "SELECT customerId " +
        "FROM appointment)";

      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet additionalRS = statement.executeQuery(additionalQuery);

      for (int i = 0; i < additionalRS.getMetaData().getColumnCount(); i++) {
        final int j = i;

        TableColumn col = new TableColumn(
          additionalRS.getMetaData().getColumnName(i + 1)
        );

        col.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

            public ObservableValue<String> call(
              CellDataFeatures<ObservableList, String> param
            ) {
              return new SimpleStringProperty(
                param.getValue().get(j).toString()
              );
            }
          }
        );

        reportAdditionalTable.getColumns().addAll(col);
        // System.out.println("Column [" + i + "] ");
      }

      // Add data to ObservableList
      while (additionalRS.next()) {
        // Iterate row
        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= additionalRS.getMetaData().getColumnCount(); i++) {
          // Iterate column
          row.add(additionalRS.getString(i));
        }

        additionalData.add(row);
      }

      // Add to tableview
      reportAdditionalTable.setItems(additionalData);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error building number of appointment types by month");
    }
  }

  /* -------------------------------------------------------------- */
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    buildApptTypeData();
    buildConsultantData();
    buildAdditionalData();
    // DataProvider.getAllAppointmentsTableList().clear();
    // appointmentTableView.setItems(DataProvider.getAllAppointmentsTableList());

    // DataProvider populateAppointments = new DataProvider();
    // populateAppointments.populateAppointmentTable();
  }

  /* -------------------------------------------------------------- */
  @FXML
  void backButtonHandler(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(
      getClass().getResource("/View_Controller/MainScreen.fxml")
    );
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
TODO

Make properties private and use property accessor methods, along with get and set methods.

--------------------------------------------------------------------
Java: setCellValuefactory; Lambd a vs. PropertyValueFactory; advantages/disadvantages
https://stackoverflow.com/questions/38049734/java-setcellvaluefactory-lambd a-vs-propertyvaluefactory-advantages-disadvant

--------------------------------------------------------------------
"SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '01' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '01' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '02' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '02' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '03' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '03' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '04' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '04' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '05' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '05' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '06' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '06' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '07' AND type = 'Presentation' "+
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '07' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '08' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '08' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '09' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '09' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '10' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '10' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '11' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '11' AND type = 'Scrum' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '12' AND type = 'Presentation' " +
      "UNION ALL SELECT count(*) FROM appointment WHERE date_format(start, '%m') = '12' AND type = 'Scrum' ";
*/
/* -------------------------------------------------------------- */
/* public void buildConsultantData() {
  consultantData = FXCollections.observableArrayList();

  try {
    String consultantQuery =
      "SELECT 'test' AS Consultant, c.customerName AS Customer, a.start AS Start, a.end AS End " +
      "FROM customer AS c " +
      "INNER JOIN appointment AS a " +
      "WHERE c.customerId = a.customerId";

    Statement statement2 = DBConnection.getConnection().createStatement();

    ResultSet consultantRS = statement2.executeQuery(consultantQuery);

    for (int i = 0; i < consultantRS.getMetaData().getColumnCount(); i++) {
      final int j = i;

      TableColumn col = new TableColumn(consultantRS.getMetaData().getColumnName(i + 1));

      col.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(
            CellDataFeatures<ObservableList, String> param
          ) {
            return new SimpleStringProperty(
              param.getValue().get(j).toString()
            );
          }
        }
      );

      reportConsultantTable.getColumns().addAll(col);
      // System.out.println("Column [" + i + "] ");
    }

    // Add data to ObservableList
    while (consultantRS.next()) {
      // Iterate row
      ObservableList<String> row = FXCollections.observableArrayList();
      for (int i = 1; i <= consultantRS.getMetaData().getColumnCount(); i++) {
        // Iterate column
        row.add(consultantRS.getString(i));
      }

      consultantData.add(row);
    }

    // Add to tableview
    reportConsultantTable.setItems(consultantData);
  } catch (Exception e) {
    e.printStackTrace();
    System.out.println("Error building schedule for each consultant");
  }
}
*/
