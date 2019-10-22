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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generates alert boxes to notify user of a change in state
 * @author marro
 */
public class AlertBox {
    
    public static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle(title);
        Label label = new Label(message);
        Button ok = new Button("OK");
        ok.setOnAction(e -> window.close());
        VBox layout = new VBox(5);
        layout.getChildren().addAll(label,ok);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25,25,25,25));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
    }
}
