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
	public log logger = new log("MotorTest.txt");
	public short detlaTime = 1;
	
	public MotorTests() throws InterruptedException 
	{
		String output = new String();
		long start = System.currentTimeMillis();
		long ext = start;
		Button.waitForAnyPress();
		for(int pow = 0; pow <= 100; pow += 10)
		{
			int i = 0;
			start = System.currentTimeMillis();
			ext = start;
			leftmotor.setPower(pow);
			rightmotor.setPower(pow);
			long motorTimer = start;
			long timer = start;
			int leftCurrent = 0;
			int rightCurrent = 0;
			int j = 0;
			while(i<10&&!Button.ESCAPE.isDown())
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
		}
		logger.writeToLog(output);
		time = (int) (motorTimer - ext);
//		System.out.println(((leftmotor.getTachoCount()-leftCurrent) +": "+ j));
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
