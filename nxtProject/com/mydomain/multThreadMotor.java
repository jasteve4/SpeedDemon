package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class multThreadMotor implements Runnable
{
	private NXTMotor motor;
	private boolean wakeUp = false;
	private int power = 0;

	public multThreadMotor(MotorPort port) 
	{
		// TODO Auto-generated constructor stub
		motor = new NXTMotor(port);
		new Thread(this).start();
		
	}
	
	public synchronized void setPower(int setPoint)
	{
		power = setPoint;
	}
	
	
	public synchronized void wakeUp() 
	{
			wakeUp = true;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(!wakeUp);
		while(!Button.ESCAPE.isDown())
		{
			try
			{
			motor.setPower(power);
			Thread.sleep(20);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
		}
		
	}

}
