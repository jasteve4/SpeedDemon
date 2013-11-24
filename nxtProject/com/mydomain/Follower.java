package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;


public class Follower implements Runnable 
{

	public PingLoop ping;
	//public FollowerDisplay followerDisplay;
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
	
	public double dtime = 0.0;
	
	public Follower() 
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

		LCD.drawString("System Ready", 0, 0);
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Control();
	
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
		/* 0 is all on line
			1 is left off, both on
			2 is both off, right on
			3 is all off
			-1 is right off, both on
			-2 is both off, left on
			-3 is all off
		*/
		long current_time= System.nanoTime();
		long prev_time = System.nanoTime();

		while(!Button.ENTER.isDown());
		LCD.drawString("System Active", 0, 0);
		ping.wakeUp();		
		//followerDisplay.wakeUp();
		leftTunedSpeed = leftSpeed;
		rightTunedSpeed = rightSpeed;
		try {
				 
				while(!Button.ESCAPE.isDown())
				{
					Thread.sleep(timeStep);
					echoValue = ping.getPulseLenght()/1000;
					echoError = echoPid.pid(1000, echoValue, timeStep);
					
					readings = array.calculateState();
					
					//normalize PID values (motors at 08, maximum add 20)
					//IR_MAX_ERROR/2+MIN  yields gray target value
					current_time= System.nanoTime();
					dtime =  (current_time-prev_time) / 1000;
					leftError = 20*leftPID.pid(IR_MAX_ERROR/2+MIN, readings[0], dtime)/IR_MAX_ERROR;
					rightError = 20*rightPID.pid(MAX, readings[2], dtime)/IR_MAX_ERROR;
					centerError = 20*centerPID.pid(IR_MAX_ERROR/2+MIN, readings[1], dtime)/IR_MAX_ERROR;
					prev_time = current_time;
					
					
					if( leftError < rightError ){
						leftTunedSpeed = (int) (leftSpeed+leftError + centerError);
						rightTunedSpeed = (int) (rightSpeed+rightError);
					}
					else{
						leftTunedSpeed = (int) (leftSpeed+leftError );
						rightTunedSpeed = (int) (rightSpeed+rightError + centerError);
					}
										//if all off line,
					//stop and admit defeat
					if((readings[0] < 400) && (readings[1] < 400) && (readings[2] < 400) ){
						leftTunedSpeed = (int) 0;
						rightTunedSpeed = (int) 0;
					}
					
					
					/*old code
					if(array.getState()==2)
					{
						leftTunedSpeed = (int) (leftTunedSpeed+leftPosition + center);
						rightTunedSpeed = (int) (rightTunedSpeed-leftPosition);
					}
					else if(array.getState()==-2)
					{
						leftTunedSpeed = (int) (leftTunedSpeed-rightPosition);
						rightTunedSpeed = (int) (rightTunedSpeed+rightPosition);
					}
					else
					{
						leftTunedSpeed = (int) (leftTunedSpeed+leftPosition-rightPosition/5);
						rightTunedSpeed = (int) (rightTunedSpeed+rightPosition-leftPosition/5);
					}
					*/
					
					motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
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
		double[] error = {echoError, leftError, centerError, rightError};
		return error;
	}
	
	public synchronized int [] getPower()
	{
		int [] power = {leftTunedSpeed, rightTunedSpeed};
		return power;
	}
	
	public synchronized double getDTime()
	{
		
		return dtime;
		
	}
	

}