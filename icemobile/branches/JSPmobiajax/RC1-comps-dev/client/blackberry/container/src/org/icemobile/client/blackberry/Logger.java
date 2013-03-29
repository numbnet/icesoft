package org.icemobile.client.blackberry;

import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Logger {

	protected static long mStartTime = System.currentTimeMillis();
	
	
	
	public static void DEBUG( String message) { 
        EventLogger.logEvent(ContainerController.GUID, message.getBytes(), EventLogger.DEBUG_INFO);
    }

    public static void ERROR( String message) { 
        EventLogger.logEvent(ContainerController.GUID, message.getBytes(), EventLogger.ERROR);
    }	

    public static void WARN(String message) { 
        EventLogger.logEvent(ContainerController.GUID, message.getBytes(), EventLogger.WARNING);
    }
    /**
     * Print a log message indicating a delta time from some arbitrary start time
     * @param startTime The start time in ms
     * @param measureTime an arbitrary time later. If 0, it's taken as currentTimeMillis()
     * @param message The message to print
     */
    public static void TIME( long startTime, long measureTime, String message) {
    	if (measureTime == 0) { 
    		measureTime = System.currentTimeMillis(); 
    	}
        DEBUG( message + " took: " + (measureTime - startTime) + " ms"); 
    }
    
    /**
     * Print a log message indicating a delta time from some arbitrary start time
     * @param startTime The start time in ms
     * @param measureTime an arbitrary time later. If 0, it's taken as currentTimeMillis()
     * @param message The message to print
     */
    public static void TIME( long startTime,  String message) {
        TIME( startTime, 0, message); 
    }
    
    /**
     * Log a debug message indicating the occurance of some event at some time since 
     * a starting time 
     * @param startTime if = 0, taken as the time the app was started. 
     * @param measureTime The delta time 
     * @param message message to log
     */
    public static void TRACE ( long startTime, long measureTime, String message ) {
    	
    	long start = startTime==0 ? mStartTime : startTime; 
        DEBUG( message + " at: " + (measureTime - start) + " ms"); 
    	
    }
    /**
     * Convenience method to print a message using app start time and current time 
     * as yardsticks.
     * @param message Message to log 
     */
    public static void TRACE (String message) { 
    	TRACE ( 0, System.currentTimeMillis(), message);
    }
    
    
    public static void DIALOG(final String message) {        
        UiApplication.getUiApplication().invokeLater(new Runnable() {            
            public void run() { 
                Dialog.alert(message);
            }
        });
    }
	
}
