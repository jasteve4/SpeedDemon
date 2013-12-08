/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;

/**
 *
 * @author Unnati Ojha
 */
class Controller extends Thread {

    public static UltrasonicSensor Us;
    public static LightSensor middleSensor;
    public static LightSensor leftSensor;
    public static LightSensor rightSensor;
    private volatile boolean running = false;

    //initialize the four sensors that you will be  using
    public void initializeSensors() {
        //YOU NEED TO WRITE NECESSARY CODE HERE
    }

    //YOUR PID CODE GOES IN THIS METHOD!!
    @Override
    public void run() {
        //YOUR PID CODE GOES HERE!!
        //after you stop the vehicle
        running = false;
    }

    
    //getters and setters 
    public void setRunning(boolean flag) {
        running = flag;
    }
    
    public boolean isRunning(){
        return running;
    }
       
    public static int getMiddleSensor() {
        return middleSensor.getNormalizedLightValue();
    }

    public static int getLeftSensor() {
        return leftSensor.getNormalizedLightValue();
    }

    public static int getRightSensor() {
        return rightSensor.getNormalizedLightValue();
    }

}
