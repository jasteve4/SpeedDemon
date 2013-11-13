package com.mydomain;



import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class UltrasonicSensor
{

	private SensorPort port = null;
	private boolean portStateHigh = true;
	private boolean portStateLow = true;
	public long startTime = 0; //System.currentTimeMillis();
	public short echoValue = 0;
	public short echoValue1 = 0;
	public boolean echoState = false;
	public boolean lastEchoState = false;
	private long echoTimer = 0;
	public double detlaTime = 0;
	
	
	public UltrasonicSensor(SensorPort Port) 
	{
		// TODO Auto-generated constructor stub
		port = Port;
		port.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		port.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		LCD.drawString("Ultrasionic Sensor On", 0, 0);
		while(!Button.ENTER.isDown());
	}
	
	

	public void pig() 
	{
		// TODO Auto-generated method stub
		if(((System.currentTimeMillis()-startTime)<1)&&(portStateHigh))
		{
			port.setSensorPin(SensorPort.SP_DIGI1, 1);
			portStateHigh = false;
		}
		else if(((System.currentTimeMillis()-startTime)>=49))
		{
			portStateHigh = true;
			portStateLow = true;
			startTime = System.currentTimeMillis();
		}
		else if(((System.currentTimeMillis()-startTime)>=1)&&portStateLow)
		{
			port.setSensorPin(SensorPort.SP_DIGI1, 0);
			portStateLow = false;
		}
	}
	
	
	public boolean echo()
	{
		if(port.getSensorPin(SensorPort.SP_DIGI0)==0)
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
			return true;
			
		}
		if((echoState)&&(lastEchoState))
		{
			lastEchoState = true;
			
		}
		return false;
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new UltrasonicSensor(SensorPort.S1);

	}

}
