package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class UltrasonicTest 
{

	public UltrasonicTest(SensorPort port) throws InterruptedException {
		// TODO Auto-generated constructor stub
		port.setMode(SensorPort.SP_MODE_OUTPUT);
		port.setSensorPin(SensorPort.SP_DIGI1, 0);
		boolean portStateHigh = true;
		boolean portStateLow = true;
		int count = 0;
		
		Thread.sleep(1);
		LCD.drawString("Ultrasonic Test", 0, 0);
		long startTime = System.nanoTime();
		double value = 0;
		while(!Button.ESCAPE.isDown())
		{
			if(((System.nanoTime()-startTime)<100000)&&(portStateHigh))
			{
				port.setSensorPin(SensorPort.SP_DIGI1, 1);
				portStateHigh = false;
			}
			else if(((System.nanoTime()-startTime)>=50000000))
			{
				portStateHigh = true;
				portStateLow = true;
				startTime = System.nanoTime();
				count++;
			}
			else if(((System.nanoTime()-startTime)>=100000)&&portStateLow)
			{
				port.setSensorPin(SensorPort.SP_DIGI1, 0);
				Thread.sleep(25);
				portStateLow = false;
			}
			if(count >10)
			{
				LCD.drawString("Value" + value, 0, 1);
				
			}
		}
		
		Button.waitForAnyPress();
		
		
	}

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new UltrasonicTest(SensorPort.S1);
	}

}
