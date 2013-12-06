package com.mydomain;

import java.util.ArrayList;

import lejos.nxt.Button;

public class FollowDisplay implements Runnable 
{
	public Follow follow = null;
	public boolean wakeUp = false;
	public int [] readings = {0, 0, 0};
	public double [] error = {0, 0, 0, 0};
	public double echoPID = 0;
	public double echoReading = 0;
	public double curveErr = 0;
	public String string;
	public long writeTimer = System.currentTimeMillis();
	public log logger = null;
	public long startTime;
	public int [] power = {0, 0};
	public double position = 0;
	public int logTime = 100;
	public int logSleepTime = 20;
	public int state = 0;
	public int [] speed = new int[2];
	public ArrayList<String> array = new ArrayList<String>();

	public FollowDisplay() 
	{
		// TODO Auto-generated constructor stub
		logger = new log("Control.txt");
		new Thread(this).start();
	}

	public synchronized void wakeUp() 
	{
		wakeUp = true;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		//		LCD.drawString("UltraSonic", 0, 1);
		//		LCD.drawString("IR Readings ", 0, 3);
		string = "time, left sensor, center sensor, right sensor, postion, echoReading, echoError, curveError, leftSpeed, rightSpeed, state\n";
		while(logger == null);
		logger.writeToLog(string);
		//		LCD.drawString("logger enabled n", 0, 7);

		try
		{
			while((!wakeUp)&&(!Button.ESCAPE.isDown()));
			startTime = System.nanoTime();
			while(!Button.ESCAPE.isDown())
			{
				echoReading = follow.getUltraSonicReading();
				echoPID = follow.getUltraSonicError();
				curveErr = follow.getCurveError();
				readings = follow.getIRReading();
				state = follow.getState();
				speed = follow.getSpeed();
				//error = follow.getPosition();
				power = follow.getPower();
				position = follow.getPostion2();
				string = ((double)(System.nanoTime() - startTime)/1000000) + ", " + readings[0]
						+ ", " + readings[1] +  ", " + readings[2] + 
						", " + position + ", " + echoReading + ", " + echoPID + "," + curveErr + 
						", " + speed[0] + ", " + speed[1] + ", " + state + "\n";
				if((System.currentTimeMillis() - writeTimer) >= logTime)
				{
					array.add(string);
					string = "";
					writeTimer = System.currentTimeMillis();
				}
				Thread.sleep(logSleepTime);
			}
			for(int n = 0; n < array.size();n++)
				logger.writeToLog(array.get(n));
			logger.closeLog();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}