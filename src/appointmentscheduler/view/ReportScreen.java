/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.view;

import appointmentscheduler.logic.Client;
import appointmentscheduler.logic.Consultant;
import appointmentscheduler.logic.Report;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author marro
 */
// TODO figure out issue with data display
public class ReportScreen extends Scene{
    
    private TableView reportResult = new TableView();
    private ObservableList<String> reports = FXCollections.observableArrayList("Appointment Types By Month", "Appointments by Consultant", "Appointments by Client");
    private ObservableList<Report.ApptTypeByMonth> reportData_appByMonth;
    private ObservableList<Report.Report2> reportData_appByConsultant;
    private ObservableList<Report.Report3> reportData_appByClient;
    private ComboBox reportDropDown = new ComboBox(reports);
    private Label selectReport = new Label("Select Report");
    private Button returnToMain = new Button("Return");
    private Button generateReport = new Button("Generate Report");
    // General Settings
    public static final String TITLE = "Report Screen";
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;
    
    // Constructor
    public ReportScreen(BorderPane root, int width, int height){
        super(root,width,height);
        HBox top = new HBox(50);
        top.getChildren().addAll(selectReport,reportDropDown,generateReport);
        HBox.setMargin(selectReport, new Insets(0,25,0,0));
        root.setTop(top);
        root.setCenter(reportResult);
        root.setBottom(returnToMain);
        BorderPane.setMargin(top, new Insets(0,0,16,0));
        BorderPane.setMargin(returnToMain, new Insets(16,0,0,0));
        // Set button actions
        returnToMain.setOnAction(e -> {
            LandingPage.returnToLandingPage();
        });
        generateReport.setOnAction(e -> 
        {try{   
                ReportInput ri = new ReportInput();
                ri.Display();
             }catch(NullPointerException ex){
                 AlertBox.display("No Report Selected", "Please select a report from the list");
             }}
        );
        root.setPadding(new Insets(25));
        
    }
    
    /**
     * Screen for user input based on given report
     */
    class ReportInput{
       private BorderPane ROOT = new BorderPane();
       private static final int WIDTH = 500;
       private static final int HEIGHT = 300;
       private final Stage window = new Stage();
       // holder for the selected report
       private String selectedReport = reportDropDown.getValue().toString();
       private final Button submit = new Button("Submit");
       // This will hold our input fields and labels in a top to bottom fashion
       private VBox verticalHolder = new VBox(5);
       /**
        * Displays the input screen based on the value in selectedReport
        */
       private void Display() throws NullPointerException{
           window.setWidth(WIDTH);
           window.setHeight(HEIGHT);
           window.initModality(Modality.APPLICATION_MODAL);
           ROOT.setPadding(new Insets(25));
           // Change input fields based on selectedReport
           switch(selectedReport){
               case "Appointment Types By Month":
                   Report1();
                   break;
               case "Appointments by Consultant":
                   Report2();
                   break;
               case "Appointments by Client":
                   Report3();
                   break;
               default:
                   AlertBox.display("No Report Selected", "Please select a report from the list.");
                   break;               
           }
           ROOT.setCenter(verticalHolder);
           window.setScene(new Scene(ROOT));
           window.showAndWait();
       }
       
       /**
        * Displays screen based on entering parameters for Report 1 (change name later)
        */
       private void Report1(){
           refreshTable(); // refresh the table data
           ComboBox month = new ComboBox();
           month.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12); // Month selection
           Label m = new Label("Select Month");
           TextField year = new TextField();
           year.setPrefWidth(80);
           Label y = new Label("Enter Year");
           // Create containers to hold our input fields
           HBox Month = new HBox(5);
           Month.getChildren().addAll(m,month);
           HBox Year = new HBox(5);
           Year.getChildren().addAll(y,year);
           // Set table columns in table
           TableColumn<Report.ApptTypeByMonth,String> col1 = new TableColumn<>("Appointment Type");
           TableColumn<Report.ApptTypeByMonth,Integer> col2 = new TableColumn<>("Number of Appointments");
           // Add columns to table
           reportResult.getColumns().addAll(col1,col2);
           // Set action for Submit button
           submit.setOnAction(e -> {
               // Generate the report
               ArrayList<Report.ApptTypeByMonth> results = Report.apptByMonth(month.getValue().toString(),year.getText());
               // If there is report data in the list, clear it out
               if (reportData_appByMonth != null) reportData_appByMonth.clear(); 
               // Add report to the observable list
               reportData_appByMonth = FXCollections.observableArrayList(results);
               // Set cell value factory
               col1.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
               col2.setCellValueFactory(new PropertyValueFactory<>("count"));
                // Set items to table
               reportResult.setItems(reportData_appByMonth);
               // close screen
               window.close();
           }); 
           // Add the horizontal containers to our vertical container for display
           this.verticalHolder.getChildren().addAll(Month,Year, submit);
       }
       
       
       /**
        * Displays screen based on entering parameters for Report 2 (change name later)
        */
       private void Report2(){
           // refresh table data
           refreshTable();
           // Create report fields
           Label label = new Label("Select Consultant:");
           ObservableList<String> consultants = FXCollections.observableArrayList(Consultant.getConsultants());
           ComboBox consultantList = new ComboBox(consultants);
           // Holder for fields
           HBox holder = new HBox(5);
           // Set table columns in table
           TableColumn<Report.Report2,String> CLIENT_NAME_COL = new TableColumn<>("Client Name");
           TableColumn<Report.Report2,String> MEETING_CAT_COL = new TableColumn<>("Meeting Category");
           TableColumn<Report.Report2,String> DESCRIPTION_COL = new TableColumn<>("Description");
           TableColumn<Report.Report2,LocalDate> DATE_COL = new TableColumn<>("Date");
           TableColumn<Report.Report2,LocalTime> MEETING_START_COL = new TableColumn<>("Start Time");
           TableColumn<Report.Report2,LocalTime> MEETING_END_COL = new TableColumn<>("End Time");
           TableColumn<Report.Report2,String> LOCATION_COL = new TableColumn<>("Location");
           // Add columns to table
           reportResult.getColumns().addAll(CLIENT_NAME_COL,MEETING_CAT_COL,DESCRIPTION_COL,DATE_COL,MEETING_START_COL,MEETING_END_COL,LOCATION_COL);
           // Set up submit button action
           submit.setOnAction(e -> {
               // Generate report
               ArrayList<Report.Report2> results = Report.apptByConsultant(consultantList.getValue().toString());
               // If there is report data in the list, clear it out
               if (reportData_appByConsultant != null) reportData_appByConsultant.clear();
               // Add report to the observable list
               reportData_appByConsultant = FXCollections.observableArrayList(results);
               // Set cell value factory
               CLIENT_NAME_COL.setCellValueFactory(new PropertyValueFactory<>("clientName"));
               MEETING_CAT_COL.setCellValueFactory(new PropertyValueFactory<>("category"));
               DATE_COL.setCellValueFactory(new PropertyValueFactory<>("date"));
               MEETING_START_COL.setCellValueFactory(new PropertyValueFactory<>("startTime"));
               MEETING_END_COL.setCellValueFactory(new PropertyValueFactory<>("endTime"));
               LOCATION_COL.setCellValueFactory(new PropertyValueFactory<>("location"));
               DESCRIPTION_COL.setCellValueFactory(new PropertyValueFactory<>("description"));
               // Set items to table
               reportResult.setItems(reportData_appByConsultant);
               // close screen
               window.close();
           });
           // Add fields to holder
           holder.getChildren().addAll(label,consultantList,submit);
           // Add this holder to main holder
           this.verticalHolder.getChildren().add(holder);
       }
       
       /**
        * Displays screen based on entering parameters for Report 3
        */
       private void Report3(){
        // refresh table data
           refreshTable();
           // Create report fields
           Label label = new Label("Select Client:");
           // Get client names from DB
           ArrayList<Client> allClients = Client.clientList();
           ArrayList<String> clientNames = new ArrayList<>();
           allClients.forEach(e -> clientNames.add(e.getName()));
           ObservableList<String> clients = FXCollections.observableArrayList(clientNames);
           ComboBox clientList = new ComboBox(clients);
           // Holder for fields
           HBox holder = new HBox(5);
           // Set table columns in table
           TableColumn<Report.Report3,String> CLIENT_NAME_COL = new TableColumn<>("Client Name");
           TableColumn<Report.Report3,String> MEETING_CAT_COL = new TableColumn<>("Meeting Category");
           TableColumn<Report.Report3,String> DESCRIPTION_COL = new TableColumn<>("Description");
           TableColumn<Report.Report3,LocalDate> DATE_COL = new TableColumn<>("Date");
           TableColumn<Report.Report3,LocalTime> MEETING_START_COL = new TableColumn<>("Start Time");
           TableColumn<Report.Report3,LocalTime> MEETING_END_COL = new TableColumn<>("End Time");
           TableColumn<Report.Report3,String> CONSULTANT_COL = new TableColumn<>("Consultant");
           TableColumn<Report.Report3,String> LOCATION_COL = new TableColumn<>("Location");
           // Add columns to table
           reportResult.getColumns().addAll(CLIENT_NAME_COL,MEETING_CAT_COL,DESCRIPTION_COL,DATE_COL,MEETING_START_COL,MEETING_END_COL,CONSULTANT_COL,LOCATION_COL);
           // Set up submit button action
           submit.setOnAction(e -> {
               // Generate report
               ArrayList<Report.Report3> results = Report.apptByClient(clientList.getValue().toString());
               // If there is report data in the list, clear it out
               if (reportData_appByClient != null) reportData_appByClient.clear();
               // Add report to the observable list
               reportData_appByClient = FXCollections.observableArrayList(results);
               // Set cell value factory
               CLIENT_NAME_COL.setCellValueFactory(new PropertyValueFactory<>("clientName"));
               MEETING_CAT_COL.setCellValueFactory(new PropertyValueFactory<>("category"));
               DATE_COL.setCellValueFactory(new PropertyValueFactory<>("date"));
               MEETING_START_COL.setCellValueFactory(new PropertyValueFactory<>("startTime"));
               MEETING_END_COL.setCellValueFactory(new PropertyValueFactory<>("endTime"));
               LOCATION_COL.setCellValueFactory(new PropertyValueFactory<>("location"));
               DESCRIPTION_COL.setCellValueFactory(new PropertyValueFactory<>("description"));
               // Set items to table
               reportResult.setItems(reportData_appByClient);
               // close screen
               window.close();
           });
           // Add fields to holder
           holder.getChildren().addAll(label,clientList,submit);
           // Add this holder to main holder
           this.verticalHolder.getChildren().add(holder);
       }
       
   
    }
    // End inner class
    
    /**
     * Helper method to refresh table view
     */
    private void refreshTable(){
        // refresh the table
        this.reportResult.getItems().clear();
        this.reportResult.getColumns().clear();
    }
}
