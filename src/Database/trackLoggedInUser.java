/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *
 * @author eunice
 */

public class trackLoggedInUser {

    public static final String filename = "userlog.txt";
    
    public trackLoggedInUser() {};

    public static void trackLog (String user, boolean loggedIn) {

            try {
           
                FileWriter fw = new FileWriter(filename, true);
                PrintWriter logFile = new PrintWriter(fw);
                LocalDateTime localTime = LocalDateTime.now();
                logFile.println(user + " access time: " + localTime);
                logFile.close();
                System.out.println(user + " - has been written to the log");
        
            } 
            
            catch (IOException ex) {
            System.out.println("Logger error: " + ex.getMessage());
        }

    }
}