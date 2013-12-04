package com.mydomain;

import lejos.nxt.Button;
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
	private short state = 0;  // happy state is zero, 1 left sad, -1 is right sad
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
	public int getState()
	{
		return state;
	}
	
	public int [] calculateState()
	{
		int leftReading = poleLeft();
		int rightReading = poleRight();
		int middleReading = poleMiddle();
		//int []readings = {leftReading, rightReading, middleReading};
		if(leftReading<MAX-10&&rightReading>MAX-10)
		{
			if(middleReading<MAX-10)
			{
				state = 2;
			}
			else
			{
				state = 1;
			}
		}
		else if(leftReading>MAX-10&&rightReading<MAX-10)
		{
			if(middleReading<MAX-10)
			{
				state = -2;
			}
			else
			{
				state = -1;
			}
		}
		else if(leftReading>MAX-10&&rightReading>MAX-10)
		{
			state = 0;
		}
		int []readings = {leftReading, rightReading, middleReading};
		return readings;
	}
	
	public int [] poleSensor()
	{
		return readings;
	}
	
	// here is the function that you will need to deterime where your car is in relation to the line
	
	public double calculatePosition()
	{
		readings[0] = poleLeft();
		readings[2] = poleRight();
		readings[1] = poleMiddle();
		double leftPostion =  RANGE - (readings[0] - MIN);
		double rightPostion = RANGE - (readings[1] - MIN);
		double centerPostion = RANGE - (readings[2] - MIN);
		
		if(readings[0]<MAX-20&&readings[2]>MAX-20)
		{
			statePosition = 1;
		}
		else if(readings[0]>MAX-20&&readings[2]<MAX-20)
		{
			statePosition = -1;
		}
		else if(readings[0]>MAX-20&&readings[2]>MAX-20)
		{
			statePosition = 0;
		}
		
		return  setPoint + statePosition * (leftPostion + rightPostion + 5*centerPostion);
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		log logger = new log("IRSensor.txt");
		String value = new String();
		IRSensorArray  IR = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		int [] readings = new int[3];
		readings[0] = 0;
		readings[1] = 0;
		readings[2] = 0;
		long timer = System.currentTimeMillis();
		while(!Button.ESCAPE.isDown())
		{
			
			if((System.currentTimeMillis() - timer) > 50)
			{
				readings = IR.calculateState();
				value = value + readings[0] + ", " + readings[1] + ", " + readings[2] + "\n";
				timer = System.currentTimeMillis();
			}
		}
		logger.writeToLog(value);
		logger.closeLog();
	}

}