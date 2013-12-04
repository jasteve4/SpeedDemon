package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class FollowTADisplay implements Runnable 
{
	public FollowTA follow = null;
	public boolean wakeUp = false;
	public int [] readings = {0, 0, 0};
	public double [] error = {0, 0, 0, 0};
	public double echoReading = 0;
	public String string;
	public long writeTimer = System.currentTimeMillis();
	public log logger = null;
	public long startTime;
	public int [] power = {0, 0};
	public double position = 0;
	

	public FollowTADisplay() 
	{
		// TODO Auto-generated constructor stub
		logger = new log("Control.txt");
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	
	public synchronized void wakeUp() 
	{
			wakeUp = true;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		LCD.drawString("UltraSonic", 0, 1);
		LCD.drawString("IR Readings ", 0, 3);
		string = "time, echo Reading, left sensor, center sensor, right sensor, left power, right power, echo error, left position, center position, right position, postion\n";
		while(logger == null);
		logger.writeToLog(string);
		LCD.drawString("logger enabled n", 0, 7);
		
		try
		{
			while((!wakeUp)&&(!Button.ESCAPE.isDown()));
			startTime = System.nanoTime();
			while(!Button.ESCAPE.isDown())
			{
				echoReading = follow.getUltraSonicReading();
				readings = follow.getIRReading();
				error = follow.getPosition();
				power = follow.getPower();
				position = follow.getPostion2();
			/*	LCD.drawString("" + echoReading , 0, 2);
				LCD.drawString("" + readings[0] , 0, 4);
				LCD.drawString("" + readings[1] , 0, 5);
				LCD.drawString("" + readings[2] , 0, 6);*/
				string = string + ((double)(System.nanoTime() - startTime)/1000000) + ", " + echoReading + ", " + readings[0]
						+ ", " + readings[1] +  ", " + readings[2] + 
						", " + power[0] + ", " + power[1] + ", " + error[0] +  ", " + error[1] +  ", " + error[2] +  
					", " + error[3] + ", " + position +"\n";
				if((System.currentTimeMillis() - writeTimer) >= 1000)
				{
					logger.writeToLog(string);
					string = "";
					writeTimer = System.currentTimeMillis();
				}
				Thread.sleep(10);
			}
			logger.closeLog();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
