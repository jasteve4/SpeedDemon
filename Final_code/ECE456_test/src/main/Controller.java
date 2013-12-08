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
import lejos.nxt.Sound;

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
    public int setPower = 60;
    public long rangeReading = 1000;
    public double rangePower = 0;
    public int menuSelection = 0;
    public int []readings = {0, 0, 0};
    public boolean stop = false;
    public int echoTarget;
    
    
    //initialize the four sensors that you will be  using
    public void initializeSensors() {
        //YOU NEED TO WRITE NECESSARY CODE HERE
        IRArray = new IRSensorArray();
        middleSensor = new LightSensor(SensorPort.S2,false);
        leftSensor = new LightSensor(SensorPort.S1,false);
        rightSensor = new LightSensor(SensorPort.S3,false);
        IRArray.leftIR = leftSensor;
        IRArray.middleIR = middleSensor;
        IRArray.rightIR = rightSensor;
        
        motors = new MotorController(MotorPort.A,MotorPort.B);
        menuSelection = 2;
        
        if(menuSelection == 1)
            linePID = new PID(1.81, 1.29, .41);
        else
            linePID = new PID(.8, 0, .01);
        
        
        rangeFinderPID = new PID(.8, .6, .2);
        
        
        SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
	SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
    }
    
    //YOUR PID CODE GOES IN THIS METHOD!!
    @Override
    public void run() {
        //YOUR PID CODE GOES HERE!!
        //after you stop the vehicle
        leftTunedSpeed = leftSpeed;
        rightTunedSpeed = rightSpeed;
        LCD.clear();
        LCD.drawString("Running...", 0, 2);
        if(menuSelection == 1)
            echoTarget = 200;
        else if(menuSelection == 2)
        {
            LCD.clear();
            while(!Button.ENTER.isDown())
            {
                LCD.drawString("Get Echo Reading", 0, 0);
                LCD.drawString(""+ultrasoicSensor.getPulseLenght()/1000, 0, 1);
                try
                {
                    Thread.sleep(100);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            echoTarget = (int)ultrasoicSensor.getPulseLenght()/1000;
            LCD.drawInt(echoTarget, 0, 0);
            leftTunedSpeed = 0;
            rightTunedSpeed = 0;
            setPower = 20;
            try
            {
                Thread.sleep(500);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            while(!Button.ENTER.isDown())
            {
                try
                {
                    Thread.sleep(100);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            LCD.clear();
        }
        try {
            Thread.sleep(50);
            while(!Button.ESCAPE.isDown())
            {
                Thread.sleep(timeStep);
                rangeReading = ultrasoicSensor.getPulseLenght()/1000;

                rangePower = 5*(rangeFinderPID.pid(echoTarget, rangeReading, (double)timeStep/1000)); // blah/40

                position = IRArray.calculatePosition();
                lineError = 10 * linePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR); //360
                readings = IRArray.poleSensor();
                if(menuSelection == 1) // Task 1-2: Line and Block Stop
                {
                    if(rangeReading < 300)
                    {
                        leftTunedSpeed = (int)(setPower - lineError)/6;
                        rightTunedSpeed = (int)(setPower + lineError)/6;
                    }
                    else if(rangeReading < 600)
                    {
                        leftTunedSpeed = (int)(setPower - lineError)/3;
                        rightTunedSpeed = (int)(setPower + lineError)/3;
                    }
                    else
                    {
                        leftTunedSpeed = (int)(setPower - lineError);
                        rightTunedSpeed = (int)(setPower + lineError);
                    }

                    if(rangeReading < 175)
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
                    linePID.capI(100);
                    if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
                    {
                        stop = true;
                    }
                    
                    if(rangeReading > echoTarget - 40 && rangeReading < echoTarget + 40)
                    {
                        motors.leftMotor.flt();
                        motors.rightMotor.flt();
                    }
                    else if(rangeReading > echoTarget)
                    {
                        motors.leftMotor.forward();
                        motors.rightMotor.forward();
                        setPower = 80;
                        leftTunedSpeed = (int)(setPower - lineError);
                        rightTunedSpeed = (int)(setPower + lineError);
                    }
                    else
                    {
                        motors.leftMotor.backward();
                        motors.rightMotor.backward();
                        setPower = 20;
                        leftTunedSpeed = (int)((setPower - lineError)* rangePower);
                        rightTunedSpeed = (int)((setPower + lineError) * rangePower);
                    }
                   
                }
                if(stop)
                {
                    motors.stopMotors();
                    LCD.drawString("Stopped", 0, 3);
                }
                else
                {
                    motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
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


