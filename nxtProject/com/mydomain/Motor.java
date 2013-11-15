package com.mydomain;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class Motor 
{
	private NXTMotor motor;
	private PID motorPID = null;
	private long motorTimer = 0;
	private short detlaTime = 40;
	private long count = 0;
	public long pulseCount = 0;
	private int speed = 0;
	private int power = 0;
	public double mult = 1;
	
	
	public Motor(MotorPort Motor) 
	{
		// TODO Auto-generated constructor stub
		motor = new NXTMotor(Motor);
	}
	
	public void PIDSetup(double kP, double kI, double kD)
	{	
		motorPID = new PID(kP,kI,kD);
	}
	
	public void setSpeed(int setPoint)
	{
		if((System.currentTimeMillis() - motorTimer) >= detlaTime)
		{
			pulseCount = (motor.getTachoCount()-count);
			motorTimer = System.currentTimeMillis();
			speed = (int) motorPID.pid(setPoint, pulseCount, detlaTime);
			power = (int)linerFit(speed);
			motor.setPower(power);
			count = motor.getTachoCount();
		}
	}
	
	public void setPower(int setPoint)
	{
		motor.setPower(setPoint);
	}
	
	public double linerFit(int pulesWidth)
	{
		return (double)(mult*2.6824*pulesWidth) + 4.4731;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Motor(MotorPort.A);
	}

}
