package com.mydomain;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class IRSensorArray 
{
	// dark reading is 866, white reading is 380 
	private LightSensor leftIR = null;
	private LightSensor middleIR = null;
	private LightSensor rightIR = null;
	private int MIN = 360;  // white
	private int MAX = 866;  // black
	private int RANGE = MAX - MIN;
	private int BLACK_RANGE = 166;
	private double setPoint =  0;
	private short statePosition = 0;
	private int [] readings = {0, 0, 0};


	public IRSensorArray(SensorPort left,SensorPort middle, SensorPort right) 
	{
		// TODO Auto-generated constructor stub
		leftIR = new LightSensor(left,false);
		middleIR = new LightSensor(middle,false);
		rightIR = new LightSensor(right,false);
	}

	public void IROn()
	{
		leftIR.setFloodlight(true);
		middleIR.setFloodlight(true);
		rightIR.setFloodlight(true);	
	}

	private int poleLeft()
	{
		return leftIR.getNormalizedLightValue();
	}

	private int poleRight()
	{
		return rightIR.getNormalizedLightValue();
	}	

	private int poleMiddle()
	{
		return middleIR.getNormalizedLightValue();
	}

	public int [] poleSensor()
	{
		return readings;
	}

	// here is the function that you will need to determine where your car is in relation to the line

	public double calculatePosition()
	{
		readings[0] = poleLeft();
		readings[2] = poleRight();
		readings[1] = poleMiddle();
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