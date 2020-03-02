/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author eunice
 */

public class DBConnection {
  private static final String DATABASENAME = "U06EKa";
  private static final String DB_URL =
    "jdbc:mysql://3.227.166.251/" + DATABASENAME;
  private static final String USERNAME = "U06EKa";
  private static final String PASSWORD = "53688739723";
  private static final String DRIVER = "com.mysql.jdbc.Driver";
  // Variable to store the "bridge"
  private static Connection conn;

  public static Connection makeConnection()
    throws ClassNotFoundException, SQLException, Exception {
    Class.forName(DRIVER);
    // Create bridge and update `conn`
    conn = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

    //Lambda expression that prints to the console when user logins
    new Thread(() -> System.out.println("Connection sucessful!")).start();
    return conn;
  }

  public static void closeConnection()
    throws ClassNotFoundException, SQLException, Exception {
    conn.close();
  }

  public static Connection getConnection() {
    return conn;
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
