/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;

import appointmentscheduler.logic.Appointment;
import appointmentscheduler.logic.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author marro
 * 
 *      Log-in Screen
 */
public final class LogInPage extends Scene {
    // Labels
    private static Text LOGO;
    private static Label LOGIN;
    private static Label USERNAME;
    private static Label PASSWORD;
    // Fields
    private static final TextField USERNAME_FIELD = new TextField();
    private static final PasswordField PASSWORD_FIELD = new PasswordField();
    // Buttons
    private static final Button SUBMIT = new Button();
    private static final Button EXIT = new Button();
    // General settings for page
    public static String TITLE;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final GridPane ROOT = new GridPane();
    // Locale settings
    Locale locale = Locale.getDefault();
    ResourceBundle bundle = ResourceBundle.getBundle("appointmentscheduler/view/Locale", locale);
    // Welcome message
    private StringBuilder welcomeMessage;
    // Error message
    private String errorTitle;
    private String errorMessage;
    // Log-in time
    LocalDate loginDate;
    LocalTime loginTime;
    // Appointment within 15 minutes message
    private String apptIn15;
    
    public LogInPage(){
        super(ROOT,WIDTH,HEIGHT);
        /*
                Tester: You can uncomment these for Locale testing. Supported locales are en-US and fr-FR
        */
        //setLocale("fr","FR");
        //setLocale("en","US");
         // Set up page items to reflect locale
        LOGO = new Text("AppointmentScheduler");
        LOGIN = new Label(bundle.getString("login"));
        USERNAME = new Label(bundle.getString("username"));
        PASSWORD = new Label(bundle.getString("password"));
        TITLE = bundle.getString("title");
        SUBMIT.setText(bundle.getString("submit"));
        EXIT.setText(bundle.getString("exit"));
        welcomeMessage = new StringBuilder(bundle.getString("welcome_message"));
        errorTitle = bundle.getString("error_title");
        errorMessage = bundle.getString("error_message");
        apptIn15 = bundle.getString("appointment");
        // add logo to top of page
        ROOT.add(LOGO,1,1);
        // set up login section
        VBox logIn = new VBox(5);
        HBox submitBox = new HBox(5);
        submitBox.setPadding(new Insets(7,0,0,0));
        submitBox.getChildren().addAll(SUBMIT,EXIT);
        submitBox.setAlignment(Pos.BOTTOM_RIGHT);
        SUBMIT.setOnAction(e->submit(e));
        EXIT.setOnAction(e->exit(e));
        logIn.setMaxSize(200, 400);
        logIn.getChildren().addAll(LOGIN,USERNAME,USERNAME_FIELD,PASSWORD,PASSWORD_FIELD,submitBox);
        ROOT.add(logIn,1,2);
        ROOT.setAlignment(Pos.CENTER);
        ROOT.setVgap(20);
    }
    
    private void submit(ActionEvent e){
        // Get username and password entered from fields
        String username = USERNAME_FIELD.getText();
        String password = PASSWORD_FIELD.getText();
        // Check authentication
        // Valid authentication
        try{
            if (Authentication.isAuthenticated(username, password)){
                Stage stage = AppointmentScheduler.getStage();
                String welcome = 
                    welcomeMessage + ", " + USERNAME_FIELD.getText()+"!";
                AlertBox.display(welcomeMessage.toString(), welcome);
                // Let user know if they have an appointment within 15 minutes
                if (Appointment.isInFifteen()){
                    AlertBox.display("Reminder", apptIn15);
                }
                stage.setScene(new LandingPage(new GridPane(),LandingPage.WIDTH,LandingPage.HEIGHT));
                stage.setTitle(LandingPage.TITLE);
                stage.show();
            }
            // Invalid authentication
            else {
                AlertBox.display(errorTitle, errorMessage);
                USERNAME_FIELD.clear();
                PASSWORD_FIELD.clear();
            }
        } catch(Exception ex){
            System.out.println("An error has likely occurred with the database. Please check connection.");
            System.out.println(ex.toString());
        }
    }
    
    /**
        Closes the application if user confirms action
    */
    private void exit(ActionEvent e){
        ConfirmBox.display("Exit Program", "Are you sure you want to exit the program?");
        if (ConfirmBox.response) System.exit(0);
    }
    // Helper method to set locale
    // Note to tester: you can change the locale for testing with this function
    private void setLocale(String lang, String region){
        locale = new Locale(lang,region);
        bundle = ResourceBundle.getBundle("appointmentscheduler/view/Locale", locale);
    }
    
    /**
     * Gets the login date as a string
     * @return A string representing the login date
     */
    private String getDate(){
        loginDate = LocalDate.now();
        StringBuilder date = new StringBuilder();
        date.append(loginDate.getDayOfWeek().toString());
        date.append(", ");
        date.append(String.valueOf(loginDate.getDayOfMonth()));
        date.append(" ");
        date.append(loginDate.getMonth().toString());
        date.append(" ");
        date.append(String.valueOf(loginDate.getYear()));
        return date.toString();
    }
    /**
     * 
     * @return A string representation of login time
     */
    private String getTime(){
        loginTime = LocalTime.now();
        return loginTime.toString();
    }
    
    /**
     * Generates the login date and time as a string
     * @return a string representation of login date and time
        in the form DATE - TIME (e.g. Sunday, October 1 2017 - 9:52pm)
     */
    private String getDateTime(){
        return getDate() + " - " + getTime();
    }
}
