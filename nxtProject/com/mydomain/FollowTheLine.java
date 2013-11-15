package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class FollowTheLine {

	public FollowTheLine() throws InterruptedException 
	{
		// TODO Auto-generated constructor stub
		LCD.drawString("Follow", 0, 0);
		Button.waitForAnyPress();
		Thread.sleep(500);
		IRSensorArray IRArray = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		IRArray.IROn();
		MotorControl motors = new MotorControl(MotorPort.A,MotorPort.B);
		int MAX = 846;  // black
		short timestep = 20;
		int []readings = new int[3];
		PID leftPID = new PID(1,0,0);
		PID rightPID = new PID(1,0,0);
		PID centerPID = new PID(1,0,0);
		
		double leftPostion = 0.0;
		double rightPostion = 0.0;
		double centerPostion = 0.0;
		log logger = new log();
		String logString = new String();
		int leftSpeed = 60;
		int rightSpeed = 60;
		long currentTime = System.currentTimeMillis();
		Button.waitForAnyPress();
		while(!Button.ESCAPE.isDown())
		{
			if((System.currentTimeMillis()-currentTime) >= timestep)
			{
				readings = IRArray.calculateState();
				leftPostion = leftPID.pid(MAX, readings[0], timestep);
				rightPostion = rightPID.pid(MAX, readings[2], timestep);
				centerPostion = centerPID.pid(MAX, readings[1], timestep);
				motors.updateMotors((int)(leftSpeed+ 20*leftPostion/446), (int)(rightSpeed + 20*rightPostion/446));
				logString = logString + readings[0] + ", " + leftPostion + ", " + readings[2] + ", " +  rightPostion + "\n"; 
				currentTime = System.currentTimeMillis();
			}
		}
		logger.writeToLog(logString);
		logger.closeLog();
		
	}

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		new FollowTheLine();
	}

}
