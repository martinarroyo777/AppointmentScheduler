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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Represents an appointment in the database
 * @author marro
 */
public class Appointment {
    private int id;
    private Client client;
    private int clientID;
    private String clientName;
    private String category;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    // Stores the current date
    public static final LocalDate TODAY = LocalDate.now();
    // Stores the current month (based on TODAY)
    public static final int MONTH = TODAY.getMonthValue();
    // Stores the current year
    public static final int YEAR = TODAY.getYear();
    
    public Appointment(Client c, String category, String description, LocalDate date, 
                        LocalTime start, LocalTime end, String location)
    {
        this.client = c;
        this.clientName = c.getName();
        this.clientID = c.getID();
        this.category = category;
        this.description = description;
        this.date = date;
        this.startTime = start;
        this.endTime = end;
        this.location = location;

    }
    
    private Appointment(String clientName, String category, String description, LocalDate date, 
                        LocalTime start, LocalTime end, String location)
    {
        this.clientName = clientName;
        this.category = category;
        this.description = description;
        this.date = date;
        this.startTime = start;
        this.endTime = end;
        this.location = location;

    }
    
    public int getId(){
        return this.id;
    }
    public void setId(){
        this.id = Appointment.getAppointmentID(this);
    }
    public Client getClient(){
        return this.client;
    }
    public void setClient(Client client){
        this.client = client;
    }
    public String getClientName(){
        return this.clientName;
    }
    public void setClientName(String name){
        this.clientName = name;
    }
    public String getCategory(){
        return this.category;
    }
    public void setCategory(String cat){
        this.category = cat;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate d){
        this.date = d;
    }
    public LocalTime getStartTime(){
        return this.startTime;
    }
    public void setStartTime(LocalTime t){
        this.startTime = t;
    }
    public LocalTime getEndTime(){
        return this.endTime;
    }
    public void setEndTime(LocalTime t){
        this.endTime = t;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLocation(String l){
        this.location = l;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String desc){
        this.description = desc;
    }
    
    /**
     * Finds if there is an appointment within 15 minutes of the current time
     * @return True if there is an appointment within 15 minutes from current time (based on UTC)
     */
    public static boolean isInFifteen(){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.APPT_IN_15);
            stmt.setInt(1, Consultant.getUserID());
            ResultSet rs = stmt.executeQuery();
            return rs.first(); // return whether or not we have a result
        }
        catch(SQLException e){
            System.out.println("Database error occurred during isInFifteen method call");
        }
        return false;
    }
 
    /**
     * Helper method to retrieve appointment id from database. Returns -1 if not found.<br>
     * Unique appointment without an appointmentId is determined by userId, start and end date/times.
     * @return Appointment id if it exists, otherwise -1
     * @param a
     */
     public static int getAppointmentID(Appointment a){
         int apptID = -1;
         // Convert our time to UTC and pass it to our Timestamp
         Timestamp start = Timestamp.valueOf(localToUTC(a.getDate(),a.getStartTime()));//LocalDateTime.of(a.getDate(),a.getStartTime())); //start
         Timestamp end = Timestamp.valueOf(localToUTC(a.getDate(),a.getEndTime()));//LocalDateTime.of(a.getDate(),a.getEndTime())); // end
         Connection con = DBConnection.getConnection();
         PreparedStatement stmt = null;
         try{
             stmt = con.prepareStatement(Queries.GET_APPID);
             stmt.setInt(1, Consultant.getUserID());
             stmt.setTimestamp(2, start);
             stmt.setTimestamp(3, end);
             ResultSet rs = stmt.executeQuery();
             if (rs.first()){
                 apptID = rs.getInt("appointmentId");
             }
         } catch (SQLException e){
             
         }
         return apptID;
     }
     
    /**
     * Returns an ArrayList representing appointments in the database for the current week.<br>
     * The work week is 7 days. This list will show appointments between
     * the current system date and the date one week later.
     * 
     * @return ArrayList of appointments
     */
    public static ArrayList<Appointment> getAppointmentsWeek(){
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        
        try{
            stmt = con.prepareStatement(Queries.GET_APP_BYWEEK);
            stmt.setString(1, TODAY.toString());
            stmt.setString(2, TODAY.toString());
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                // Create client object from query results
                Client c = new Client(rs.getInt("customerId"), rs.getString("customerName"), 
                                      rs.getString("address1"),rs.getString("address2"),rs.getString("city"),
                                      rs.getString("postalCode"),rs.getString("country"), rs.getString("phone"));
                LocalDate meetingDate = rs.getDate("meetingDate").toLocalDate();
                // Convert times from UTC to local for display
                LocalDateTime start = utcToLocal(meetingDate, rs.getTime("startTime").toLocalTime());
                LocalDateTime end = utcToLocal(meetingDate, rs.getTime("endTime").toLocalTime());
                LocalTime startTime = start.toLocalTime();
                LocalTime endTime = end.toLocalTime();
                appointments.add(new Appointment(c,rs.getString("type"), rs.getString("description"), meetingDate, startTime, endTime,rs.getString("location")));
            }
        
        } catch (SQLException e){
            System.out.println("Error occurred with database connection during getAppointmentsWeek method call.");
            System.out.println(e);
        }
        return appointments;
    }
    
    /**
     * Returns an ArrayList representing appointments in the database for the given week.<br>
     * The work week is 7 days. This list will show appointments between
     * the given date and the date one week later.
     * @param currdate
     * @return ArrayList of appointments
     */
    public static ArrayList<Appointment> getAppointmentsWeek(String currdate){
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        
        try{
            stmt = con.prepareStatement(Queries.GET_APP_BYWEEK);
            stmt.setString(1, currdate);
            stmt.setString(2, currdate);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                // Create client object from query results
                Client c = new Client(rs.getInt("customerId"), rs.getString("customerName"), 
                                      rs.getString("address1"),rs.getString("address2"),rs.getString("city"),
                                      rs.getString("postalCode"),rs.getString("country"), rs.getString("phone"));
                LocalDate meetingDate = rs.getDate("meetingDate").toLocalDate();
                // Convert times from UTC to local for display
                LocalDateTime start = utcToLocal(meetingDate, rs.getTime("startTime").toLocalTime());
                LocalDateTime end = utcToLocal(meetingDate, rs.getTime("endTime").toLocalTime());
                LocalTime startTime = start.toLocalTime();
                LocalTime endTime = end.toLocalTime();
                appointments.add(new Appointment(c,rs.getString("type"), rs.getString("description"), meetingDate, startTime, endTime,rs.getString("location")));
            }
        
        } catch (SQLException e){
            System.out.println("Error occurred with database connection during getAppointmentsWeek method call.");
            System.out.println(e);
        }
        return appointments;
    }
    
    
    /**
     * Returns an ArrayList representing appointments in the database for the current month.
     * @return ArrayList of appointments
     */
    public static ArrayList<Appointment> getAppointmentsMonth(){
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        
        try{
            stmt = con.prepareStatement(Queries.GET_APP_BYMONTH);
            stmt.setString(1, String.valueOf(YEAR));
            stmt.setString(2, String.valueOf(MONTH));
            stmt.setString(3, String.valueOf(YEAR));
            stmt.setString(4, String.valueOf(MONTH));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                // Create client object from query results
                Client c = new Client(rs.getInt("customerId"), rs.getString("customerName"), 
                                      rs.getString("address1"),rs.getString("address2"),rs.getString("city"),
                                      rs.getString("postalCode"),rs.getString("country"), rs.getString("phone"));
                LocalDate meetingDate = rs.getDate("meetingDate").toLocalDate();
                // Convert times from UTC to local for display
                LocalDateTime start = utcToLocal(meetingDate, rs.getTime("startTime").toLocalTime());
                LocalDateTime end = utcToLocal(meetingDate, rs.getTime("endTime").toLocalTime());
                LocalTime startTime = start.toLocalTime();//rs.getTime("startTime").toLocalTime();
                LocalTime endTime = end.toLocalTime();//rs.getTime("endTime").toLocalTime();
                appointments.add(new Appointment(c,rs.getString("type"), rs.getString("description"), meetingDate, startTime, endTime,rs.getString("location")));
            }
        
        } catch (SQLException e){
            System.out.println("Error occurred with database connection during getAppointmentsMonth method call.");
            System.out.println(e);
        }
        return appointments;
    }
    
    /**
     * Returns an ArrayList representing appointments in the database for the given month and year.
     * @param month
     * @param year
     * @return ArrayList of appointments for the given month and year
     */
    public static ArrayList<Appointment> getAppointmentsMonth(int month, int year){
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        
        try{
            stmt = con.prepareStatement(Queries.GET_APP_BYMONTH);
            stmt.setString(1, String.valueOf(year));
            stmt.setString(2, String.valueOf(month));
            stmt.setString(3, String.valueOf(year));
            stmt.setString(4, String.valueOf(month));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                // Create client object from query results
                Client c = new Client(rs.getInt("customerId"), rs.getString("customerName"), 
                                      rs.getString("address1"),rs.getString("address2"),rs.getString("city"),
                                      rs.getString("postalCode"),rs.getString("country"), rs.getString("phone"));
                LocalDate meetingDate = rs.getDate("meetingDate").toLocalDate();
                // Convert times from UTC to local for display
                LocalDateTime start = utcToLocal(meetingDate, rs.getTime("startTime").toLocalTime());
                LocalDateTime end = utcToLocal(meetingDate, rs.getTime("endTime").toLocalTime());
                LocalTime startTime = start.toLocalTime();//rs.getTime("startTime").toLocalTime();
                LocalTime endTime = end.toLocalTime();//rs.getTime("endTime").toLocalTime();
                appointments.add(new Appointment(c,rs.getString("type"), rs.getString("description"), meetingDate, startTime, endTime,rs.getString("location")));
            }
        
        } catch (SQLException e){
            System.out.println("Error occurred with database connection during getAppointmentsMonth method call.");
            System.out.println(e);
        }
        return appointments;
    }
    
    
    /**
     * Adds an appointment to the database
     * @param a
     */
    public static void addAppointment(Appointment a){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        // Get variables to complete database insertion
        int clientID = a.getClient().getClientID(); //customerId
        int userID = Consultant.getUserID(); //userId
        String userName = Consultant.getUserName(); //createdBy & lastUpdateBy
        String desc = a.getDescription(); //description
        String loc = a.getLocation(); //location
        String category = a.getCategory(); //type
        // Convert our time to UTC and pass it to our Timestamp for storage
        Timestamp start = Timestamp.valueOf(localToUTC(a.getDate(),a.getStartTime())); //LocalDateTime.of(a.getDate(),a.getStartTime())); //start
        Timestamp end = Timestamp.valueOf(localToUTC(a.getDate(),a.getEndTime()));//LocalDateTime.of(a.getDate(),a.getEndTime())); // end
        try {
            stmt = con.prepareStatement(Queries.ADD_APPT);
            stmt.setInt(1, clientID); //customerId
            stmt.setInt(2, userID); //userId
            stmt.setString(3, desc); //description
            stmt.setString(4, loc); //location
            stmt.setString(5, category); //type
            stmt.setTimestamp(6, start); //start
            stmt.setTimestamp(7, end); //end
            stmt.setString(8, userName); //createdBy
            stmt.setString(9, userName); //lastUpdateBy
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Database error occurred during addAppointment method call");
        }
    }
    
    /**
     * Updates an existing appointment in the database from the Change Appointment screen
     * @param a
    */
    public static void updateAppointment(Appointment a){
        
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        // variables for update query
        int clientID = a.getClient().getClientID();//customerId
        String description = a.getDescription(); //description
        String location = a.getLocation(); //location
        String category = a.getCategory(); //type
        // Convert our time to UTC and pass it to our Timestamp for storage
        Timestamp start = Timestamp.valueOf(localToUTC(a.getDate(),a.getStartTime()));//Timestamp.valueOf(LocalDateTime.of(a.getDate(),a.getStartTime())); //start
        Timestamp end = Timestamp.valueOf(localToUTC(a.getDate(),a.getEndTime()));//LocalDateTime.of(a.getDate(),a.getEndTime())); // end
        int appointmentID = a.getId();
        System.out.println(appointmentID);
        try {
            stmt = con.prepareStatement(Queries.UPDATE_APPT);
            stmt.setInt(1, clientID);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, category);
            stmt.setTimestamp(5, start);
            stmt.setTimestamp(6, end);
            stmt.setString(7, Consultant.getUserName());
            stmt.setInt(8, appointmentID);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Database error occurred during updateAppointment method call");
            System.out.println(e);
        }
    }
    
    /**
     * Deletes the appointment associated with the given appointment id
     * @param apptID
    */
    public static void deleteAppt(int apptID){
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.DELETE_APPT);
            stmt.setInt(1, apptID);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Database error occurred during deleteAppointment method call");
            System.out.println(e);
        }
    }
    
    /**
     * Checks if there is a scheduling conflict with the given appointment start and end times
     * Assumptions: -1 indicates new appointment, so don't include check for existing appointments in that timeslot;<br>
     *              If it is an existing appointment, checks for conflicts excluding the current appointment in the db
     * @param apptId
     * @param start
     * @param end
     * @return True if there is a scheduling conflict found, otherwise false.
     */
    public static boolean schedulingConflict(int apptId, Timestamp start, Timestamp end){
        int appID = apptId;
        System.out.println("The appointment id is: " + appID);
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        boolean isConflict = false;
        // Convert start and end to UTC before querying database
        Timestamp s = Timestamp.valueOf(localToUTC(start.toLocalDateTime().toLocalDate(), start.toLocalDateTime().toLocalTime()));
        Timestamp e = Timestamp.valueOf(localToUTC(end.toLocalDateTime().toLocalDate(), end.toLocalDateTime().toLocalTime()));
        try{
            // Checking for a scheduling conflict with a new appointment
            if (appID == -1){
                stmt = con.prepareStatement(Queries.SCHEDULE_CONFLICT);
                stmt.setTimestamp(1, s);
                stmt.setTimestamp(2, e);
                stmt.setTimestamp(3, s);
                stmt.setTimestamp(4, e);
                ResultSet rs = stmt.executeQuery();
                if (rs.first()) isConflict = true;
            } 
            // Checking for scheduling conflict with existing appointment
            else{ 
                stmt = con.prepareStatement(Queries.SCHEDULE_CONFLICT_UPDT);
                stmt.setTimestamp(1, s);
                stmt.setTimestamp(2, e);
                stmt.setTimestamp(3, s);
                stmt.setTimestamp(4, e);
                stmt.setInt(5, appID);
                ResultSet rs = stmt.executeQuery();
                return rs.first();
            }
            
            // Check if we have any results. If so, set isConflict = true
            
        }
        catch(SQLException ex){
            System.out.println("Database error occurred during schedulingConflict method call");
            System.out.println(ex);
        }
        
        return isConflict;
    }
    
    /**
     * Helper method to convert local datetime to UTC
     * @param date
     * @param time
     * @return LocalDateTime representation of our system time converted to UTC
     */
    public static LocalDateTime localToUTC(LocalDate date, LocalTime time){
        // convert our parameters to localdatetime
        LocalDateTime ldt = LocalDateTime.of(date, time);
        // convert to a Zoned date time representation of local time
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        // Get the UTC version of our local time
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        // Convert the UTC version to LocalDateTime
        LocalDateTime ldtIn = utczdt.toLocalDateTime();
        return ldtIn;
    }
    
    /**
     * Helper method to convert UTC to local datetime
     * @param date
     * @param time
     * @return LocalDateTime representation of our system time converted from UTC
     */
    public static LocalDateTime utcToLocal(LocalDate date, LocalTime time){
        // convert our parameters to local datetime
        LocalDateTime ldt = LocalDateTime.of(date, time);
        // convert to a Zoned date time representation
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        // Get the our local time with timezone
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        // Convert local datetime with timezone to local datetime without zone
        LocalDateTime ldtIn = utczdt.toLocalDateTime();
        return ldtIn;
    }
}
