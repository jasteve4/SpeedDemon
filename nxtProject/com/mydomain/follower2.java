package com.mydomain;

import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class follower2 
{

	public PingLoop ping;
	public FollowerDisplay followerDisplay;
	public double echoValue = 0;
	public IRSensorArray array = null;
	public int [] readings = {0, 0, 0};
	MotorControl motors;
	public PID echoPid;
	public PID leftPID;
	public PID rightPID;
	public PID centerPID;
	public int leftSpeed = 60;
	public int rightSpeed = 60;
	public int timeStep = 10;
	public double leftError = 0.0;
	public double rightError = 0.0;
	public double centerError = 0.0;
	public double echoError = 0;
	public int MAX = 866;  // black
	public int MIN	= 380;
	public int IR_MAX_ERROR = MAX - MIN;
	public int leftTunedSpeed;
	public int rightTunedSpeed;
	
	public follower2() 
	{
		// TODO Auto-generated constructor stub
		ping = new PingLoop(49,SensorPort.S4);
		//followerDisplay = new FollowerDisplay();
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		echoPid = new PID(1,0,0);
		leftPID = new PID(0.9,.01,0);
		rightPID = new PID(0.9,.01,0);
		centerPID = new PID(2,0,0);		
		motors = new MotorControl(MotorPort.A,MotorPort.B);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		//followerDisplay.follower = this;
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new follower2();

	}

}
