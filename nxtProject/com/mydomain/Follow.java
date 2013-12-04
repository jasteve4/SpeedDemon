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
	public PID echoPID;
	public PID curvePID;
	public int leftSpeed = 80;
	public int rightSpeed = 80;
	public int timeStep = 10;
	public int set = 20;
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
	public double echoMult = 1;
	
	public Follow() 
	{
		// TODO Auto-generated constructor stub		
		ping = new PingLoop(49,SensorPort.S4);
		display = new FollowDisplay();
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		echoPID = new PID(1, 0, 0);
		curvePID = new PID(.025, .25, .00416); // u turn good(1.9, 1.5, .05) straightLinePID(.025, .25, .00416)
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
		boolean stop = false;
		try {
				 
				while(!Button.ESCAPE.isDown())
				{
					Thread.sleep(timeStep);
					echoValue = ping.getPulseLenght()/1000;
					
					echoMult = (echoPID.pid(1000, echoValue, (double)timeStep/1000))/40; // blah/40
					
					position = array.calculatePosition();
					curveError = 20 * curvePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR); //360
					readings = array.poleSensor();
					
					if(echoValue < 250)
					{
						motors.stopMotors();
					}
					else// if(echoValue > 2000)
					{
						leftTunedSpeed = (int)(set + 60 - curveError);
						rightTunedSpeed = (int)(set + 60 + curveError);
						
						if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
						{
							stop = true;
						}
						if(stop)
						{
							motors.stopMotors();
						}
						else
						{
							motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
						}
					}
					//PLATOONING
//					else
//					{
//						leftTunedSpeed = (int)(set - curveError - echoMult);
//						rightTunedSpeed = (int)(set + curveError - echoMult);
//						
//						if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
//						{
//							stop = true;
//						}
//						if(stop)
//						{
//							motors.stopMotors();
//						}
//						else
//						{
//							motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
//						}
//					}
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
	
	public synchronized double getUltraSonicError()
	{
		return echoMult;
	}
	
	public synchronized double getCurveError()
	{
		return curveError;
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