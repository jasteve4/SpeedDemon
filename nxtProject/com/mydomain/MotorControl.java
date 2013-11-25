package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;

public class MotorControl {

	public Motor leftMotor = null;
	public Motor rightMotor = null;
	
	public MotorControl(MotorPort a, MotorPort b) 
	{
		// TODO Auto-generated constructor stub
		leftMotor = new Motor(a);
		rightMotor = new Motor(b);
		leftMotor.PIDSetup(1, 0, 0);
		rightMotor.PIDSetup(1, 0, 0);
	}


	
	
	public void updateMotors(int leftSpeed, int rightSpeed)
	{
		if(leftSpeed > 100)
			leftSpeed = 100;
		if(leftSpeed < 0)
			leftSpeed = 0;
		if(rightSpeed > 100)
			rightSpeed = 100;
		if(rightSpeed < 0)
			rightSpeed = 0;		
		
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
	}
	
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		MotorControl no = new MotorControl(MotorPort.A,MotorPort.B);
		long displayTimer = System.currentTimeMillis();
		no.leftMotor.mult = 1.2;
		
		while(!Button.ESCAPE.isDown())
		{
			no.updateMotors(10, 10);
			if((System.currentTimeMillis() - displayTimer) >= 100)
			{			
				
				LCD.drawString("" + no.leftMotor.pulseCount, 0, 2);
				LCD.drawString("" + no.rightMotor.pulseCount, 0, 3);
				displayTimer = System.currentTimeMillis();
			}	
		}
	}

}
