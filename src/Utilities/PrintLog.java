/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 *
 * @author eunice
 */
// createFile
public class PrintLog {
  public static String filename = "user_login_log.txt";

  File file = new File(filename);

  public static void trackLog(String user, boolean loggedIn) {
    try {
      FileWriter fwriter = new FileWriter(filename, true);

      try (PrintWriter logFile = new PrintWriter(fwriter)) {
        logFile.println(
          user + " logged in on: " + Calendar.getInstance().getTime()
        );
      }
    } catch (IOException e) {
      System.out.println("Writing to log failed");
    }
  }
}
/* =================================================================  
                          	MY NOTES
================================================================= */
/*
Java Create and Write To Files
https://www.w3schools.com/java/java_files_create.asp
https://howtodoinjava.com/java/io/how-to-create-a-new-file-in-java/

--------------------------------------------------------------------
*/
/* -------------------------------------------------------------- */
