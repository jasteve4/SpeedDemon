package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * DataLogger class handles connection to PC as well as data receiving and sending tasks
 * It implements the run method from Thread class to start a data sending thread.
 * @author Unnati Ojha 11/26/2013
 */
class DataLogger extends Thread {

    private BTConnection PCconnection;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private volatile boolean connected = false;
    enum Command { CONNECT, RUN, GETREFERENCE;}
    
    /**
     * Connect to PC through BlueTooth    
    **/
    public void connect() throws IOException {
        LCD.clear();
        LCD.drawString("Waiting", 0, 0);
        PCconnection = Bluetooth.waitForConnection(); // this method is very patient.
        LCD.clear();
        LCD.drawString("Connected", 0, 0);
        dataIn = PCconnection.openDataInputStream();
        dataOut = PCconnection.openDataOutputStream();
        Sound.beepSequence();
        connected = true;

    }

    public boolean isConnected() {
        return connected;
    }

    public void writeData(float data) {
        try {
            dataOut.writeFloat(data);
        } catch (IOException e) {
        }
    }

    public void sendData() {
        try {
            dataOut.flush();
        } catch (IOException e) {
        }
    }
    
    /**
     * Reads data sent from PC
    **/
    public int readData() {
        int code = -1;
        try {
            code = dataIn.readInt();
        } catch (IOException ex) {
            Motor.A.stop();
            Motor.B.stop();
        }
        return code;
    }

    /**
     * Writes and sends data about sensors and motors to the PC    
    **/
    public void reportData() {
        writeData(Controller.getLeftSensor());
        writeData(Controller.getRightSensor());
        writeData(Controller.getMiddleSensor());
        writeData(Motor.A.getSpeed());
        writeData(Motor.B.getSpeed());
        sendData();
    }   

    @Override
    public void run() {
        while (ProcessManager.getInstance().getController().isRunning()) {
            reportData();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                System.out.println("OMG");
                break;
            }
        }
    }
}
