package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class UltrasonicTest //implements SensorPortListener
{
	
	int echoValue = 0;
	int echoValue1 = 0;
	boolean echoState = false;
	boolean lastEchoState = false;
	long echoTimer = 0;
	long timer = 0;
	double detlaTime = 0;
	long displayCount = 0;
	
	
	public UltrasonicTest(SensorPort port) throws InterruptedException {
		// TODO Auto-generated constructor stub
		port.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		port.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		boolean portStateHigh = true;
		boolean portStateLow = true;
		
		Thread.sleep(1);
		LCD.drawString("Ultrasonic Test", 0, 0);
		LCD.drawString("EchoTimer:", 0, 1);
		long startTime = System.currentTimeMillis();
		while(!Button.ESCAPE.isDown())
		{
			if(((System.currentTimeMillis()-startTime)<1)&&(portStateHigh))
			{
				port.setSensorPin(SensorPort.SP_DIGI1, 1);
				portStateHigh = false;
			}
			else if(((System.currentTimeMillis()-startTime)>=50))
			{
				portStateHigh = true;
				portStateLow = true;
				startTime = System.currentTimeMillis();
				displayCount++;
			}
			else if(((System.currentTimeMillis()-startTime)>=1)&&portStateLow)
			{
				port.setSensorPin(SensorPort.SP_DIGI1, 0);
				portStateLow = false;
			}
			if(port.getSensorPin(port.SP_DIGI0)==0)
			{
				echoState = false;
				
			}
			else
			{
				echoState = true;
			}
			
			if((!echoState)&&(!lastEchoState))
			{
				lastEchoState = false;
				
				
			}
			if((echoState)&&(!lastEchoState))
			{
				lastEchoState = true;
				echoTimer = System.nanoTime();
			}
			if((!echoState)&&(lastEchoState))
			{
				lastEchoState = false;
				detlaTime = (double)(System.nanoTime() - echoTimer)/1000;
				
			}
			if((echoState)&&(lastEchoState))
			{
				lastEchoState = true;
				
			}
			
			if(displayCount > 10)
			{
				LCD.drawString(""+detlaTime, 0, 2);
				displayCount = 0;
			}
		}
		
		
	}

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new UltrasonicTest(SensorPort.S4);
	}
}
