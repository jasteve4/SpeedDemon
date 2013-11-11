package com.mydomain;



import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class UltrasonicSensor implements Runnable 
{

	short threadCount = -1;
	SensorPort port = null;
	
	public UltrasonicSensor(SensorPort Port) 
	{
		// TODO Auto-generated constructor stub
		port = Port;
		port.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		port.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		new Thread(this).start();
	//	new Thread(this).start();
		LCD.drawString("Ultrasionic Sensor On", 0, 0);
		while(!Button.ENTER.isDown());
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		threadCount++;
		
		if(threadCount == 0)
		{
			boolean portStateHigh = true;
			boolean portStateLow = true;
			short displayCount = 0;
			long startTime = System.currentTimeMillis();
			while(!Button.ESCAPE.isDown())
			{
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
					displayCount++;
				}
				else if(((System.currentTimeMillis()-startTime)>=1)&&portStateLow)
				{
					port.setSensorPin(SensorPort.SP_DIGI1, 0);
					portStateLow = false;
				}
				
			}
		}
		else if(threadCount==1)
		{

			while(!Button.ESCAPE.isDown())
			{
				
			}
		}

	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new UltrasonicSensor(SensorPort.S1);

	}

}
