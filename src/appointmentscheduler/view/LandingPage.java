/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;

import appointmentscheduler.data.DBConnection;
import appointmentscheduler.logic.Appointment;
import appointmentscheduler.logic.Client;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author marro
 * 
 * This is the main screen for the application. From here users can view
 * their appointments, modify them, schedule new ones, manage clients/contacts,
 * and generate reports.
 */

public class LandingPage extends Scene {
   
    // Tables
    private static final TableView apptByMonth = new TableView();
    private static final TableView apptByWeek = new TableView();
    // Table columns
        // Weekly Table columns
    private final TableColumn<Appointment,Client> WCLIENT_NAME_COL = new TableColumn<>("Client Name");
    private final TableColumn<Appointment,String> WMEETING_CAT_COL = new TableColumn<>("Meeting Category");
    private final TableColumn<Appointment,LocalDate> WDATE_COL = new TableColumn<>("Date");
    private final TableColumn<Appointment,LocalTime> WMEETING_START_COL = new TableColumn<>("Start Time");
    private final TableColumn<Appointment,LocalTime> WMEETING_END_COL = new TableColumn<>("End Time");
    private final TableColumn<Appointment,String> WLOCATION_COL = new TableColumn<>("Location");
        // Monthly Table Columns
    private final TableColumn<Appointment,Client> MCLIENT_NAME_COL = new TableColumn<>("Client Name");
    private final TableColumn<Appointment,String> MMEETING_CAT_COL = new TableColumn<>("Meeting Category");
    private final TableColumn<Appointment,LocalDate> MDATE_COL = new TableColumn<>("Date");
    private final TableColumn<Appointment,LocalTime> MMEETING_START_COL = new TableColumn<>("Start Time");
    private final TableColumn<Appointment,LocalTime> MMEETING_END_COL = new TableColumn<>("End Time");
    private final TableColumn<Appointment,String> MLOCATION_COL = new TableColumn<>("Location");
    // Tab pane to hold tables
    private final TabPane tabPane = new TabPane();
    // Tabs
    private final Tab week = new Tab("Week");
    private final Tab month = new Tab("Month");
    // Buttons
    private static final Button SCHEDULE_APPT_BUTTON = new Button("Schedule Appointment");
    private static final Button CHANGE_APPT_BUTTON = new Button("Change Appointment");
    private static final Button MANAGE_CLIENTS_BUTTON = new Button("Manage Clients");
    private static final Button REPORTS_BUTTON = new Button("Reports");
    private static final Button LOGOUT_BUTTON = new Button("Log Out");
    private static final Button NEXT_WEEK_BUTTON = new Button(">>");
    private static final Button NEXT_MONTH_BUTTON = new Button(">>");
    private static final Button PREV_WEEK_BUTTON = new Button("<<");
    private static final Button PREV_MONTH_BUTTON = new Button("<<");
    // General settings for page
    public static final String TITLE = "Home";
    public static final int WIDTH = 700;
    public static final int HEIGHT = 550;
    // Holder for selected appointment in window
    public static Appointment selectedAppointment;
    // Appointment list for display purposes
    ObservableList<Appointment> weekAppts = FXCollections.observableArrayList(Appointment.getAppointmentsWeek());
    ObservableList<Appointment> monthAppts = FXCollections.observableArrayList(Appointment.getAppointmentsMonth());
    // variables to store the dates with the current focus for our display
    private LocalDate today = Appointment.TODAY;
    private int currMonth = today.getMonthValue();
    private int currYear = today.getYear();
    // temporary holder to keep track of date for month button
    private LocalDate temp = today;
    public LandingPage(GridPane root, int width, int height) {
        super(root,WIDTH,HEIGHT);
            // Buttons on right side of screen
            TilePane buttons = new TilePane(); // holder for buttons
            buttons.setVgap(10);
            buttons.setHgap(10);
            buttons.setPadding(new Insets(0,50,0,0));
            buttons.setPrefColumns(1);
            buttons.getChildren().addAll(SCHEDULE_APPT_BUTTON,CHANGE_APPT_BUTTON,MANAGE_CLIENTS_BUTTON,REPORTS_BUTTON,LOGOUT_BUTTON);
            SCHEDULE_APPT_BUTTON.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            CHANGE_APPT_BUTTON.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            MANAGE_CLIENTS_BUTTON.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            REPORTS_BUTTON.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            LOGOUT_BUTTON.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            root.add(buttons, 1, 0);
            // Set button actions
            SCHEDULE_APPT_BUTTON.setOnAction(e -> clicked(e)); 
            CHANGE_APPT_BUTTON.setOnAction(e -> clicked(e));  
            MANAGE_CLIENTS_BUTTON.setOnAction(e -> clicked(e));
            REPORTS_BUTTON.setOnAction(e -> clicked(e));                
            LOGOUT_BUTTON.setOnAction(e -> clicked(e));
            PREV_WEEK_BUTTON.setOnAction(e -> {
                 // Subtract one week to current date
                today = today.minusWeeks(1);
                //Clear info from table
                apptByWeek.getItems().clear();
                weekAppts.clear();
                // add info back
                weekAppts = FXCollections.observableArrayList(Appointment.getAppointmentsWeek(today.toString()));
                apptByWeek.setItems(weekAppts);
                System.out.println(today.toString());
            });
            NEXT_WEEK_BUTTON.setOnAction(e ->{
                // Add one week to current date
                today = today.plusWeeks(1);
                //Clear info from table
                apptByWeek.getItems().clear();
                weekAppts.clear();
                // add info back
                weekAppts = FXCollections.observableArrayList(Appointment.getAppointmentsWeek(today.toString()));
                apptByWeek.setItems(weekAppts);
                System.out.println(today.toString());
            });
            PREV_MONTH_BUTTON.setOnAction(e -> {
                // set the date behind one month
                temp = temp.minusMonths(1);
                // extract month and year values
                currMonth = temp.getMonthValue();
                currYear = temp.getYear();
                // Clear info from table
                apptByMonth.getItems().clear();
                monthAppts.clear();
                // add info back
                monthAppts = FXCollections.observableArrayList(Appointment.getAppointmentsMonth(currMonth, currYear));
                apptByMonth.setItems(monthAppts);
                System.out.println("The number of month is: " + currMonth + " and the year is " + currYear);
            });
            NEXT_MONTH_BUTTON.setOnAction(e -> {                
                // set the date ahead one month
                temp = temp.plusMonths(1);
                // extract month and year values
                currMonth = temp.getMonthValue();
                currYear = temp.getYear();
                // Clear info from table
                apptByMonth.getItems().clear();
                monthAppts.clear();
                // add info back
                monthAppts = FXCollections.observableArrayList(Appointment.getAppointmentsMonth(currMonth, currYear));
                apptByMonth.setItems(monthAppts);
                System.out.println("The number of month is: " + currMonth + " and the year is " + currYear);
            });
            root.setHgap(100);
            // Appointments - Left side of screen
            apptByWeek.setItems(weekAppts);
            apptByMonth.setItems(monthAppts);
            week.setContent(apptByWeek);
            month.setContent(apptByMonth);
            tabPane.getTabs().addAll(week,month);
            tabPane.setTabClosingPolicy(tabPane.getTabClosingPolicy().UNAVAILABLE); // don't allow user to close tabs
            // Set Up Column Properties
            // Weekly
            WCLIENT_NAME_COL.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            WMEETING_CAT_COL.setCellValueFactory(new PropertyValueFactory<>("category"));
            WDATE_COL.setCellValueFactory(new PropertyValueFactory<>("date"));
            WMEETING_START_COL.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            WMEETING_END_COL.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            WLOCATION_COL.setCellValueFactory(new PropertyValueFactory<>("location"));
            // Monthly columns
            MCLIENT_NAME_COL.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            MMEETING_CAT_COL.setCellValueFactory(new PropertyValueFactory<>("category"));
            MDATE_COL.setCellValueFactory(new PropertyValueFactory<>("date"));
            MMEETING_START_COL.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            MMEETING_END_COL.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            MLOCATION_COL.setCellValueFactory(new PropertyValueFactory<>("location"));
            // Add columns to tables
            apptByWeek.getColumns().addAll(WCLIENT_NAME_COL,WMEETING_CAT_COL,WDATE_COL,WMEETING_START_COL,WMEETING_END_COL,WLOCATION_COL);
            apptByMonth.getColumns().addAll(MCLIENT_NAME_COL,MMEETING_CAT_COL,MDATE_COL,MMEETING_START_COL,MMEETING_END_COL,MLOCATION_COL);
            // Make weekly meetings clickable 
            //Using a Lambda here allows for a more efficient way of setting up the row factor rather than writing two separate functions
            apptByWeek.setRowFactory(tv ->{
                TableRow<Appointment> row = new TableRow<>();
                row.setOnMouseClicked(e ->{
                    if (!row.isEmpty() && e.getButton() == MouseButton.PRIMARY){
                        selectedAppointment = row.getItem();
                    }
                });
                return row;
            });
            // Make monthly meetings clickable
            //Using a Lambda here allows for a more efficient way of setting up the row factor rather than writing two separate functions
            apptByMonth.setRowFactory(tv ->{
                TableRow<Appointment> row = new TableRow<>();
                row.setOnMouseClicked(e ->{
                    if (!row.isEmpty() && e.getButton() == MouseButton.PRIMARY){
                        selectedAppointment = row.getItem();
                    }
                });
                return row;
            });
            // Set up positioning on screen and control elements for next/prev week and next/prev month
            root.add(tabPane, 0, 0);
            HBox nextButtonWeek = new HBox(5);
            nextButtonWeek.getChildren().addAll(PREV_WEEK_BUTTON, NEXT_WEEK_BUTTON);//, PREV_MONTH_BUTTON, NEXT_MONTH_BUTTON);
            VBox weekControls = new VBox(5);
            HBox weekLabel = new HBox(5);
            weekLabel.getChildren().add(new Label("Week \n(prev/next)"));
            weekLabel.setAlignment(Pos.CENTER);
            weekControls.getChildren().addAll(weekLabel,nextButtonWeek);
            HBox nextButtonMonth = new HBox(5);
            nextButtonMonth.getChildren().addAll(PREV_MONTH_BUTTON,NEXT_MONTH_BUTTON);
            HBox monthLabel = new HBox(5);
            monthLabel.getChildren().addAll(new Label("Month \n(prev/next)"));
            monthLabel.setAlignment(Pos.CENTER);
            VBox monthControls = new VBox(5);
            monthControls.getChildren().addAll(monthLabel,nextButtonMonth);
            HBox tableControls = new HBox(20);
            tableControls.getChildren().addAll(weekControls,monthControls);
            root.add(tableControls,0, 1);
            root.setAlignment(Pos.CENTER_RIGHT);
            root.setPadding(new Insets(25));
    }
    /**
     * Controller to manage responses to button presses on landing page
     * @param e 
     */
    private void clicked(ActionEvent e) {
        Stage stage = AppointmentScheduler.getStage();
        if (e.getSource() == MANAGE_CLIENTS_BUTTON){
            stage.setScene(new ManageClientScreen(new BorderPane(),ManageClientScreen.WIDTH,ManageClientScreen.HEIGHT, false));
            stage.setTitle(ManageClientScreen.TITLE);
            clearTables();
            stage.show();
        }
        if (e.getSource() == SCHEDULE_APPT_BUTTON){
            stage.setScene(new ManageApptScreen(new BorderPane(),ManageApptScreen.WIDTH,ManageApptScreen.HEIGHT, false, false));
            stage.setTitle(ManageApptScreen.TITLE);
            clearTables();
            stage.show();
        }
        if (e.getSource() == CHANGE_APPT_BUTTON){
            selectedAppointment.setId(); // Set the appointment ID to the appointment for easier processing on change appointment screen
            stage.setScene(new ManageApptScreen(new BorderPane(),ManageApptScreen.WIDTH,ManageApptScreen.HEIGHT,false, true));
            stage.setTitle("Change Appointment");
            clearTables();
            stage.show();
        }
        if (e.getSource() == REPORTS_BUTTON){
            stage.setScene(new ReportScreen(new BorderPane(),ReportScreen.WIDTH,ReportScreen.HEIGHT));
            stage.setTitle(ReportScreen.TITLE);
            clearTables();
            stage.show();
        }
        if (e.getSource() == LOGOUT_BUTTON){
            // Close the database connection
            DBConnection.closeConnection();
            // Give logout confirmation message
            AlertBox.display("Logoff", "You have successfully logged out. Goodbye.");
            // Close the program
            AppointmentScheduler.getStage().close();
        }
    }
    
    /**
     * Returns to this landing page 
     */
    public static void returnToLandingPage(){
        Stage stage = AppointmentScheduler.getStage();
        stage.setScene(new LandingPage(new GridPane(),LandingPage.WIDTH,LandingPage.HEIGHT));
        stage.setTitle(LandingPage.TITLE);
        stage.show();
    }
    /**
     * Clears the data from the Appointment by Week and Appointment by Month tables on the Landing Page
     */
    private void clearTables(){
        apptByMonth.getItems().clear();
        apptByMonth.getColumns().clear();
        apptByWeek.getItems().clear();
        apptByWeek.getColumns().clear();
    }
    
   
}
