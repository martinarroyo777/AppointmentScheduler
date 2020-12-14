/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.data;

import appointmentscheduler.logic.Authentication;
import appointmentscheduler.logic.LogWriter;
import appointmentscheduler.view.AlertBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marro
 */
public class DBConnection {
    
    private static Connection conn;
    private static String DRIVER;
    private static String DB;
    private static String URL;
    private static String USER;
    private static String PASS;
        
    private DBConnection(){};
    
    // Method to get connection 
    public static Connection getConnection() {
        /*
            Load DB properties from config file
        */
        Properties props = new Properties(); 
        FileInputStream in;
        
        try {
            in = new FileInputStream("dbconf.config");
            try {
                props.load(in);
                in.close();
                // set our variables
                URL = props.getProperty("dburl");
                USER = props.getProperty("dbuser");
                PASS = props.getProperty("dbpass");
                DB = props.getProperty("dbname");
                DRIVER = props.getProperty("dbdriver");

            } catch (IOException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);

            try{
                LogWriter.writeToLog(ex.toString());
            } catch(IOException e){
                System.out.println(e.toString());
            }
        }
        
        try{
            Class.forName(DRIVER);
            try{
                conn = DriverManager.getConnection(URL+DB,USER,PASS);
            }catch (SQLException ex){
                AlertBox.display("Database Error", "Cannot establish connection to database. Check your connection and try again.");
                System.out.println("Cannot obtain connection to Database");
                // Connection cannot be made. Write to log.
                StringBuilder logMessage = new StringBuilder(LocalDate.now().toString());
                String stackTrace = Arrays.toString(ex.getStackTrace());
                logMessage.append(":\t");
                logMessage.append("Error obtaining database connection:\n");
                logMessage.append(stackTrace);
                try{
                    Authentication.writeToLog(logMessage.toString());
                } catch(IOException e){
                    System.out.println(e);
                }
            } 
        } catch (ClassNotFoundException e){
            System.out.println("Cannot find database driver");
        } catch (NullPointerException npe){
            // If we get here, then the db configuration file is missing or damaged. Log it and let the user know
            try{
                LogWriter.writeToLog(npe.toString() + "--It appears that the database configuration file is missing. You can add one to the project root directory to connect to the database.");
            } catch(IOException ioe){
                System.out.println(ioe.toString());
            }
            AlertBox.display("Missing Configuration Data", "It appears that the database configuration file is missing. You can add one to the project root directory to connect to the database.");
        }
        

        return conn;
    }
    public static void closeConnection(){
        try{
            if (conn != null){
                conn.close();
            }
        }catch(SQLException e){
            System.out.println("Could not close the connection.");
        }
    }
}
