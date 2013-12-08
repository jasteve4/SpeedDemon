/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;

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
    
    // intitialize class need to follow the line
    private IRSensorArray IRArray;
    private PID linePID;
    private PID rangeFinderPID;
    private MotorController motors;
    public UltrasonicSensorEcho ultrasoicSensor = null;
   
    public int leftSpeed = 80;
    public int rightSpeed = 80;
    public int timeStep = 10;
    public int MAX = 866;  // black
    public int MIN = 380;
    public int IR_MAX_ERROR = MAX - MIN;
    public int leftTunedSpeed;
    public int rightTunedSpeed;
    public double position = 0;
    public double lineError = 0;
    public int setPower = 80;
    public long rangeReading = 1000;
    public double rangePower = 0;
    public int menuSelection = 0;
    public int []readings = {0, 0, 0};
    public boolean stop = false;
    public int echoTarget = 0;
    
    
    //initialize the four sensors that you will be  using
    public void initializeSensors() {
        //YOU NEED TO WRITE NECESSARY CODE HERE
        IRArray = new IRSensorArray(
                SensorPort.S1, SensorPort.S2, SensorPort.S3 );
        middleSensor = IRArray.middleIR;
        leftSensor = IRArray.leftIR;
        rightSensor = IRArray.rightIR;
        motors = new MotorController(MotorPort.A,MotorPort.B);
    	LCD.drawString("Left for", 0, 0);
	LCD.drawString("Task 1-2 'curve'", 0, 1);
	LCD.drawString("Right for", 0, 3);
	LCD.drawString("Task 3 'platoon'", 0, 4);
        menuSelection = getTaskNumber();
	taskSetUp(menuSelection);
        SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
	SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
    }

    
    
    public int getTaskNumber()
    {
        int menuSelection = Button.waitForAnyPress();
        if(menuSelection == Button.ID_LEFT) //Task 1 and 2: 
        {
            return 1;
        }
        else if(menuSelection == Button.ID_RIGHT) //Task 3:
        {
            return 2;
        }
        else
        {
            return -1;
        }
    }
    
    public void taskSetUp(int task)
    {
        LCD.clear();
        if(menuSelection == 1)
        {
            linePID = new PID(1, 0, 0); //Initialized
            rangeFinderPID = new PID(1, 0, 0);
            LCD.drawString("Task 1-2", 0, 0);
            setPower = 80;
            echoTarget = 200;
        }
        else if(menuSelection == 2)
        {
            double a = 1.7;
            double b = 1.6;
            linePID = new PID(0.33*a, 2*a/b, a*b/3);
            rangeFinderPID = new PID(4, 6, .1);
            LCD.drawString("Task 3", 0, 0);
            setPower = 20;
            echoTarget = 890;
        }
        else
        {
            LCD.drawString("Invalid Task", 0, 0);
            LCD.drawString("Left for", 0, 2);
            LCD.drawString("Task 1-2 'curve'", 0, 3);
            LCD.drawString("Right for", 0, 5);
            LCD.drawString("Task 3 'platoon'", 0, 6);
            menuSelection = getTaskNumber();
            taskSetUp(menuSelection);
        }
        LCD.drawString("Press Enter", 0, 1);
        LCD.drawString("to Start", 0, 2);
    }
    
    
    //YOUR PID CODE GOES IN THIS METHOD!!
    @Override
    public void run() {
        //YOUR PID CODE GOES HERE!!
        //after you stop the vehicle
        LCD.clear();
        if(menuSelection == 1)
        {
            LCD.drawString("Task 1-2 Selected", 0, 0);
        }
        else if(menuSelection == 2)
        {
            LCD.drawString("Task 3 Selected", 0, 0);
        }
       // ultrasoicSensor.wakeUp();
        leftTunedSpeed = leftSpeed;
        rightTunedSpeed = rightSpeed;
        LCD.drawString("Running...", 0, 2);
        LCD.clear();
        
        try {
            Thread.sleep(50);
            while(!Button.ESCAPE.isDown())
            {
                Thread.sleep(timeStep);
                rangeReading = ultrasoicSensor.getPulseLenght()/1000;

                rangePower = (rangeFinderPID.pid(1000, rangeReading, (double)timeStep/1000))/40; // blah/40

                position = IRArray.calculatePosition();
                lineError = 10 * linePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR); //360
                readings = IRArray.poleSensor();
                if(menuSelection == 1) // Task 1-2: Line and Block Stop
                {
                    if(rangeReading < 600)
                    {
                        leftTunedSpeed = (int)(setPower - lineError - rangePower);
                        rightTunedSpeed = (int)(setPower + lineError - rangePower);
                    }
                    else
                    {
                        leftTunedSpeed = (int)(setPower - lineError);
                        rightTunedSpeed = (int)(setPower + lineError);
                    }

                    if(rangeReading < 400)
                    {
                        stop = true;
                    }
                    else
                    {
                        stop = false;
                    }
                }
                else if(menuSelection == 2) //Task 3: Platooning and Redline Stop
                {
                    rangeFinderPID.capI(100);
                    if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
                    {
                        stop = true;
                    }
                    leftTunedSpeed = (int)(setPower - lineError - rangePower);
                    rightTunedSpeed = (int)(setPower + lineError - rangePower);
                }
                if(stop)
                {
                    motors.stopMotors();
                    LCD.drawString("Stopped", 0, 3);
                }
                else
                {
                    if(menuSelection == 1)
                        motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
                    else
                    {
                        if(leftTunedSpeed < 0 && rightTunedSpeed < 0)
                        {
                            motors.leftMotor.backward();
                            motors.rightMotor.backward();
                            leftTunedSpeed = setPower-leftTunedSpeed;
                            rightTunedSpeed = setPower-rightTunedSpeed;

                        }
                        else
                        {
                            motors.leftMotor.forward();
                            motors.rightMotor.forward();
                        }
                        motors.updateMotors(leftTunedSpeed, rightTunedSpeed);

                    }
                }
            }
        }
        catch (InterruptedException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


