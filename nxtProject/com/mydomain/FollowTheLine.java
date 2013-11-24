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
		int MAX = 866;  // black
		int MIN	= 380;
		int IR_MAX_ERROR = MAX - MIN;
		short timestep = 10;
		int []readings = new int[3];
		PID leftPID = new PID(0.9,.01,0);
		PID rightPID = new PID(0.9,.01,0);
		PID centerPID = new PID(2,0,0);
		
		double leftPostion = 0.0;
		double rightPostion = 0.0;
		double centerPostion = 0.0;
		log logger = new log("FollowLine.txt");
		String logString = new String();
		int leftSpeed = 60;
		int rightSpeed = 60;
		
		int leftTunedSpeed = leftSpeed;
		int rightTunedSpeed = rightSpeed;
		
		long currentTime = System.currentTimeMillis();
		Button.waitForAnyPress();
		while(!Button.ESCAPE.isDown())
		{
			/*if((System.currentTimeMillis()-currentTime) >= timestep)
			{
				readings = IRArray.calculateState();
				leftPostion = leftPID.pid(MAX, readings[0], timestep);
				rightPostion = rightPID.pid(MAX, readings[2], timestep);
				centerPostion = centerPID.pid(MAX, readings[1], timestep);
				logString = logString + readings[0] + ", " + leftPostion + ", " + readings[2] + ", " +  rightPostion + "\n"; 
				currentTime = System.currentTimeMillis();
			}*/
			
			if((System.currentTimeMillis()-currentTime) >= timestep)
			{
				readings = IRArray.calculateState();
				
				leftPostion = 20*leftPID.pid(MAX, readings[0], timestep)/IR_MAX_ERROR;
				rightPostion = 20*rightPID.pid(MAX, readings[2], timestep)/IR_MAX_ERROR;
				centerPostion = 20*centerPID.pid(MAX, readings[1], timestep)/IR_MAX_ERROR;
				
				if(IRArray.getState()==2)
				{
					leftTunedSpeed = (int) (leftSpeed+leftPostion);
					rightTunedSpeed = (int) (rightSpeed-leftPostion);
				}
				else if(IRArray.getState()==-2)
				{
					leftTunedSpeed = (int) (leftSpeed-rightPostion);
					rightTunedSpeed = (int) (rightSpeed+rightPostion);
				}
				else
				{
					leftTunedSpeed = (int) (leftSpeed+leftPostion-rightPostion/5);
					rightTunedSpeed = (int) (rightSpeed+rightPostion-leftPostion/5);
				}
				
				motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
				
				logString = logString + readings[0] + " " + leftTunedSpeed + " " + readings[2] + " " +  rightTunedSpeed + "\n"; 
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
