/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Database.DBConnection;
import Model.Appointment;
import Model.Customer;
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
  private TableView<Appointment> reportConsultantTable;

  @FXML
  private TableColumn<?, ?> consultantCol;

  @FXML
  private TableColumn<?, ?> customerCol;

  @FXML
  private TableColumn<?, ?> startCol;

  @FXML
  private TableColumn<?, ?> endCol;

  @FXML
  private TableView<Customer> reportAdditionalTable;

  @FXML
  private TableColumn<?, ?> nameAddCol;

  @FXML
  private Button backButton;

  private ObservableList<ObservableList> apptTypeCountData;

  // private

  /* -------------------------------------------------------------- */
  public void buildMonthTypeData() {
    apptTypeCountData = FXCollections.observableArrayList();

    try {
      String apptTypeQuery =
        "SELECT date_format(start, '%M') AS Month, count(*) AS 'Number of appointment types' " +
        "FROM appointment " +
        "GROUP BY date_format(start, '%M')";

      Statement statement = DBConnection.getConnection().createStatement();

      ResultSet apptTypeRS = statement.executeQuery(apptTypeQuery);

      // Create cell value factories
      // Use lambdas. It is more flexible than
      // monthApptCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      //   public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
      //       return new SimpleStringProperty(param.getValue().get(0).toString());
      //   }
      // });

      // typeApptCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      //   public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
      //       return new SimpleStringProperty(param.getValue().get(1).toString());
      //   }
      // });

      for (int i = 0; i < apptTypeRS.getMetaData().getColumnCount(); i++) {
        //We are using non property style for making dynamic table
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
        System.out.println("Column [" + i + "] ");
      }

      // Add data to ObservableList
      while (apptTypeRS.next()) {
        // Iterate row
        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= apptTypeRS.getMetaData().getColumnCount(); i++) {
          // Iterate column
          row.add(apptTypeRS.getString(i));
        }

        apptTypeCountData.add(row);
      }

      // Add to tableview
      reportAppointmentTable.setItems(apptTypeCountData);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error building number of appointment types by month");
    }
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    buildMonthTypeData();
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
Java: setCellValuefactory; Lambda vs. PropertyValueFactory; advantages/disadvantages
https://stackoverflow.com/questions/38049734/java-setcellvaluefactory-lambda-vs-propertyvaluefactory-advantages-disadvant

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
