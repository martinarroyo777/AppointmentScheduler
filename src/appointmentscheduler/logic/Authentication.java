/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import appointmentscheduler.data.DBConnection;
import appointmentscheduler.view.AlertBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Class to handle authentication and log writing
 * @author marro
 */
public final class Authentication {
   
    private Authentication(){};
    
    public static boolean isAuthenticated(String user, String pass){
        // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        Integer userID = null;
        StringBuilder logMessage = new StringBuilder(LocalDateTime.now().toString());
        logMessage.append(":\t");
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.IS_AUTH);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.first()) { 
            // We have a result, so user is authenticated. Write to log
                userID = rs.getInt("userId");
                logMessage.append(" Log-in from ");
                logMessage.append(user);
                try{
                    writeToLog(logMessage.toString());
                } catch(IOException e){
                    System.out.println(e);
                }
            // Create new Consultant object
                Consultant consultant = new Consultant(userID, user);
                return true;
            } else {
                // Invalid log-in. Record in log.
                logMessage.append("Invalid log-in attempted with username: ");
                logMessage.append(user);
                logMessage.append(" and password: ");
                logMessage.append(pass);
                try{
                    writeToLog(logMessage.toString());
                } catch (IOException e){
                    System.out.println(e);
                }
            }
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during authentication.");
            AlertBox.display("Authentication Error", "Cannot connect to database. Please try again.");
            logMessage.append("Database connection error:\n");
            logMessage.append(Arrays.toString(e.getStackTrace()));
            try{
                writeToLog(logMessage.toString());
            } catch(IOException ex){
                System.out.println(ex);
            }
            return false;
        }
       
        
        return false;
        
    }
    /**
     * Writes to our log. <br>Utilizes Try with Resources to ensure resource closure
     * @param message
     * @return
     * @throws IOException 
     */
    public static boolean writeToLog(String message) throws IOException{
        
        if (message.isEmpty()) return false;
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Logs/log.txt",true))){
            writer.write(message);
            writer.newLine();
        }

        return true;
    }
}
