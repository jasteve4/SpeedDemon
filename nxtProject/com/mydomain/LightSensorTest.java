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
	
	
	public LightSensorTest() 
	{
		LCD.drawString("LED Test Mode: " + leftSensor.getNormalizedLightValue(), 0, 0);
		Button.waitForAnyPress();
		leftSensor.setFloodlight(true);
		middleSensor.setFloodlight(true);
		rightSensor.setFloodlight(true);
		Button.waitForAnyPress();
		
		while(true)
		{
			LCD.drawString("Left LED:   " + leftSensor.getNormalizedLightValue() + "           ", 0, 1);
			LCD.drawString("Middle LED: " + middleSensor.getNormalizedLightValue() + "      ", 0, 2);
			LCD.drawString("Right LED:  " + rightSensor.getNormalizedLightValue() + "      ", 0, 3);
			
			if(Button.ESCAPE.isDown())
			{
				break;
			}
		}
		
		
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new LightSensorTest();
	}

}
