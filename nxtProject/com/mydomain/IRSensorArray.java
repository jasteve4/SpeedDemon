package com.mydomain;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class IRSensorArray 
{
	// dark reading is 866, white reading is 380 
	private LightSensor leftIR = null;
	private LightSensor middleIR = null;
	private LightSensor rightIR = null;
	private int MIN = 400;  // white
	private int MAX = 846;  // black
	private short state = 0;  // happy state is zero, 1 left sad, -1 is right sad
	
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
	
	private int polemiddle()
	{
		return middleIR.getNormalizedLightValue();
	}
	
	public int [] calculateState()
	{
		int leftReading = poleLeft();
		int rightReading = poleRight();
		int middleReading = 0;
		int [] reading = {MAX, MAX, MAX};
		if((leftReading >= MIN)||(rightReading >= MIN)) // is either sensor seeing all white
			middleReading = polemiddle();
		// MAX = Dark  MIN = white
		if(middleReading == 0)
		{
			if(leftReading >= MAX) // left Sensor reading white
			{
				reading[0] = leftReading;
				state = 1;
				return reading;
			}
			if(rightReading >= MAX) // right Sensor reading white
			{
				reading[2] = rightReading;
				state = -1;
				return reading;
			}
			state = 0;
			return reading;
		}
		else
		{
			if((leftReading >= MAX)&&(rightReading >= MAX)) // left Sensor reading white
			{
				reading[0] = leftReading;
				reading[1] = middleReading;
				reading[2] = rightReading;
				return reading;
			}
			
			else if(leftReading >= MAX) // left Sensor reading white
			{
				reading[0] = leftReading;
				reading[1] = middleReading;
				state = 1;
				return reading;
			}
			else
			{
				state = -1;
				reading[2] = rightReading;
				reading[1] = middleReading;
				return reading;
			}
		}
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);

	}

}
