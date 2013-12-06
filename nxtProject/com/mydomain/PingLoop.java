package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class PingLoop implements Runnable {

	private int waitTime = 0;
	private SensorPort port;
	private boolean wakeUp = false;
	private long riseTime = 0;
	private long pulseTime = 0;


	public PingLoop(int Wait, SensorPort sensor)
	{
		waitTime = Wait;
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
		while((!wakeUp)&&(!Button.ESCAPE.isDown()));
		while(!Button.ESCAPE.isDown())
		{
			try
			{
				//pulse
				port.setSensorPin(SensorPort.SP_DIGI1, 1);
				Thread.sleep(1);
				port.setSensorPin(SensorPort.SP_DIGI1, 0);

				//wait for high 
				while((port.getSensorPin(SensorPort.SP_DIGI0) == 0)&&(!Button.ESCAPE.isDown()));
				riseTime = System.nanoTime();
				//read response, timeout after 6ms
				while((port.getSensorPin(SensorPort.SP_DIGI0) > 0)&&(!Button.ESCAPE.isDown())&&((System.nanoTime()-riseTime)>6000));
				pulseTime = System.nanoTime() - riseTime;


				//wait before next pulse
				Thread.sleep(waitTime);


				while((!wakeUp)&&(!Button.ESCAPE.isDown()));
				//wakeUp = false;
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
