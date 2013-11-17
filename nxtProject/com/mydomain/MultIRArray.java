package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class MultIRArray implements Runnable 
{
	private SensorPort leftSensor;
	private SensorPort centerSensor;
	private SensorPort rightSensor;
	private int [] readings = {0, 0, 0};
	private boolean wakeUp = false;
	
	public MultIRArray(SensorPort s1, SensorPort s2, SensorPort s3) 
	{
		// TODO Auto-generated constructor stub
		leftSensor = s1;
		centerSensor = s2;
		rightSensor = s3;
		new Thread(this).start();
	}
	
	public int [] getIRReadings()
	{
		return readings;
	}
	
	public synchronized void wakeUp() 
	{
			wakeUp = true;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while((!wakeUp)&&(!Button.ESCAPE.isDown()));
		while(!Button.ESCAPE.isDown())
		{
			try
			{
			readings[0] = leftSensor.readRawValue();
			readings[1] = centerSensor.readRawValue();
			readings[2] = rightSensor.readRawValue();
			Thread.sleep(2);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
		}
	}

}
