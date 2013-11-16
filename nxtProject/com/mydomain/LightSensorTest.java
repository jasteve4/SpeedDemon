package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightSensorTest 
{

	LightSensor leftSensor = new LightSensor(SensorPort.S1,false);
	LightSensor middleSensor = new LightSensor(SensorPort.S2,false);
	LightSensor rightSensor = new LightSensor(SensorPort.S3,false);
	log logger = new log();
	String value = new String();
	
	
	public LightSensorTest() 
	{
		LCD.drawString("LED Test Mode: " + leftSensor.getNormalizedLightValue(), 0, 0);
		
		leftSensor.setFloodlight(true);
		middleSensor.setFloodlight(true);
		rightSensor.setFloodlight(true);
		Button.waitForAnyPress();
		long timer = System.currentTimeMillis();
		while(!Button.ESCAPE.isDown())
		{
			if((System.currentTimeMillis()-timer)> 250)
			{
				LCD.drawString("Left LED:   " + leftSensor.getNormalizedLightValue() + "           ", 0, 1);
				LCD.drawString("Middle LED: " + middleSensor.getNormalizedLightValue() + "      ", 0, 2);
				LCD.drawString("Right LED:  " + rightSensor.getNormalizedLightValue() + "      ", 0, 3);
				value = value + leftSensor.getNormalizedLightValue() + ", " + middleSensor.getNormalizedLightValue() + ", " + rightSensor.getNormalizedLightValue() + "\n";
				timer = System.currentTimeMillis();
			}
		}
		logger.writeToLog(value);
		logger.closeLog();
		
		
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new LightSensorTest();
	}

}
