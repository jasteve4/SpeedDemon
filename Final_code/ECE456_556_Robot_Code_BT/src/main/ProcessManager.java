package main;

import java.io.IOException;

/**
 * ProcessManager handles the initialization, starting and stopping of 
 * controlling and reporting threads
 * @author Unnati Ojha, 11/26/2013
 */
public class ProcessManager {

    private final Controller controller = new Controller();
    private final DataLogger dataLogger = new DataLogger();
    private static final ProcessManager processManager = new ProcessManager();
    
    
    private ProcessManager() {
    }

    public static ProcessManager getInstance() {
        return processManager;
    }

    /**
     * Initialize the connection as well as the sensors  
    **/
    private void initialize() throws IOException {
        while (true) {
            dataLogger.connect();
            while (dataLogger.isConnected()) {
                controller.initializeSensors();
                readData();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    /**
     * Wait for data to be sent from the PC and respond to its request   
    **/
    private void readData() {
        int code = dataLogger.readData();
        if (code == DataLogger.Command.RUN.ordinal()) { //run and start sending instanteneous data
            controller.setRunning(true);
            controller.start();
            dataLogger.start();
        } else if (code == DataLogger.Command.GETREFERENCE.ordinal()) { //send reference values
            dataLogger.reportData();
        }
    }

    /**
     * Simple getter for controller
     * @return controller 
    **/
    public Controller getController() {
        return controller;
    }
    
    /**
     * Main entry point function Starts the initialize method in ProcessManager
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        processManager.initialize();
    }
    
}
