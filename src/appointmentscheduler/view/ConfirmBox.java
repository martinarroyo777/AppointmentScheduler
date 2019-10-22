/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generates a confirmation dialogue box for user actions 
 * @author marro
 */
public class ConfirmBox {
    public static boolean response;
    
    public static void display(String title, String message){
        Stage window = new Stage();
        window.setMinWidth(250);
        window.initModality(Modality.APPLICATION_MODAL);
        // Create buttons
        Button ok = new Button("OK");
        Button cancel = new Button("CANCEL");
        // Add button actions
        ok.setOnAction(e ->{
            response = true;
            window.close();
        });
        cancel.setOnAction(e ->{
            response = false;
            window.close();
        });
        // Label for message
        Label output = new Label(message);
        // Store buttons for layout
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(ok,cancel);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        // Store label and buttons
        VBox layout = new VBox(5);
        layout.setPadding(new Insets(25,25,25,25));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(output,buttons);
        // Launch window
        window.setScene(new Scene(layout));
        window.showAndWait();
        
    }
}
