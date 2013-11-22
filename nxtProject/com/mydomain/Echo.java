package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class Echo implements Runnable 
{

	public Ping ping = null;
	private boolean wakeUp = false;
	private SensorPort port = null;
	private long riseTime = 0;
	private long pulseTime = 0;
	
	public Echo(SensorPort sensor) 
	{
		// TODO Auto-generated constructor stub
		port = sensor;
		new Thread(this).start();

	}

	public synchronized void wakeUp() 
	{
			wakeUp = true;
	}
	
	public synchronized long getPulseLenght()
	{
		return pulseTime;
	}
	

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		long timeoutTime = 0;
		
		while((!wakeUp)&&(!Button.ESCAPE.isDown()));
			
		while(!Button.ESCAPE.isDown())
		{
			try
			{
				wakeUp = false;
				timeoutTime = System.nanoTime();
				while((port.getSensorPin(SensorPort.SP_DIGI0) == 0)&&(!Button.ESCAPE.isDown()))
				{
					if((System.nanoTime()-timeoutTime)>12000)
					{
						ping.wakeUp();
						timeoutTime = System.nanoTime();
					}
						
				}
				riseTime = System.nanoTime();
				
				while((port.getSensorPin(SensorPort.SP_DIGI0) > 0)&&(!Button.ESCAPE.isDown())&&((System.nanoTime()-riseTime)>5000));
				pulseTime = System.nanoTime() - riseTime;
				Thread.sleep(40);
				
				ping.wakeUp();
				
				while((!wakeUp)&&(!Button.ESCAPE.isDown()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	

}
