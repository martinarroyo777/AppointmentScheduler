/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;

import appointmentscheduler.logic.Address;
import appointmentscheduler.logic.Client;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author marro
 * 
 * Screen where user can add, modify or remove clients from the system
 */

public class ManageClientScreen extends Scene {
    
    private static final Label ID = new Label("ID");
    private static final Label NAME = new Label("Name");
    private static final Label ADDRESS_1 = new Label("Address Line 1");
    private static final Label ADDRESS_2 = new Label("Address Line 2");
    private static final Label CITY = new Label("City");
    private static final Label ZIP = new Label("Zip Code");
    private static final Label COUNTRY = new Label("Country");
    private static final Label PHONE = new Label("Phone");
    // Text Fields
    private static final TextField ID_FIELD = new TextField("Auto Gen - Disabled");
    private static final TextField NAME_FIELD = new TextField();
    private static final TextField ADDRESS_1_FIELD = new TextField();
    private static final TextField ADDRESS_2_FIELD = new TextField(); // Price/Cost Field
    private static final TextField CITY_FIELD = new TextField();
    private static final TextField ZIP_FIELD = new TextField();
    private static final TextField COUNTRY_FIELD = new TextField();
    private static final TextField PHONE_FIELD = new TextField();
    private static final ArrayList<TextField> fields = new ArrayList<>(Arrays.asList(ID_FIELD,NAME_FIELD,ADDRESS_1_FIELD,ADDRESS_2_FIELD,CITY_FIELD,ZIP_FIELD,COUNTRY_FIELD,PHONE_FIELD));
    // Buttons
    private static final Button SAVE = new Button("Save");
    private static final Button CANCEL = new Button("Cancel");
    // General configuration settings
    public static final String TITLE = "Manage Clients";
    private GridPane innerGrid = new GridPane();
    public static final int WIDTH = 550;
    public static final int HEIGHT = 500;
    // Flag to determine if we are working on adding a client or an existing one
    private boolean existingClient = false;
    // Flag to determine if screen is entered from Appointment Screen
    private boolean fromAppt = false;
    // Holder for the current client
    private static Client currClient;
    // Data cache for clients
    private ObservableList<Client> clientList = FXCollections.observableArrayList(Client.clientList()); // May move to constructor during refactor
    
    public ManageClientScreen(BorderPane root, int width, int height, boolean fromApp){
        super(root,width,height);
        // Tells us if we entered this screen from landing page or appt screen
        this.fromAppt = fromApp;
        Button selectClientFromList = new Button("Select Client From List");
        selectClientFromList.setMinWidth(Control.USE_PREF_SIZE);
        selectClientFromList.setOnAction(e->getClientList(e));
        HBox top = new HBox(150);
        top.getChildren().addAll(selectClientFromList);
        root.setTop(top);
        /* 
            Set up Center of BorderPane
        */
        // Create and set up inner grid pane
        innerGrid.setHgap(10);
        innerGrid.setVgap(10);
        innerGrid.setPadding(new Insets(25,25,25,25));
        // Set labels up
        ID.setMinWidth(Control.USE_PREF_SIZE);
        NAME.setMinWidth(Control.USE_PREF_SIZE);
        ADDRESS_1.setMinWidth(Control.USE_PREF_SIZE);
        ADDRESS_2.setMinWidth(Control.USE_PREF_SIZE);
        CITY.setMinWidth(Control.USE_PREF_SIZE);
        ZIP.setMinWidth(Control.USE_PREF_SIZE);
        COUNTRY.setMinWidth(Control.USE_PREF_SIZE);
        PHONE.setMinWidth(Control.USE_PREF_SIZE);        
        // Set TextFields up
        ID_FIELD.setDisable(true);
        ADDRESS_1_FIELD.setPromptText("Address Line 1");
        ADDRESS_2_FIELD.setPromptText("Address Line 2");
        CITY_FIELD.setPromptText("City");
        ZIP_FIELD.setPromptText("Zip");
        COUNTRY_FIELD.setPromptText("Country");
        PHONE_FIELD.setPromptText("Phone");
        // Add fields and labels to grid
        innerGrid.add(ID,0,0);
        innerGrid.add(ID_FIELD,1,0);
        innerGrid.add(NAME,0,1);
        innerGrid.add(NAME_FIELD,1,1);
        innerGrid.add(ADDRESS_1,0,2);
        innerGrid.add(ADDRESS_1_FIELD,1,2);
        innerGrid.add(ADDRESS_2,0,3);
        innerGrid.add(ADDRESS_2_FIELD,1,3);
        innerGrid.add(CITY,0,4);
        innerGrid.add(CITY_FIELD,1,4);
        innerGrid.add(ZIP,0,5);
        innerGrid.add(ZIP_FIELD,1,5);
        innerGrid.add(COUNTRY,0,6);
        innerGrid.add(COUNTRY_FIELD,1,6);
        innerGrid.add(PHONE, 0, 7);
        innerGrid.add(PHONE_FIELD, 1, 7);
        root.setCenter(innerGrid);
        // Create HBox for Save and Cancel
        HBox buttons = new HBox(5);
        SAVE.setOnAction(e -> save(e));
        CANCEL.setOnAction(e -> cancel(e));
        buttons.getChildren().addAll(SAVE,CANCEL);
        root.setBottom(buttons);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        root.setPadding(new Insets(25,25,25,25));
        // Change Save button to Save & Return if coming from Appt Mgmt Screen
        if (fromAppt) SAVE.setText("Save & Return");
    }
    
    /**
     * Sets the currClient variable
     * @param c
     */
    public static void setCurrentClient(Client c){
        currClient = c;
    }
    /**
        Adds client to DB
    */
    private void save(ActionEvent e){
        // Make sure all required fields are filled out before saving
        if (formIsValid()){
            // Check if client currently exists. If so, modify. Otherwise create new entry.
            if (existingClient){
                // Modify the existing currentClient holder -- Change the variable to global currentClient cache holder
                currClient.setName(NAME_FIELD.getText());
                currClient.setAddress1(ADDRESS_1_FIELD.getText());
                currClient.setAddress2(ADDRESS_2_FIELD.getText());
                currClient.setCity(CITY_FIELD.getText());
                currClient.setZip(ZIP_FIELD.getText());
                currClient.setCountry(COUNTRY_FIELD.getText());
                currClient.setPhone(PHONE_FIELD.getText());
                // Update client in database
                Client.modifyClient(currClient, currClient.getClientID());
            }
            // Create new client entry
            else { 
               Client c = new Client (NAME_FIELD.getText(),ADDRESS_1_FIELD.getText(),
                               ADDRESS_2_FIELD.getText(),CITY_FIELD.getText(),
                               ZIP_FIELD.getText(),COUNTRY_FIELD.getText(),PHONE_FIELD.getText());
               Client.addNewClient(c);
            }

            existingClient = false; // reset existing client flag
            clearFields(); // remove data from fields
            // Return to Appt Mgmt Screen if we came from there
            if (fromAppt){
                goToManageApptScreen();
            }
        }
        // If forms are not complete throw an alert
        else {
            AlertBox.display("Invalid Submission", "Form is missing required information or has invalid input."
                    + " The following information is required:\n"
                    + "Name\nAddress 1\nCity, State, Country, Zipcode\nPhoneNumber"
                    + "\nPlease complete form with valid input and submit again.");
        }
    };
    
    /**
     * 
        Clears data from text fields and returns to landing page
        @params e
    */
    private void cancel(ActionEvent e){
        // Clear textfields
        clearFields();
        // If coming from appt management screen, go back there
        if (fromAppt){
            goToManageApptScreen();
        }
        // Otherwise, set the current client to null and go to the landing page  
        else {
            currClient = null;
            LandingPage.returnToLandingPage();
        }
    };
    
    /*
        Helper function to clear text fields in Manage Client Screen.
        Uses fields ArrayList to update all fields. If adding new fields,
        be sure to add them to the ArrayList so that they are included in 
        any clearing.
    */
    private void clearFields(){
        fields.forEach(e -> e.clear());
    }
    
    /**
        Launches the client list screen in a new window
    */
    private void getClientList(ActionEvent e){
        new ClientList().Display();
    }
    
    /**
     *  Goes to ManageApptScreen
     */
    private void goToManageApptScreen(){
        Stage stage = AppointmentScheduler.getStage();
        stage.setScene(new ManageApptScreen(new BorderPane(),ManageApptScreen.WIDTH,ManageApptScreen.HEIGHT, true, false));
        stage.setTitle(ManageApptScreen.TITLE);
        stage.show();
    }
    
    /**
     * Helper method to determine if all required client fields have been
     * filled out.<br> To be considered complete, the following fields must be
     * filled out: Name, Address 1, City, State, Country, Zip, Phone
     * @return True if the required fields have input; false otherwise
    */
    private boolean formComplete(){
        return NAME_FIELD.getLength()>0 && ADDRESS_1_FIELD.getLength()>0 &&
               CITY_FIELD.getLength()>0 && ZIP_FIELD.getLength()>0 && 
               COUNTRY_FIELD.getLength()>0 && PHONE_FIELD.getLength()>0;  
    }
    
    /**
     * Helper method to determine if fields only have letters (used for name, city and country fields)<br>
     * Whitespace is ignored in this test.
     * @param fieldVal
     * @return True if there are only letters. False otherwise.
     */
    private boolean isAllChars(String fieldVal){
        // Get rid of all whitespace from the given string
        String val = fieldVal.replaceAll("\\s", "").trim();
        // Check each character in the string to determine if it is a digit or not
        for (int i=0; i < val.length(); i++){
            // if we find a digit return false
            if (Character.isDigit(val.charAt(i))){
                return false;
            }
        }
        // if we get here then all characters in the string are letters
        return true;
    }
    
    /**
     * Helper method to determine if fields only have numbers (used for phone and zip fields<br>
     * Whitespace, dashes and parenthesis are ignored in this test
     * @param fieldVal
     * @return True if there are only numbers. False otherwise
     */
    private boolean isAllNums(String fieldVal){
        // Get rid of all whitespace
        String val = fieldVal.replaceAll("\\s","").trim();
        // check for presence of letters in the string, ignoring '()' and '-'
        for (int i=0; i < val.length(); i++){
            if (val.charAt(i) != '(' || val.charAt(i) != ')' || val.charAt(i) != '-'){
                if (Character.isLetter(val.charAt(i))){
                    return false;
                }
            }
        }
        return true;       
    }
    
    /**
     * Determines if the client intake form is valid (is both completely filled out and has no invalid information in the given fields)
     * @return True if the form is filled out completely and accurately. False otherwise.
     */
    private boolean formIsValid(){
        // check if all the required fields have been filled in
        boolean allFieldsEntered = formComplete();
        // check if Name field has valid input
        boolean nameFieldValid = isAllChars(NAME_FIELD.getText());
        // check if city field has valid input
        boolean cityFieldValid = isAllChars(CITY_FIELD.getText());
        // check if country field has valid input
        boolean ctryFieldValid = isAllChars(COUNTRY_FIELD.getText());
        // check if zipcode field has valid input
        boolean zipFieldValid = isAllNums(ZIP_FIELD.getText());
        // check if phone field has valid input
        boolean phoneFieldValid = isAllNums(PHONE_FIELD.getText());
        // check if all fields and form are valid
        return allFieldsEntered && nameFieldValid &&
                cityFieldValid && ctryFieldValid &&
                zipFieldValid && phoneFieldValid;           
    }
    
    /**
     * Gets the client that we are currently working with to pass between views
     * @return The user-selected client
     */
    public static Client getCurrentClient(){
        return currClient;
    }
    
    /**
     * Screen that shows the list of currently active clients.<br>
     * Active clients have a value of 1 in the active field in customer table.
     */
    class ClientList{
        private BorderPane ROOT = new BorderPane();
        private static final int WIDTH = 500;
        private static final int HEIGHT = 300;
        private Stage window = new Stage();
        // Client List
        private TableView<Client> table = new TableView<>();
        // Table settings (Columns, etc.)
        private final TableColumn<Client, String> cName = new TableColumn<>("Name");
        private final TableColumn<Client, String> cAdd1 = new TableColumn<>("Address 1");
        private final TableColumn<Client, String> cAdd2 = new TableColumn<>("Address 2");
        private final TableColumn<Client, String> cCity = new TableColumn<>("City");
        //private final TableColumn<Client, String> cState = new TableColumn<>("State");
        private final TableColumn<Client, String> cZip = new TableColumn<>("Zipcode");
        private final TableColumn<Client, String> cCountry = new TableColumn<>("Country");
        private final TableColumn<Client, String> cPhone = new TableColumn<>("Phone Number");
        // Buttons
        private final Button back = new Button("Back"); 
        private final Button select = new Button("Select");
        private final Button delete = new Button("Delete");
        // Holder for selected Client
        private Client client;       
        /**
         * Initialize the ClientList view and populate client table
        */
        public void Display(){
            window.setWidth(WIDTH);
            window.setHeight(HEIGHT);
            window.initModality(Modality.APPLICATION_MODAL);
            HBox buttons = new HBox(5);
            buttons.getChildren().addAll(select,delete,back);
            buttons.setPadding(new Insets(25,15,15,15));
            ROOT.setBottom(buttons);
            ROOT.setPadding(new Insets(25));
            back.setOnAction(e->back(e));
            select.setOnAction(e->select(e));
            delete.setOnAction(e->delete(e));
            window.setScene(new Scene(ROOT));
            // Populate the table with info from client list
            refresh();
            table.getColumns().addAll(cName,cAdd1,cAdd2,cCity,cZip,cCountry,cPhone); 
            // Set data for column
            cName.setCellValueFactory(new PropertyValueFactory<>("name"));
            cAdd1.setCellValueFactory(new PropertyValueFactory<>("address1"));
            cAdd2.setCellValueFactory(new PropertyValueFactory<>("address2"));
            cCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            cZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
            cCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
            cPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            // Set table factory to allow rows to be clickable
        table.setRowFactory(tv ->{
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(e ->{
                if (!row.isEmpty() && e.getButton() == MouseButton.PRIMARY){
                    client = row.getItem();
                }
            });
            return row;
        });
            ROOT.setCenter(table);
            window.showAndWait();        
        }
        
        /**
            Close window and return to Client Management Screen
        */
        private void back(ActionEvent e){
            window.close();
        }
        
        /**
            Selects the highlighted client and brings user back to Client
            Management Screen, with the selected client's information populating
            the fields.
        */
        private void select(ActionEvent e){
            // Mark this client as an existing client since they are in the list
            existingClient = true;
            // To bring data to Management Screen we will modify the text fields
            NAME_FIELD.setText(client.getName());
            ADDRESS_1_FIELD.setText(client.getAddress1());
            ADDRESS_2_FIELD.setText(client.getAddress2());
            CITY_FIELD.setText(client.getCity());
            ZIP_FIELD.setText(client.getZip());
            COUNTRY_FIELD.setText(client.getCountry());
            PHONE_FIELD.setText(client.getPhone());
           // client.setAddressID();
            client.setClientID(Client.getClientID(client.getName(), client.getAddressID()));
            // Pass client information to Client Management Screen
            currClient = client;
            back(e);
        }
        
        /**
            Soft deletes the client in the database
        */
        private void delete(ActionEvent e){
            if (client != null){
                ConfirmBox.display("Confirm Deletion", "Are you sure you want to delete this entry?");
                if (ConfirmBox.response){
                    Client.deleteClient(client);
                    AlertBox.display("Delete Client", "Client Deleted Successfully");
                    // Refresh view
                    refresh();
                    
                }
            }
        }
        
        /**
         * Refresh the table view for the client list
        */
        private void refresh(){
            if (!clientList.isEmpty()){
                clientList.clear();
                clientList = FXCollections.observableArrayList(Client.clientList());
            } 
            table.setItems(clientList);
        }
    }
    
    
}
