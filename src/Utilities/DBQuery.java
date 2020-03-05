/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author eunice
 */

 // Utilities class to shorten database code
public class DBQuery {
  private static String query;
  private static Statement statement;
  private static ResultSet result;

  public static void makeQuery(String sql) {
    query = sql.toLowerCase();

    try {
      // Create a Statement Object
      Statement statement = DBConnection.makeConnection().createStatement();

      // Execute SELECT queries
      if (query.startsWith("select")) {
        result = statement.executeQuery(query);
      }
      // Execute DELETE, INSERT, UPDATE commands
      else if (
        query.startsWith("delete") ||
        query.startsWith("insert") ||
        query.startsWith("update")
      ) {
        statement.executeUpdate(query);
      }
    } catch (Exception e) {
      System.out.println("SQL query failed");
    }
  }

  public static ResultSet getResult() {
    return result;
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
