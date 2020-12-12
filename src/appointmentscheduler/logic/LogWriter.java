/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logs user sign-in attempts and errors in program to Logs/log.txt
 * @author martinarroyo
 */
public class LogWriter {
     
    private LogWriter(){}
    
    
    /**
     * Writes to our log. <br>Utilizes Try with Resources to ensure resource closure
     * @param message
     * @return
     * @throws IOException 
     */
    public static boolean writeToLog(String message) throws IOException{
        
        if (message.isEmpty()) return false;
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Logs/log.txt",true))){
            writer.write(message);
            writer.newLine();
        }

        return true;
    }
    
    
}
