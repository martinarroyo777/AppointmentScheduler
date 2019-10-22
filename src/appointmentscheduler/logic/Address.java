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
 * Represents an address in the database
 * @author marro
 */
public class Address extends Entity{
    // These four variables together determine a unique address
    private String add1;
    private String add2;
    private String zip;
    private String phone;
    
    // Constructor
    public Address (String addr1, String addr2, String zip, String phone){
        this.add1 = addr1;
        this.add2 = addr2;
        this.zip = zip;
        this.phone = phone;
    }
    
    /*
        Getter and setter methods
    */
    public String getAddress1(){
        return this.add1;
    }
    public void setAddress1(String add1){
        this.add1 = add1;
    }
    public String getAddress2(){
        return this.add2;
    }
    public void setAddress2(String add2){
        this.add2 = add2;
    }
    public String getZipcode(){
        return this.zip;
    }
    public void setZipcode(String zip){
        this.zip = zip;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    /**
     * Returns the addressId for the address found with the given parameters
     * @param addr1
     * @param addr2
     * @param zip
     * @param phone
     * @param cityID
     * @return The address Id if it exists, otherwise -1
     */
    public static int getAddressID(String addr1, String addr2, String zip, String phone, int cityID){
         // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String add1 = addr1.toLowerCase();
        String add2 = addr2.toLowerCase();      
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.GET_ADDRID);
            stmt.setString(1, add1);
            stmt.setString(2, add2);
            stmt.setString(3, zip);
            stmt.setString(4, phone);
            stmt.setInt(5, cityID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { 
            // If we have a result then the client exists
                return rs.getInt("AddressID");
            } 
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during getAddressID method call.");
            System.out.println(e);
        }
        
        // Address Id doesn't exist for the given address params
        return -1;
    }
    
    
    /**
     * Tests to see if the given address entry already exists in the database.
     * 
     * @param addr1
     * @param addr2
     * @param zip
     * @param phone
     * @param cityId
     * @return True when all four parameters are present in a single database entry.
     */
    public static boolean exists(String addr1, String addr2, String zip, String phone, int cityId){
    // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String add1 = addr1.toLowerCase();
        String add2 = addr2.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.ADDR_EXISTS);
            stmt.setString(1, add1);
            stmt.setString(2, add2);
            stmt.setString(3, zip);
            stmt.setString(4, phone);
            stmt.setInt(5, cityId);
            ResultSet rs = stmt.executeQuery();
            return rs.first(); // Return whether or not we have a record
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during exists method call in Address class.");
        }
        
        return false;
    }
    
    /**
     * Adds given address to the address table if it is unique. CityID is required. 
     * An address is considered unique when the combination of address1 and 2, zip and phone
     * do not already exist in the database. 
     * @param addr
     * @param cityID
     */
    public static void addAddress(Address addr, int cityID){
        // Query parameters
        String add1 = addr.getAddress1();
        String add2 = addr.getAddress2();
        String zip = addr.getZipcode();
        String phone = addr.getPhone();
        String userName = Consultant.getUserName();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt_addr = null;
        try {
            stmt_addr = con.prepareStatement(Queries.ADD_ADDRESS);
            stmt_addr.setString(1, add1);
            stmt_addr.setString(2, add2);
            stmt_addr.setInt(3, cityID);
            stmt_addr.setString(4, zip);
            stmt_addr.setString(5, phone);
            stmt_addr.setString(6, userName);
            stmt_addr.setString(7, userName);
            stmt_addr.executeUpdate();
        }
        catch (SQLException ex){
            System.out.println("Difficulties contacting database during addCity method call.");
            System.out.println(ex);
        }
    }
    
    /**
     * Updates given address in the address table. CityID is required. 
     * @param addr
     * @param cityID
     */
    public static void updateAddress(Address addr, int cityID){
        // Query parameters
        String add1 = addr.getAddress1();
        String add2 = addr.getAddress2();
        String zip = addr.getZipcode();
        String phone = addr.getPhone();
        String userName = Consultant.getUserName();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt_addr = null;
        try {
            stmt_addr = con.prepareStatement(Queries.UPDATE_ADDR);
            stmt_addr.setString(1, add1);
            stmt_addr.setString(2, add2);
            stmt_addr.setInt(3, cityID);
            stmt_addr.setString(4, zip);
            stmt_addr.setString(5, phone);
            stmt_addr.setString(6, userName);
            stmt_addr.setString(7, userName);
            stmt_addr.executeUpdate();
        }
        catch (SQLException ex){
            System.out.println("Difficulties contacting database during addCity method call.");
            System.out.println(ex);
        }
    }
    
}
