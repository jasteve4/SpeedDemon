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
	public PID curvePID;
	public int leftSpeed = 70;
	public int rightSpeed = 70;
	public int timeStep = 5;
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
	public double curveError = 0;
	
	
	public Follow() 
	{
		// TODO Auto-generated constructor stub		
		ping = new PingLoop(49,SensorPort.S4);
		display = new FollowDisplay();
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		echoPid = new PID(1,0,0);
		leftPID = new PID(0.9,.01,0);
		rightPID = new PID(0.9,.01,0);
		centerPID = new PID(.9,0,0);	
		positionPID = new PID(.9,3.1,.0001); 		// kc = .95
		curvePID = new PID(1.95, 0, .001); // 2.1, 4, .001
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
					//echoError = echoPid.pid(1000, echoValue, timeStep/1000);
					position = array.calculatePosition();
					curveError = 20 * curvePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR);
					error = 20 * positionPID.pid(0,position,timeStep/1000)/(3*IR_MAX_ERROR);
					
				/*	if(echoValue < 1000)
					{
						motors.stopMotors();
						break;
					}*/
					motors.updateMotors((int)(70 - curveError),(int)(70 + curveError));
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
