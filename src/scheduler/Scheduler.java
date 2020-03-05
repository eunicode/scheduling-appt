/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import Utilities.DBConnection;
import Model.Appointment;
import Model.Customer;
import Model.DataProvider;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
// import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

// import javafx.stage.Stage;

/**
 *
 * @author eunice
 */

public class Scheduler extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(
      getClass().getResource("/View_Controller/LoginScreen.fxml")
    );

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
Writing a function inside the main method 
https://stackoverflow.com/questions/20888208/writing-a-function-inside-the-main-method-java

you can't declare a method inside another method.
Unless you use Java 8's Closure/Lambd a functionality

--------------------------------------------------------------------
STATIC METHODS

static methods = class methods 
non-static methods = instance methods
a static method cannot call a non static method, 
because it is not being called from an instance/object

*/
/* -------------------------------------------------------------- */
