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
	public log logger = null;
	public String string;

	public DisplayReadings() 
	{
		// TODO Auto-generated constructor stub
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
		string = "echo Reading, left sensor, center sensor, right sensor, echo error, left error, center error, right error\n";
		try
		{
			while(!wakeUp)
			{
				Thread.sleep(100);
			}
			while(!Button.ESCAPE.isDown())
			{
				echoReading = control.getUltraSonicReading();
				readings = control.getIRReading();
				error = control.getError();
				LCD.drawString("" + echoReading , 0, 2);
				LCD.drawString("" + readings[0] , 0, 4);
				LCD.drawString("" + readings[1] , 0, 5);
				LCD.drawString("" + readings[2] , 0, 6);
				string = string + echoReading + ", " + readings[0] + 
						", " + ", " + readings[1] +  ", " + readings[2] + 
						", " + error[0] +  ", " + error[1] +  ", " + error[2] +  
						", " + error[3] + "\n"; 
				Thread.sleep(100);
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
