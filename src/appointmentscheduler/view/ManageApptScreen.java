/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;


import appointmentscheduler.logic.Appointment;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 
 * @author marro
 * 
 * Screen where user can add, modify, or delete appointments
 */
public class ManageApptScreen extends Scene{
    private static final Label NAME = new Label("Name");
    private static final Label MEETING_TYPE = new Label("Type of Meeting");
    private static final Label DESCRIPTION = new Label("Description");
    private static final Label LOCATION = new Label("Location");
    private static final Label DATE = new Label("Date");
    private static final Label START = new Label("Start Time");
    private static final Label END = new Label("End Time");
    // Text Fields
    private static final TextField NAME_FIELD = new TextField();
    private static final TextField MEETING_TYPE_FIELD = new TextField();
    private static final TextField DESCRIPTION_FIELD = new TextField(); 
    private static final TextField LOCATION_FIELD = new TextField();
    private static final DatePicker DATE_FIELD = new DatePicker();
    private static final ComboBox START_HOUR = new ComboBox();
    private static final ComboBox START_MINUTE = new ComboBox();
    private static final ComboBox END_HOUR = new ComboBox();
    private static final ComboBox END_MINUTE = new ComboBox();
    private static final ComboBox AM_PM_START = new ComboBox();
    private static final ComboBox AM_PM_END = new ComboBox();
    private static final ArrayList<TextField> fields = new ArrayList<>(Arrays.asList(NAME_FIELD,MEETING_TYPE_FIELD,DESCRIPTION_FIELD,LOCATION_FIELD));
    // Buttons
    private static final Button ADD_CLIENT = new Button("Find/Add Client"); // this button will open up the Manage Client Screen
    private static final Button SAVE = new Button("Save");
    private static final Button CANCEL = new Button("Cancel");
    private static final Button DELETE = new Button("Delete");
    // General configuration settings
    public static final String TITLE = "Schedule Appointment";
    private GridPane innerGrid = new GridPane();
    public static final int WIDTH = 550;
    public static final int HEIGHT = 500;
    // Flag to determine if we arrived here from CMS
    private boolean fromCMS = false;
    // Flag to determine if we are changing an existing appointment
    private boolean changeAppt = false;
    // Which name to use for name field depending on where we came into the screen from
    private StringBuilder nameFieldVal = new StringBuilder("");
    
    public ManageApptScreen(BorderPane root, int width, int height, boolean fromCMS, boolean changeAppt){
        super(root,width,height);
        // Determine whether screen was entered from CMS or Landing Page
        this.fromCMS = fromCMS;
        // Determine if we are modifying an appointment
        this.changeAppt = changeAppt;
        /* 
            Set up Center of BorderPane
        */
        // Create and set up inner grid pane
        innerGrid.setHgap(10);
        innerGrid.setVgap(10);
        innerGrid.setPadding(new Insets(25,25,25,25));
        // Set labels up
        NAME.setMinWidth(Control.USE_PREF_SIZE);
        MEETING_TYPE.setMinWidth(Control.USE_PREF_SIZE);
        DESCRIPTION.setMinWidth(Control.USE_PREF_SIZE);
        LOCATION.setMinWidth(Control.USE_PREF_SIZE);
        DATE.setMinWidth(Control.USE_PREF_SIZE);
        START.setMinWidth(Control.USE_PREF_SIZE);
        END.setMinWidth(Control.USE_PREF_SIZE);
        // Create HBox for Save and Cancel
        HBox buttons = new HBox(5);
        SAVE.setOnAction(e -> {
            try{
                save(e);
            }catch(NullPointerException ex){
                AlertBox.display("Invalid Form Entry", "Please fill out form in order to save appointment.");
            }});
        CANCEL.setOnAction(e -> cancel(e));
        buttons.getChildren().addAll(SAVE,CANCEL);
        // Set TextFields up
        // If we have information from CMS, populate NAME_FIELD with name, else use default value
        if (fromCMS){
            if (ManageClientScreen.getCurrentClient() != null){
                this.nameFieldVal.append(ManageClientScreen.getCurrentClient().getName());
            } else {
                this.nameFieldVal.append("Name");
            }
        }
        // If we're modifying an appointment then get the client name and other information for it 
        else if (changeAppt){
            this.nameFieldVal.append(LandingPage.selectedAppointment.getClientName());
            // Set the current client in the ManageClientScreen to the client in the appointment
            ManageClientScreen.setCurrentClient(LandingPage.selectedAppointment.getClient());
            MEETING_TYPE_FIELD.setText(LandingPage.selectedAppointment.getCategory());
            DESCRIPTION_FIELD.setText(LandingPage.selectedAppointment.getDescription());
            LOCATION_FIELD.setText(LandingPage.selectedAppointment.getLocation());
            DATE_FIELD.setValue(LandingPage.selectedAppointment.getDate());
            String [] startTime = convertTime24to12(LandingPage.selectedAppointment.getStartTime().toString());
            String [] endTime =convertTime24to12(LandingPage.selectedAppointment.getEndTime().toString());
            START_HOUR.setValue(startTime[0]);
            START_MINUTE.setValue(startTime[1]);
            AM_PM_START.setValue(startTime[2]);
            END_HOUR.setValue(endTime[0]);
            END_MINUTE.setValue(endTime[1]);
            AM_PM_END.setValue(endTime[2]);
            // Add delete button to the screen
            DELETE.setOnAction(e -> delete(e));
            buttons.getChildren().add(DELETE);
            System.out.println(Arrays.toString(startTime)); // testing
            System.out.println(Arrays.toString(endTime)); // testing
            
        } 
        NAME_FIELD.setPromptText(nameFieldVal.toString());
        NAME_FIELD.setDisable(true);
        MEETING_TYPE_FIELD.setPromptText("Type of Meeting");
        DESCRIPTION_FIELD.setPromptText("Description");
        LOCATION_FIELD.setPromptText("Location");
        DATE_FIELD.setPromptText("Date");
        // Set Up Hour/Min fields for start and end times
        setHourValues(START_HOUR);
        setMinuteValues(START_MINUTE);
        setHourValues(END_HOUR);
        setMinuteValues(END_MINUTE);
        AM_PM_START.getItems().addAll("AM","PM");
        AM_PM_END.getItems().addAll("AM","PM");
        HBox startHMin = new HBox(5);
        HBox endHMin = new HBox(5);
        startHMin.getChildren().addAll(START_HOUR, new Label(":"), START_MINUTE, AM_PM_START);
        endHMin.getChildren().addAll(END_HOUR, new Label(":"), END_MINUTE, AM_PM_END);
        // Add fields and labels to grid
        innerGrid.add(NAME,0,1);
        HBox findClientField = new HBox(5);
        findClientField.getChildren().addAll(NAME_FIELD,ADD_CLIENT);
        innerGrid.add(findClientField,1,1);
        innerGrid.add(MEETING_TYPE,0,2);
        innerGrid.add(MEETING_TYPE_FIELD,1,2);
        innerGrid.add(DESCRIPTION,0,3);
        innerGrid.add(DESCRIPTION_FIELD,1,3);
        innerGrid.add(LOCATION,0,4);
        innerGrid.add(LOCATION_FIELD,1,4);
        innerGrid.add(DATE,0,5);
        innerGrid.add(DATE_FIELD,1,5);
        innerGrid.add(START,0,6);
        innerGrid.add(startHMin,1,6);
        innerGrid.add(END,0,7);
        innerGrid.add(endHMin,1,7);
        root.setCenter(innerGrid);
        root.setBottom(buttons);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        root.setPadding(new Insets(25,25,25,25));
        // Open Client Management Screen
        ADD_CLIENT.setOnAction(e -> openClientManager(e));   
    }
    
    /**
        Saves appointment to the database
    
        Business hours are 8am to 5pm. The program will not save an appointment
        that falls outside of those constraints.
        
        It also checks to make sure that the appointment will not overlap with
        another current appointment
        * @param e
    */
    private void save(ActionEvent e) throws NullPointerException{
        // Convert start and end times to 24 hour format, then to LocalTime
        LocalTime start = convertTime(convertTime12to24(START_HOUR.getValue()
                        .toString(), START_MINUTE.getValue().toString(), 
                        AM_PM_START.getValue().toString()));
        LocalTime end = convertTime(convertTime12to24(END_HOUR.getValue()
                        .toString(), END_MINUTE.getValue().toString(), 
                        AM_PM_END.getValue().toString()));
        LocalDate day = convertDate(DATE_FIELD);
        // Test to make sure appointment is within business hours
        if (isBusinessHours(start) && isBusinessHours(end) && validTimes() && validEntries()){
            // Test to make sure appointment does not overlap with other appointments
            if(!isOverlap(day,start,end)){
                // If we are updating an appointment, call the updateAppointment method
                if (changeAppt){
                    // Update the appointment with any new information from fields
                    LandingPage.selectedAppointment.setClient(ManageClientScreen.getCurrentClient());
                    LandingPage.selectedAppointment.setCategory(MEETING_TYPE_FIELD.getText());
                    LandingPage.selectedAppointment.setDescription(DESCRIPTION_FIELD.getText());
                    LandingPage.selectedAppointment.setDate(day);
                    LandingPage.selectedAppointment.setStartTime(start);
                    LandingPage.selectedAppointment.setEndTime(end);
                    LandingPage.selectedAppointment.setLocation(LOCATION_FIELD.getText());
                    // Make the update
                    Appointment.updateAppointment(LandingPage.selectedAppointment);
                    // Confirm to user that appointment was added
                    AlertBox.display("Success", "Appointment Updated");
                    // return to landing page
                    clearFields();
                    LandingPage.returnToLandingPage();
                } else {
                    // Add appointment to DB/List
                    Appointment a = new Appointment(ManageClientScreen.getCurrentClient(),
                            MEETING_TYPE_FIELD.getText(), DESCRIPTION_FIELD.getText(),day,start,end,LOCATION_FIELD.getText());
                    Appointment.addAppointment(a);
                    // Confirm to user that appointment was added
                    AlertBox.display("Success", "Appointment Saved");
                    // return to landing page
                    clearFields();
                    LandingPage.returnToLandingPage();
                }
            } 
            // Notify user that there is a scheduling conflict
            else {
                AlertBox.display("Scheduling Conflict", "There is a scheduling conflict with your selected meeting time. Please see schedule and select a different time.");
            }
        } 
        // Appointment time selected is not within business hours. Pop a message to inform user
        else {
            if (!validEntries()){
                AlertBox.display("Form Incomplete", "A client, meeting type, date, start and end time are required in order to save an appointment.");
            }
            if (!validTimes()){
                AlertBox.display("Invalid Time Selection", "Start time cannot be after end time. Please check your selection and try again.");
            }
            if (!(isBusinessHours(start) && isBusinessHours(end))){
                AlertBox.display("Outside of Business Hours", "Business hours are between 8:00am and 5:00pm. Please adjust your selection and try again.");
            }
        }
        
        
    }
    
    /**
        Clear fields and return to Landing Page
    */
    private void cancel(ActionEvent e){
        clearFields();
        LandingPage.returnToLandingPage();
    }
    
    /**
     * Deletes the current appointment from the database
    */
    private void delete(ActionEvent e){
        // Get the current appointment id
        int appointmentId = LandingPage.selectedAppointment.getId();
        // pop a confirm box dialogue warning user that this is a permanent delete
        ConfirmBox.display("Delete Current Appointment", "Are you sure you want to delete this appointment? Operation cannot be undone.");
        // if confirmed, proceed with deletion and clear screen
        if (ConfirmBox.response){
            // delete appointment
            Appointment.deleteAppt(appointmentId);
            // Give alert that the delete has been completed
            AlertBox.display("Delete Confirmation", "The appointment has been successfully deleted.");
            // clear fields
            clearFields();
            // return to landing page
            LandingPage.returnToLandingPage();
        }
        
    }
            
  
    /**
        Helper method to clear fields
    */
    private void clearFields(){
        // Clear text fields
        fields.forEach(e -> e.clear());
        // Clear Combo Boxes
        START_HOUR.getItems().clear();
        START_MINUTE.getItems().clear();
        END_HOUR.getItems().clear();
        END_MINUTE.getItems().clear();
        AM_PM_START.getItems().clear();
        AM_PM_END.getItems().clear();
    }
    
    /**
        Opens Client Management Screen when user clicks on 'Find/Add Client'
    */
    private void openClientManager(ActionEvent e){
        Stage stage = AppointmentScheduler.getStage();
        stage.setScene(new ManageClientScreen(new BorderPane(),ManageClientScreen.WIDTH,ManageClientScreen.HEIGHT, true));
        stage.setTitle(ManageClientScreen.TITLE);
        stage.show();
    }
    
    /**
        Helper method to provide values for hours
    */
    private void setHourValues(ComboBox cb){
        cb.getItems().addAll("12","01","02","03","04","05","06","07",
                             "08","09","10","11");
    }
    
    /**
        Helper method to provide values for minutes
    */
    private void setMinuteValues(ComboBox cb){
        for (int i=0; i<60; i++){
            // HH:00 - HH:09
            if (i < 10){
                cb.getItems().add("0" + Integer.toString(i));
            }
            // HH:10 - HH:59
            else{
                cb.getItems().add(Integer.toString(i));
            }
        }
    }
    /**
     * Helper method to determine if start time is after end time
     * @return True if the start period is earlier than the end period. False otherwise.
     */
    private boolean validTimes(){
        LocalTime start = convertTime(convertTime12to24(START_HOUR.getValue()
                        .toString(), START_MINUTE.getValue().toString(), 
                        AM_PM_START.getValue().toString()));
        LocalTime end = convertTime(convertTime12to24(END_HOUR.getValue()
                        .toString(), END_MINUTE.getValue().toString(), 
                        AM_PM_END.getValue().toString()));
        return start.isBefore(end);
    }
    /**
     * Helper method to determine if fields are complete
     * Required fields for an appointment are: NAME_FIELD, MEETING_TYPE_FIELD, DATE_FIELD, 
     * START_HOUR, START_MINUTE, AM_PM_START, END_HOUR, END_MINUTE, AM_PM_END 
     * 
     * @return True if the required fields are filled out. False otherwise.
     */
    private boolean validEntries(){
        return ManageClientScreen.getCurrentClient() != null && MEETING_TYPE_FIELD.getLength() > 0 && DATE_FIELD.getValue() != null
                && START_HOUR.getValue() != null && START_MINUTE.getValue() !=null &&
                AM_PM_START.getValue() != null && END_HOUR.getValue() != null
                && END_MINUTE.getValue() != null && AM_PM_END.getValue() != null;
    }
    /**
        Helper method to convert 12 hour format to 24 hour format
        @params hour, min, meridiem
    */
    private String convertTime12to24(String hour, String min, String meridiem){
        String time12hour = hour + ":" + min + " " + meridiem;
        String time24hour = LocalTime.parse(time12hour, 
                DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
                .format(DateTimeFormatter.ofPattern("HH:mm"));
        return time24hour;
    }
    /**
     * Helper method to convert 24 hour format to 12 hour format for display purposes
    */
    private String [] convertTime24to12(String time){
        
        String time12hour = LocalTime.parse(time, 
                DateTimeFormatter.ofPattern("HH:mm", Locale.US))
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
        // extract hour, minute and meridiem values
        String hour = time12hour.substring(0,2);
        String min = time12hour.substring(3,5);
        String meridiem = time12hour.substring(6);
        String [] time12 = {hour,min,meridiem};
        return time12;
    }
    /**
        Helper method to convert String to LocalTime
    */
    private LocalTime convertTime(String ts){
        LocalTime time = LocalTime.parse(ts);
        return time;
    }
    /**
        Helper method to convert DatePicker to LocalDate
    */
    private LocalDate convertDate(DatePicker d){
        LocalDate date = d.getValue();
        return date;
    }
    
    /**
        * Helper method to determine if appointment time falls outside of normal business hours.<br>
        * Checks to see if the given time falls between business hours (24 hr format).
        * @param t
        * @return True if given time(t) is within business hours (8:00 am - 5:00 pm)
    */
    private boolean isBusinessHours(LocalTime t){
        return t.isAfter(LocalTime.of(7,59)) && t.isBefore(LocalTime.of(17, 01));
    }
    
    /**
        * Helper method to find if there is a scheduling conflict based on given day, start and end times
        * @param day
        * @param start
        * @param end
        * @return True if there is a scheduling conflict; otherwise false.
    */
    private boolean isOverlap(LocalDate day, LocalTime start, LocalTime end){
        // TODO needs to pass in appointment id (?)
        int apptId = LandingPage.selectedAppointment != null ? LandingPage.selectedAppointment.getId(): -1;
       // Take parameters and convert to Timestamp to pass into DB query
       Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(day,start));
       Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(day,end));
       // Check DB for overlap
       return Appointment.schedulingConflict(apptId,startTime, endTime);
    }
    
}
