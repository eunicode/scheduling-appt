/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

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

    // System.out.println("Connection successful");

    return conn;
  }

  public static Connection getConnection() {
    return conn;
  }

  public static void closeConnection()
    throws ClassNotFoundException, SQLException, Exception {
    conn.close();
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
