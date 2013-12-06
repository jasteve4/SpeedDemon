package com.mydomain;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class Motor 
{
	private NXTMotor motor;
	private boolean reverseEnabled = false;

	public Motor(MotorPort Motor) 
	{
		// TODO Auto-generated constructor stub
		motor = new NXTMotor(Motor);
	}

	public void setSpeed(int setPoint)
	{
//		if(reverseEnabled&&setPoint<0)
//		{
//			setPoint = Math.abs(setPoint);
//			motor.backward();
//		}
//		else if(!reverseEnabled&&setPoint<0)
//		{
//			setPoint = 0;
//			motor.forward();
//		}
//		else
//		{
//			motor.forward();
//		}
		motor.setPower(setPoint);
	}
	
	public void enableReverse()
	{
		reverseEnabled = true;
	}
	
	public void stop()
	{
		motor.stop();
	}
}
