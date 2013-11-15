package com.bendomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

import com.mydomain.MotorControl;
import com.mydomain.PID;
import com.mydomain.log;

public class FollowTheLine2 {

	public FollowTheLine2() throws InterruptedException 
	{
		// TODO Auto-generated constructor stub
		LCD.drawString("Follow2", 0, 0);
		Thread.sleep(500);
		IRSensorArray IRArray = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		IRArray.IROn();
		MotorControl motors = new MotorControl(MotorPort.A,MotorPort.B);
		int MAX = 866;  // black
		short timestep = 50;
		int []readings = new int[3];
		PID leftPID = new PID(1,0,0);
		PID rightPID = new PID(1,0,0);
		PID centerPID = new PID(1,0,0);
		
		double leftPostion = 0.0;
		double rightPostion = 0.0;
		double centerPostion = 0.0;
		log logger = new log();
		String logString = new String();
		int leftSpeed = 50;
		int rightSpeed = 50;
		long currentTime = System.currentTimeMillis();
		Button.waitForAnyPress();
		motors.leftMotor.setPower(50);
		motors.rightMotor.setPower(50);
		while(!Button.ESCAPE.isDown())
		{
			if((System.currentTimeMillis()-currentTime) >= timestep)
			{
				readings = IRArray.calculateState();
				leftPostion = leftPID.pid(MAX, readings[0], timestep);
				rightPostion = rightPID.pid(MAX, readings[2], timestep);
				centerPostion = centerPID.pid(MAX, readings[1], timestep);
				motors.leftMotor.setPower((int) (50+(leftPostion-866)/16));
				motors.rightMotor.setPower((int) (50+(rightPostion-866)/16));
				logString = logString + readings[0] + " " + leftPostion + " " + readings[2] + " " +  rightPostion + "\n"; 
				currentTime = System.currentTimeMillis();
			}
		}
		logger.writeToLog(logString);
		logger.closeLog();
		
	}

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new FollowTheLine2();
	}

}
