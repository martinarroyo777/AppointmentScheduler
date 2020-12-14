/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinarroyo
 */
public class LogWriterTest {

    @Test
    // LogWriter should not write empty strings to log
    public void testWriteEmptyStringToLog() throws Exception {
        System.out.println("writeEmptyStringToLog");
        String message = "";
        boolean expResult = false;
        boolean result = LogWriter.writeToLog(message);
        assertEquals(expResult, result);      
    }
    
    @Test
    // LogWriter should return true if it successfully wrote to the log
    // After running, check log.txt for test string
    public void testWriteToLog() throws Exception {
        System.out.println("writeToLogSuccessful");
        assertEquals(true, LogWriter.writeToLog("This is a test of the LogWriter functionality."));
    }
}
