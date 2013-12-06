package com.mydomain;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class Motor 
{
	private NXTMotor motor;

	public Motor(MotorPort Motor) 
	{
		// TODO Auto-generated constructor stub
		motor = new NXTMotor(Motor);
	}

	public void setSpeed(int setPoint)
	{
		motor.setPower(setPoint);
	}

	public void stop()
	{
		motor.stop();
	}
}
