/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.data;

import appointmentscheduler.logic.Authentication;
import appointmentscheduler.view.AlertBox;
import appointmentscheduler.view.AppointmentScheduler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

/**
 *
 * @author marro
 */
public class DBConnection {
    //52.206.157.109/ -- Old server name
    private static Connection conn;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB = "U04NEQ";
    private static final String URL = "jdbc:mysql://3.227.166.251/" + DB;
    private static final String USER = DB;
    private static final String PASS = "53688288426";
    
    private DBConnection(){};
    
    // Method to get connection 
    public static Connection getConnection(){
        try{
            Class.forName(DRIVER);
            try{
                conn = DriverManager.getConnection(URL,USER,PASS);
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
