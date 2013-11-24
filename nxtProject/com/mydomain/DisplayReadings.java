package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class DisplayReadings implements Runnable 
{
	public Control control = null;
	public boolean wakeUp = false;
	public int [] readings = {0, 0, 0};
	public double [] error = {0, 0, 0, 0};
	public double echoReading = 0;
	public String string;
	public long writeTimer = System.currentTimeMillis();
	public log logger = null;
	public long startTime;
	

	public DisplayReadings() 
	{
		// TODO Auto-generated constructor stub
		logger = new log("ControlTest.txt");
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
		string = "time, echo Reading, left sensor, center sensor, right sensor, echo error, left position, center position, right position\n";
		while(logger == null);
		logger.writeToLog(string);
		LCD.drawString("logger enabled", 0, 7);
		startTime = System.nanoTime();
		try
		{
			while((!wakeUp)&&(!Button.ESCAPE.isDown()));
			while(!Button.ESCAPE.isDown())
			{
				echoReading = control.getUltraSonicReading();
				readings = control.getIRReading();
				error = control.getPosition();
				LCD.drawString("" + echoReading , 0, 2);
				LCD.drawString("" + readings[0] , 0, 4);
				LCD.drawString("" + readings[1] , 0, 5);
				LCD.drawString("" + readings[2] , 0, 6);
				string = string + ((System.nanoTime() - startTime)/1000) + echoReading + ", " + readings[0] + 
						", " + ", " + readings[1] +  ", " + readings[2] + 
						", " + error[0] +  ", " + error[1] +  ", " + error[2] +  
					", " + error[3] + "\n";
				if((System.currentTimeMillis() - writeTimer) >= 1000)
				{
					logger.writeToLog(string);
					string = "";
					writeTimer = System.currentTimeMillis();
				}
				Thread.sleep(100);
			}
			logger.closeLog();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
