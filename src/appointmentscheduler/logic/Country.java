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

/**
 * Represents country entries in the database
 * @author marro
 */
public class Country extends Entity {
    /**
     * Gets countryID from database for the given country if it already exists in the DB.
     * @param country
     * @return CountryID or -1 if it doesn't exist in the database
     */
    public int getCountryID(String country){
         // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String c = country.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.GET_CTRYID);
            stmt.setString(1, c);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { 
            // If we have a result then the client exists
                return rs.getInt("countryID");
            } 
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during getCountryID method call.");
            System.out.println(e);
        }
        // Error occurred somewhere
        return -1;
    }
    
    public boolean exists(String country){
    // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String c = country.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.CTRY_EXISTS);
            stmt.setString(1, c);
            ResultSet rs = stmt.executeQuery();
            return rs.first(); // return whether or not we retrieved a record
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during exist method call in Country.");
        }
        return false;
    }
    
    /**
     * Adds given country to the country table if it is unique
     * @param c
     */
    public static void addCountry(Country c){
        // Query parameters
        String countryName = c.getName();
        String userName = Consultant.getUserName();
        // Check if country exists... if so do nothing
        if (c.exists(countryName)){
            System.out.println("This country already exists in database.");
        }
        // Otherwise add it to the database
        else {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt_ctry = null;
            try {
                stmt_ctry = con.prepareStatement(Queries.ADD_CTRY);
                stmt_ctry.setString(1, countryName);
                stmt_ctry.setString(2, userName);
                stmt_ctry.setString(3, userName);
                stmt_ctry.executeUpdate();
            }
            catch (SQLException ex){
            System.out.println("Difficulties contacting database during addCountry method call.");
            System.out.println(ex);
            }
        }
    }
}
