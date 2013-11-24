package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;


public class Follow implements Runnable 
{

	public PingLoop ping;
	public FollowDisplay display;
	public double echoValue = 0;
	public IRSensorArray array = null;
	public int [] readings = {0, 0, 0};
	MotorControl motors;
	public PID echoPid;
	public PID leftPID;
	public PID rightPID;
	public PID centerPID;
	public PID positionPID;
	public int leftSpeed = 60;
	public int rightSpeed = 60;
	public int timeStep = 10;
	public double leftPosition = 0.0;
	public double rightPosition = 0.0;
	public double centerPosition = 0.0;
	public double echoError = 0;
	public int MAX = 866;  // black
	public int MIN	= 380;
	public int IR_MAX_ERROR = MAX - MIN;
	public int leftTunedSpeed;
	public int rightTunedSpeed;
	public double position = 0;
	public double error = 0;
	
	
	public Follow() 
	{
		// TODO Auto-generated constructor stub		
		ping = new PingLoop(49,SensorPort.S4);
		display = new FollowDisplay();
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		echoPid = new PID(1,0,0);
		leftPID = new PID(0.9,.01,0);
		rightPID = new PID(0.9,.01,0);
		centerPID = new PID(2,0,0);	
		positionPID = new PID(.6,0,0);
		motors = new MotorControl(MotorPort.A,MotorPort.B);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		display.follow = this;

		LCD.drawString("System Ready", 0, 0);
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Follow();
	
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub

		while(!Button.ENTER.isDown());
		LCD.drawString("System Active", 0, 0);
		ping.wakeUp();		
		display.wakeUp();
		leftTunedSpeed = leftSpeed;
		rightTunedSpeed = rightSpeed;
		try {
				 
				while(!Button.ESCAPE.isDown())
				{
					Thread.sleep(timeStep);
					echoValue = ping.getPulseLenght()/1000;
					echoError = echoPid.pid(1000, echoValue, timeStep);
					position = array.calculatePosition();
					error = positionPID.pid(0,position,timeStep);
					
				//	readings = array.calculateState();
					
					/*leftPosition = 20*leftPID.pid(MAX, readings[0], timeStep)/IR_MAX_ERROR;
					rightPosition = 20*rightPID.pid(MAX, readings[2], timeStep)/IR_MAX_ERROR;
					centerPosition = 20*centerPID.pid(MAX, readings[1], timeStep)/IR_MAX_ERROR;
					
					if(array.getState()==2)
					{
						leftTunedSpeed = (int) (leftSpeed+leftPosition);
						rightTunedSpeed = (int) (rightSpeed-leftPosition);
					}
					else if(array.getState()==-2)
					{
						leftTunedSpeed = (int) (leftSpeed-rightPosition);
						rightTunedSpeed = (int) (rightSpeed+rightPosition);
					}
					else
					{
						leftTunedSpeed = (int) (leftSpeed+leftPosition-rightPosition/5);
						rightTunedSpeed = (int) (rightSpeed+rightPosition-leftPosition/5);
					}*/
					
					motors.updateMotors((int)(60 + error),(int)(60 - error));
				}
		}
		catch (InterruptedException e) 
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public synchronized double getUltraSonicReading()
	{
		return echoValue;
	}
	
	public synchronized int [] getIRReading()
	{
		return readings;
	}

	public synchronized double [] getPosition()
	{
		double[] error = {echoError, leftPosition, centerPosition, rightPosition};
		return error;
	}
	
	public synchronized int [] getPower()
	{
		int [] power = {leftTunedSpeed, rightTunedSpeed};
		return power;
	}
	
	public synchronized double getPostion2()
	{
		return position;
	}

}
