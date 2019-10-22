/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

import appointmentscheduler.data.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author marro
 */
public class Consultant extends Entity {
    private static int userID;
    private static String userName;
    
    public Consultant(int uid, String uname){
        userID = uid;
        userName = uname;
    }
    public static String getUserName(){
        return userName;
    }
    public static int getUserID(){
        return userID;
    }
    /* May move to Appointment class or remove altogether
    public static Appointment getNextAppointment(){
       // TODO: add logic to get next appointment
       // TODO consider whether this needs to return an appointment object... this could be set to void and just pop an alert
       // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement("SELECT start "
                    + "FROM appointment "
                    + "WHERE (createdBy=? OR lastUpdateBy=?)"
                    + "AND TIMESTAMPDIFF(MINUTE,start,?) <= 15");
            stmt.setString(1, userName);
            stmt.setString(2, userName);
            stmt.setString(3, userName);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                
            }
        }catch (SQLException e){
            System.out.println("Difficulties contacting database.");
        }
        return null;
    }
    */
    
    /**
     * Gets the userId for the given consultant by name
     * @param name
     * @return UserId if the name is valid; otherwise -1
     */
    public static int getConsultantID(String name){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        int id = -1;
        try{
            stmt = con.prepareStatement(Queries.GET_CONSULTANTID_WNAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) id = rs.getInt("userId");
        }
        catch(SQLException e){
            System.out.println("Database error occurred during getConsultantID method call");
            System.out.println(e);
        }
        return id;
    }
    
    /**
     * Gets an ArrayList list of the user names in the system
     * @return ArrayList of user names from system
     */
    public static ArrayList<String> getConsultants(){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        ArrayList<String> results = new ArrayList<>();
        try{
            stmt = con.prepareStatement(Queries.GET_CONSULTANT_NAMES);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                results.add(rs.getString("userName"));
            }
        }
        catch(SQLException e){
            System.out.println("Database error occurred during getConsultantID method call");
            System.out.println(e);
        }
        return results;
    }
    
    /**
     * Gets the username in user table for given userId
     * @param userId
     * @return Username if found; "" otherwise
     */
    public static String getConsultantName(int userId){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.GET_CONSULTANTNAME_WID);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) return rs.getString("userName");
        }
        catch(SQLException e){
            System.out.println("Database error occurred during getConsultantName method call");
            System.out.println(e);
        }
        return "";
    }
    
    
}
