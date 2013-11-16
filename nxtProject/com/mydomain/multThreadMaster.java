package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class multThreadMaster implements Runnable 
{

	public multThreadMaster() 
	{
		// TODO Auto-generated constructor stub
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new multThreadMaster();
	}

	@Override
	public void run() 
	{
		
		Ping trigger = new Ping(49,SensorPort.S4);
		Echo echo = new Echo(SensorPort.S4);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		
		
		trigger.echo = echo;
		echo.ping = trigger;
		
		trigger.wakeUp();
		System.out.println("here");

		
		
	}

}
