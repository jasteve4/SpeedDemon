package com.mydomain;

import java.util.ArrayList;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class Motor 
{
	public ArrayList<Integer> powerLogger = new ArrayList<Integer>();
	private NXTMotor motor;
	//private PID motorPID = null;
	private long motorTimer = 0;
	private short detlaTime = 40;
	public long pulseCount = 0;
	private int power = 0;
	public double mult = 1;
	
	
	public Motor(MotorPort Motor) 
	{
		// TODO Auto-generated constructor stub
		motor = new NXTMotor(Motor);
	}
	
	public void PIDSetup(double kP, double kI, double kD)
	{	
		//motorPID = new PID(kP,kI,kD);
	}
	
	public void setSpeed(int setPoint)
	{
		motor.setPower(setPoint);
		/*
		if((System.currentTimeMillis() - motorTimer) >= detlaTime)
		{
			motor.setPower((int) (mult*power));
			motorTimer = System.currentTimeMillis();
			powerLogger.add(setPoint);
		}
		*/
	}
	
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Motor(MotorPort.A);
	}

}
