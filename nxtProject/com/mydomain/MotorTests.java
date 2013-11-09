package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class MotorTests 
{
	
	NXTMotor leftMotor = new NXTMotor(MotorPort.A); 
	long currentTime;
	
	public MotorTests() throws InterruptedException 
	{
		// TODO Auto-generated constructor stub
		currentTime = 0;
		LCD.drawString("Motor Test", 0, 0);
		Button.waitForAnyPress();
		LCD.drawString("Start Test", 0, 1);
		start(100);
		long irt = 0;
		while(true)
		{
			if(Button.ESCAPE.isDown())
			{
				break;
			}
			long newTime = System.currentTimeMillis();
			long detlaTime = newTime - currentTime;
			LCD.drawString("Time: " + detlaTime , 0, 2);
			currentTime = newTime;
			Thread.sleep(5);
			irt++;
			
			
		}
		
		LCD.drawString("Times: " + irt , 0, 3);
		while(!Button.ENTER.isDown());
		
	}
	
	public void start(int power)
	{
		currentTime = System.currentTimeMillis();
		leftMotor.setPower(power);
		leftMotor.forward();
		
	}
			
	
	
	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new MotorTests();
	}

}
