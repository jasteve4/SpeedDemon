package com.mydomain;

import lejos.nxt.MotorPort;

public class MotorControler {

	public Motor leftMotor = null;
	public Motor rightMotor = null;

	public MotorControler(MotorPort a, MotorPort b) 
	{
		// TODO Auto-generated constructor stub
		leftMotor = new Motor(a);
		rightMotor = new Motor(b);
	}
	
	public void updateMotors(int leftSpeed, int rightSpeed)
	{
		if(leftSpeed > 100)
			leftSpeed = 100;
		if(rightSpeed > 100)
			rightSpeed = 100;
		if(rightSpeed < 0)
			rightSpeed = 0;		
		if(leftSpeed < 0)
			leftSpeed = 0;

		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
	}
//	public void enableReverse()
//	{
//		leftMotor.enableReverse();
//		rightMotor.enableReverse();
//	}
//	
	public void stopMotors()
	{
		leftMotor.stop();
		rightMotor.stop();
	}
}
