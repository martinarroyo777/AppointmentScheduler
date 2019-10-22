/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler;

import appointmentscheduler.view.LogInPage;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author marro
 */
public class AppointmentScheduler extends Application {
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene logInScreen = new LogInPage();
        stage.setTitle(LogInPage.TITLE);
        stage.setScene(logInScreen);
        stage.show();
    }
 // Returns the current stage
    public static Stage getStage(){
        return stage;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
