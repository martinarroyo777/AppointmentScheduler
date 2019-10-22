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
 * Represents a city in the database
 * @author marro
 */
public class City extends Entity{
    
    /**
     * Gets the city Id for the given city
     * @param city
     * @param countryID
     * @return The city id if it exists, otherwise -1
     */ 
    public int getCityID(String city, int countryID){
         // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String c = city.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.GET_CITYID);
            stmt.setString(1, c);
            stmt.setInt(2, countryID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { 
            // If we have a result then the client exists
                return rs.getInt("cityId");
            }
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during getCityID method call.");
            System.out.println(e);
        }
        // An error occurred somewhere
        return -1;
    }
    
    /**
     * Tests to see if the given city entry already exists in the database.
     * 
     * @param city
     * @param countryID
     * @return True when given city is in the database AND the given countryID is associated with it.
     */
    public boolean exists(String city, int countryID){
    // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String c = city.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.CITY_EXISTS);
            stmt.setString(1, c);
            stmt.setInt(2, countryID);
            ResultSet rs = stmt.executeQuery();
            return rs.first(); // Return whether or not we have a record
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during exists method call in City class.");
        }
        
        return false;
    }
    
    /**
     * Adds given city to the city table if it is unique. A country Id is needed in order to add a city.
     * @param c
     * @param countryID
     */
    public static void addCity(City c, int countryID){
        // Query parameters
        String cityName = c.getName();
        String userName = Consultant.getUserName();
        // Check if city exists... if so do nothing
        if (c.exists(cityName, countryID)){
            System.out.println("This city already exists in database.");
        }
        // Otherwise add it to the database
        else {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt_city = null;
            try {
                stmt_city = con.prepareStatement(Queries.ADD_CITY);
                stmt_city.setString(1, cityName);
                stmt_city.setInt(2, countryID);
                stmt_city.setString(3, userName);
                stmt_city.setString(4, userName);
                stmt_city.executeUpdate();
            }
            catch (SQLException ex){
            System.out.println("Difficulties contacting database during addCity method call.");
            System.out.println(ex);
            }
        }
    }
}
