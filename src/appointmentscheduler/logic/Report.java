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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * Holder class for displaying reports in reports screen
 * @author martinarroyo
 */
public class Report {
    
    private Report(){}    
    // TODO catch the case where a user inputs a bad year using another error handling mechanism
        /**
         * Generates Appointments by Month report
         * @param month
         * @param year
         * @return An ArrayList containing the lines for the Appointments by Month report
         */
    public static ArrayList<Report.ApptTypeByMonth> apptByMonth(String month, String year) {
        // Generate data holder
        ArrayList <Report.ApptTypeByMonth> results = new ArrayList<>();
        // Convert inputs to int
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);
        // Establish DB connection 
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.APPTYPES_BY_MONTH); // takes in month and year as parameters
            stmt.setInt(1, m);
            stmt.setInt(2, y);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Report.ApptTypeByMonth line = new Report().new ApptTypeByMonth();
                line.setAppointmentType(rs.getString("type"));
                line.setCount(rs.getInt("number"));
                results.add(line);
            }
        }
        catch(SQLException e){
            System.out.println("Database error occurred during apptByMonth method call.");
            System.out.println(e);
        }
        return results;
    }
    
    
    /**
     * Generates Appointment by Consultant report
     * @param consultant
     * @return ArrayList of current appointments for the given consultant
     */
    public static ArrayList<Report.Report2> apptByConsultant(String consultant) {
        // Generate data holder
        ArrayList <Report.Report2> results = new ArrayList<>();
        // Get userId
        int userId = Consultant.getConsultantID(consultant);
        // Establish DB connection 
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.APPTS_BY_CONSULTANT); // takes in userId as parameter
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Report.Report2 line = new Report().new Report2();
                // Set up data in our line
                line.setClientName(rs.getString("customerName")); // client name
                line.setCategory(rs.getString("type")); // category
                line.setDescription(rs.getString("description"));
                line.setLocation(rs.getString("location"));
                line.setDate(rs.getDate("meetingDate").toLocalDate());
                line.setStartTime(rs.getTime("startTime").toLocalTime());
                line.setEndTime(rs.getTime("endTime").toLocalTime());
                // add to result set
                results.add(line);
            }
        }
        catch(SQLException e){
            System.out.println("Database error occurred during apptByConsultant method call.");
            System.out.println(e);
        }
        return results;
    }
    
    /**
     * Generates Appointment by Client report
     * @param client
     * @return ArrayList of current appointments for the given client
     */
    public static ArrayList<Report.Report3> apptByClient(String client) {
        // Generate data holder
        ArrayList <Report.Report3> results = new ArrayList<>();
 
        // Establish DB connection 
        Connection con = DBConnection.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(Queries.APPTS_BY_CLIENT); // takes in customerName as parameter
            stmt.setString(1, client);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Report.Report3 line = new Report().new Report3();
                // Set up data in our line
                line.setClientName(rs.getString("customerName")); // client name
                line.setConsultantName(rs.getString("consultant")); // consultant
                line.setCategory(rs.getString("type")); // category
                line.setDescription(rs.getString("description"));
                line.setLocation(rs.getString("location"));
                line.setDate(rs.getDate("meetingDate").toLocalDate());
                line.setStartTime(rs.getTime("startTime").toLocalTime());
                line.setEndTime(rs.getTime("endTime").toLocalTime());
                // add to result set
                results.add(line);
            }
        }
        catch(SQLException e){
            System.out.println("Database error occurred during apptByClient method call.");
            System.out.println(e);
        }
        return results;
    }
    
    
    
    
    /**
     * Holder class for Appointment by Month report
     */        
    public class ApptTypeByMonth{
        private String appointmentType;
        private int count;
        
        public String getAppointmentType(){
            return this.appointmentType;
        }
        public int getCount(){
            return this.count;
        }
        public void setAppointmentType(String type){
            this.appointmentType = type;
        }
        public void setCount(int c){
            this.count = c;
        }
    }
    /**
     * Holder class for Appointment by Consultant report
     */
    public class Report2{
        private String clientName;
        private String category;
        private String description;
        private String location;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        
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
        public String getDescription(){
            return this.description;
        }
        public void setDescription(String desc){
            this.description = desc;
        }
        public String getLocation(){
            return this.location;
        }
        public void setLocation(String loc){
            this.location = loc;
        }
        public LocalDate getDate(){
            return this.date;
        }
        public void setDate(LocalDate day){
            this.date = day;
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
    }
    
    /**
     * Holder class for Report 3 - Appointment by Client
     */
    public class Report3{
        private String clientName;
        private String consultantName;
        private String category;
        private String description;
        private String location;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        
        public String getClientName(){
            return this.clientName;
        }
        public void setClientName(String name){
            this.clientName = name;
        }
        public String getConsultantName(){
            return this.consultantName;
        }
        public void setConsultantName(String name){
            this.consultantName = name;
        }
        public String getCategory(){
            return this.category;
        }
        public void setCategory(String cat){
            this.category = cat;
        }
        public String getDescription(){
            return this.description;
        }
        public void setDescription(String desc){
            this.description = desc;
        }
        public String getLocation(){
            return this.location;
        }
        public void setLocation(String loc){
            this.location = loc;
        }
        public LocalDate getDate(){
            return this.date;
        }
        public void setDate(LocalDate day){
            this.date = day;
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
    
    }
}
