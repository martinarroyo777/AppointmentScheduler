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
 * Represents a customer in the database
 * @author marro
 */

public class Client extends Entity {
    private int clientID;
    private int addressID;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String country;
    private String phone;
    // Container for client information to be passed between screens
    private static ArrayList<Client> clientCache = new ArrayList<>();
    
    public Client (int clientID, String name, String address1, String address2, String city, String zip, String country, String phone) {
        this.clientID = clientID;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
    }
 
    public Client ( String name, String address1, String address2, String city, String zip, String country, String phone) {
        
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
    }
    public Client(){}
    
    public int getClientID(){
        return this.clientID;
    }
    public void setClientID(int id){
        this.clientID=id;
    }
    public int getAddressID(){
        return this.addressID;
    }
    public void setAddressID(int id){
        this.addressID=id;
    }
    public String getAddress1(){
        return this.address1;
    }
    public void setAddress1(String addr){
        this.address1 = addr;
    }
    public String getAddress2(){
        return this.address2;
    }
    public void setAddress2(String addr){
        this.address2 = addr;
    }
    public String getCity(){
        return this.city;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getZip(){
        return this.zip;
    }
    public void setZip(String zip){
        this.zip = zip;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String ph){
        this.phone = ph;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    
    /**
     * Gets client ID from database
     * @param name
     * @param addressID
     * @return client Id if found; -1 if no Id was found 
     */ 
    public static int getClientID(String name, int addressID) {
         // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        String n = name.toLowerCase();
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.GET_CLIENTID);
            stmt.setString(1, n);
            stmt.setInt(2, addressID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) { 
            // If we have a result then the client exists
                int id = rs.getInt("customerID");
                return id;
            }
            
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during getClientID method call.");
        }
        // An error occurred somewhere
        System.out.println("Something went wrong in the getClientID method...");
        return -1;
    }
    
    
    /**
     * Checks if client exists in the database
     * @param name
     * @param addressID
     * @return True if the client is in the database; false otherwise
     */
    public boolean exists(String name, int addressID) {
    // Get database connection
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        // Query the database with the supplied credentials
        try {
            stmt = con.prepareStatement(Queries.CLIENT_EXISTS);
            stmt.setString(1, name);
            stmt.setInt(2, addressID);
            ResultSet rs = stmt.executeQuery();
            // Determine whether or not a result is present in our result set
            return rs.first();
        }catch (SQLException e){
            System.out.println("Difficulties contacting database during exists method call in Client class.");
        }
        
        return false;
    }
    
    /**
     * Adds a new client to the database
     * @param client
    */
    public static void addNewClient(Client client) {
    // Get database connection
        Connection con = DBConnection.getConnection();
        // Statements for batch processing
        PreparedStatement stmt_city = null;
        PreparedStatement stmt_address = null;
        PreparedStatement stmt_client = null;
        // Client info for database
        String name = client.getName();
        String add1 = client.getAddress1();
        String add2 = client.getAddress2();
        String city = client.getCity();
        String country = client.getCountry();
        String zip = client.getZip();
        String phone = client.getPhone();
        String user = Consultant.getUserName();
        // Query the database with the supplied credentials
        try {
            /* --------------------
                Insert into country
            ------------------------ */
            Country ctry = new Country();
            ctry.setName(country);
            // Add country to db
            Country.addCountry(ctry);
            int countryID = ctry.getCountryID(country);
            /* ---------------------
                Insert into city
            -------------------------*/
            City cty = new City();
            cty.setName(city);
            // Add city to db
            City.addCity(cty, countryID);
            int cityID = cty.getCityID(city, countryID);
            /*----------------------------
                Insert Address
            ------------------------------- */
            Address add = new Address(add1,add2,zip,phone);
            // Add address to db
            Address.addAddress(add, cityID);
            int addressID = Address.getAddressID(add1,add2,zip,phone, cityID);
            /*----------------------------
                Insert Client
            ------------------------------- */
            // Execute insertion only if the address is not present in DB
            if (client.exists(name, addressID)){
                System.out.println("Client already exists in database.");
            } 
            else{
                stmt_client = con.prepareStatement(Queries.INSERT_CLIENT);
                stmt_client.setString(1, name);
                stmt_client.setInt(2, addressID);
                stmt_client.setInt(3, 1); // 1 denotes active; 0 denotes inactive
                stmt_client.setString(4, user);
                stmt_client.setString(5, user);
                stmt_client.executeUpdate();
            }
        }catch (SQLException ex){
            System.out.println("Difficulties contacting database during addClient method call.");
            System.out.println(ex);
        }
        
    
    }
    
    /**
     * Modifies the given client in the database
     * @param client
     * @param clientID
    */
    public static void modifyClient(Client client, int clientID) {
    // Get database connection
        Connection con = DBConnection.getConnection();
        // Statements for batch processing
        PreparedStatement stmt_city = null;
        PreparedStatement stmt_address = null;
        PreparedStatement stmt_client = null;
        // Client info for database
        String name = client.getName();
        String add1 = client.getAddress1();
        String add2 = client.getAddress2();
        String city = client.getCity();
        String country = client.getCountry();
        String zip = client.getZip();
        String phone = client.getPhone();
        String user = Consultant.getUserName();
        // Query the database with the supplied credentials
        try {
            /* --------------------
                Update country
            ------------------------ */
            Country ctry = new Country();
            ctry.setName(country);
            // Add country to db
            Country.addCountry(ctry);
            int countryID = ctry.getCountryID(country);
            /* ---------------------
                Update city
            -------------------------*/
            City cty = new City();
            cty.setName(city);
            // Add city to db
            City.addCity(cty, countryID);
            int cityID = cty.getCityID(city, countryID);
            System.out.println(cityID);
            /*----------------------------
                Update Address
            ------------------------------- */
            Address add = new Address(add1,add2,zip,phone);
            // if address exists perform an update, otherwise add it
            if (Address.exists(add1, add2, zip, phone, cityID)){
                System.out.println("Address already exists in database");
            } else {
                Address.addAddress(add, cityID);
            }
            int addressID = Address.getAddressID(add1,add2,zip,phone, cityID);
            System.out.println(addressID);
            /*----------------------------
                Update Client
            ------------------------------- */
            stmt_client = con.prepareStatement(Queries.MODIFY_CLIENT);
            stmt_client.setString(1, name);
            stmt_client.setInt(2, addressID);
            stmt_client.setString(3, user);
            stmt_client.setString(4, user);
            stmt_client.setInt(5, clientID);
            stmt_client.executeUpdate();            
        }catch (SQLException ex){
            System.out.println("Difficulties contacting database during modifyClient method call.");
            System.out.println(ex);
        }
        
    
    }
    
    
    /**
     * Method to return client cache
     * @return ArrayList  of clients currently in our cache
     */
    public static ArrayList<Client> getClientCache(){
        return clientCache;
    }
    
    
    /**
     * Method to add client to client cache
     * @param c 
     */
    public static void addClientCache(Client c){
        clientCache.add(c);
    }
    
  
    /**
     * Clears the client cache
     */
    public static void clearClientCache(){
        clientCache.clear();
    }
    
    
    /**
     * Soft deletes the given client in database by setting active field to 0.
     * @param c
     */
    public static void deleteClient(Client c) {
        Address clientAddr = new Address(c.getAddress1(),c.getAddress2(),c.getZip(),c.getPhone());
        int addressID = c.getAddressID();
        int clientID = c.getClientID();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(Queries.DELETE_CLIENT);
            stmt.setInt(1, clientID);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("A database error occurred during the deleteClient method call.");
        }
    }

    
    /**
     * Retrieves client data from DB and returns as an ArrayList.
     * @return An ArrayList representing all active clients in database.
     */
    public static ArrayList<Client> clientList() {
        // Our ArrayList
        ArrayList<Client> list = new ArrayList<>();
        // Get database connection
        Connection con = DBConnection.getConnection();
        // Holder for SQL statement
        PreparedStatement getClientList = null;
        // Query the database
        try{
        
            getClientList = con.prepareStatement(Queries.GET_ALLCLIENTS);
            ResultSet rs = getClientList.executeQuery();
            // Go through our result set and add each client to our array list
            while (rs.next()){
                Client c = new Client(rs.getInt("customerId"),rs.getString("customerName"),
                                    rs.getString("address"),rs.getString("address2"),rs.getString("city")
                                   ,rs.getString("postalCode"), rs.getString("country"),
                                    rs.getString("phone"));
                c.setAddressID(rs.getInt("addressId"));
                list.add(c);
            }    
        }
        catch (SQLException e){
            System.out.println("Something went wrong with SQL query/Database");
        }

        return list;
    }
}
