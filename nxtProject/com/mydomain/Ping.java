package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class Ping implements Runnable 
{

	private int waitTime = 0;
	private SensorPort port;
	private boolean wakeUp = false;
	public Echo echo = null;
	
	public Ping(int Wait, SensorPort sensor)
	{
		waitTime = Wait;
		port = sensor;
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
		//echo.wakeUp();
		while(!Button.ESCAPE.isDown())
		{
			try
			{
				port.setSensorPin(SensorPort.SP_DIGI1, 1);
				Thread.sleep(1);
				port.setSensorPin(SensorPort.SP_DIGI1, 0);
				Thread.sleep(waitTime);
				echo.wakeUp();
				while((!wakeUp)&&(!Button.ESCAPE.isDown()));
				wakeUp = false;
			}
			catch (Exception e)
			{
				System.out.println("all no");
				e.printStackTrace();
				return;
			}
			
			
		}

	}

}
