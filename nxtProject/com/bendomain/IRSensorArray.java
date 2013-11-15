package com.bendomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

import com.mydomain.log;

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
	
	private int poleMiddle()
	{
		return middleIR.getNormalizedLightValue();
	}
	
	public int [] calculateState()
	{
		int leftReading = poleLeft();
		int rightReading = poleRight();
		int middleReading = poleMiddle();
		if(leftReading<MAX&&rightReading>=MAX)
			state = -1;
		if(leftReading>=MAX&&rightReading<MAX)
			state = 1;
		int[] readings = {leftReading,middleReading,rightReading};
		return readings;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		LCD.drawString("Ben Test IR", 0, 0);
		log logger = new log();
		String value = "";
		IRSensorArray IR = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		int[] readings = {0,0,0};
		long timer = System.currentTimeMillis();
		while(!Button.ESCAPE.isDown())
		{
			if(System.currentTimeMillis() - timer > 50)
			{
				readings = IR.calculateState();
				value = value + readings[0] + " " + readings[1] + " " +readings[2] + "\n";
				timer = System.currentTimeMillis();
			}
		}
		logger.writeToLog(value);
		logger.closeLog();
	}

}
