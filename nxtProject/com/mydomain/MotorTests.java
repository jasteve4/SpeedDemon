package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class MotorTests 
{
	
	NXTMotor leftmotor = new NXTMotor(MotorPort.A); 
	NXTMotor rightmotor = new NXTMotor(MotorPort.B); 	
 	
	public long motorTimer;
	public int time;
	public log logger = new log();
	public short detlaTime = 1;
	
	public MotorTests() throws InterruptedException 
	{
		// TODO Auto-generated constructor stub
		int i = 0;
		long start = System.currentTimeMillis();
		long ext = start;
		String output = new String();
		leftmotor.setPower(40);
		rightmotor.setPower(40);
		long motorTimer = start;
		long timer = start;
		int leftCurrent = 0;
		int rightCurrent = 0;
		int j = 0;
		while(i<40)
		{
				if((System.currentTimeMillis()-motorTimer)>=100)
				{
					output = output + (timer - start) + ", " + (timer - motorTimer) + ", " + (leftmotor.getTachoCount() - leftCurrent) +", "+ (rightmotor.getTachoCount() - rightCurrent) + "\n";
					leftCurrent = leftmotor.getTachoCount();
					rightCurrent = rightmotor.getTachoCount();
					ext = System.currentTimeMillis();
					motorTimer = ext;
					i++;
					
				}
				timer = System.currentTimeMillis();
			
		}
		logger.writeToLog(output);
		time = (int) (motorTimer - ext);
		System.out.println(((leftmotor.getTachoCount()-leftCurrent) +": "+ j));
		logger.closeLog();
		leftmotor.stop();
		rightmotor.stop();
		Button.waitForAnyPress();
			
	}
	
	
	
	public double linerFit(int pulesWidth)
	{
		return (double)(2.6824*pulesWidth) + 4.4731;
	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new MotorTests();
	}

}
