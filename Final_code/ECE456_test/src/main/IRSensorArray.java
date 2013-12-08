/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;


import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 *
 * @author Josh
 */
public class IRSensorArray 
{
    // dark reading is 866, white reading is 380 
    public LightSensor leftIR = null;
    public LightSensor middleIR = null;
    public LightSensor rightIR = null;
    private final int MIN;
    private final int MAX;
    private final int RANGE;
    private short state = 0;  // happy state is zero, 1 left sad, -1 is right sad
    private double setPoint =  0;
    private short statePosition = 0;
    private int [] readings; //= {0, 0, 0};
    private int BLACK_RANGE = 20;


    public IRSensorArray(/*SensorPort left,SensorPort middle, SensorPort right*/) 
    {
        // TODO Auto-generated constructor stub
       // leftIR = new LightSensor(left,false);
       // middleIR = new LightSensor(middle,false);
       // rightIR = new LightSensor(right,false);
        MIN = 360;  // white
        MAX = 866;  // black
        RANGE = MAX - MIN;
        readings = new int[3];
        readings[0] = 0;
        readings[1] = 0;
        readings[2] = 0;
    }

    public void IROn()
    {
        leftIR.setFloodlight(true);
        middleIR.setFloodlight(true);
        rightIR.setFloodlight(true);	
    }

    public int getState()
    {
        return state;
    }

    public int [] poleSensor()
    {
        return readings;
    }

    // here is the function that you will need to deterime where your car is in relation to the line

    public double calculatePosition()
    {
        readings[0] = leftIR.getNormalizedLightValue();
        readings[2] = rightIR.getNormalizedLightValue();
        readings[1] = middleIR.getNormalizedLightValue();
        double leftPostion =  RANGE - (readings[0] - MIN);
        double rightPostion = RANGE - (readings[2] - MIN);
        double centerPostion = RANGE - (readings[1] - MIN);

        if(readings[0]<MAX-BLACK_RANGE&&readings[2]>MAX-BLACK_RANGE)
        {
            statePosition = 1;
        }
        else if(readings[0]>MAX-BLACK_RANGE&&readings[2]<MAX-BLACK_RANGE)
        {
            statePosition = -1;
        }
        else if(readings[0]>MAX-BLACK_RANGE&&readings[2]>MAX-BLACK_RANGE)
        {
            statePosition = 0;
        }

        return  setPoint + statePosition * (BLACK_RANGE + leftPostion + rightPostion + 5*centerPostion);

    }
}
