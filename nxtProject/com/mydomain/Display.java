package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;


public class Display implements Runnable
{

	private boolean wakeUp = false;
	public MultMotorControl motorControl  = null;
	
	public Display() 
	{
		// TODO Auto-generated constructor stub
		LCD.drawString("Display", 0, 0);
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
		while((!wakeUp)&&(!Button.ESCAPE.isDown()));
		while(!Button.ESCAPE.isDown())
		{
			try
			{
				int [] readings = motorControl.getIR();
				LCD.drawString("Left LED:   " + readings[0] + "      ", 0, 1);
				LCD.drawString("Middle LED: " + readings[1] + "      ", 0, 2);
				LCD.drawString("Right LED:  " + readings[2] + "      ", 0, 3);
				LCD.drawString("Echo: " + motorControl.gerEchoReading(), 0, 4);
				
				
				Thread.sleep(100);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
