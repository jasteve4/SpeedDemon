package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;


public class Follow implements Runnable 
{

	public PingLoop ping;
	public FollowDisplay display;
	public double echoValue = 1000;
	public IRSensorArray array = null;
	public int [] readings = {0, 0, 0};
	public int [][] tracker = new int[3][10];
	MotorControl motors;
	public PID echoPID;
	public PID curvePID;
	public int leftSpeed = 80;
	public int rightSpeed = 80;
	public int timeStep = 10;
	public int set = 0;
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
	public int menuSelection = 0;
	public boolean stop = false;
	public int counter = 0;
	public int state = 0;
	public int echoTarget = 0;

	public Follow() 
	{
		// TODO Auto-generated constructor stub		
		ping = new PingLoop(49,SensorPort.S4);
		display = new FollowDisplay();
		LCD.drawString("Left for", 0, 0);
		LCD.drawString("Task 1-2 'curve'", 0, 1);
		LCD.drawString("Right for", 0, 3);
		LCD.drawString("Task 3 'platoon'", 0, 4);
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		menuSelection = getTaskNumber();
		taskSetUp(menuSelection);
		motors = new MotorControl(MotorPort.A,MotorPort.B);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		display.follow = this;

		new Thread(this).start();
	}
	
	public void switchGains()
	{
		if(state == 1)
		{
			curvePID.updateGains(1.4, 1.2, .4);
			state = 0;
		}
		else if(state == 0)
		{
			curvePID.updateGains(1, 1.2, .05);
			state = 1;
		}
	}

	public void taskSetUp(int task)
	{
		LCD.clear();
		if(menuSelection == 1)
		{
			curvePID = new PID(1, 1.2, .05); //(1.9, 1.5, .05)
			echoPID = new PID(1, 0, 0);
			LCD.drawString("Task 1-2 Selected", 0, 0);
			set = 80;
			echoTarget = 150;
		}
		else if(menuSelection == 2)
		{
			curvePID = new PID(.025, .25, .00416);
			echoPID = new PID(1, 0, 0);
			LCD.drawString("Task 3 Selected", 0, 0);
			set = 20;
			echoTarget = 1000;
		}
		else
		{
			LCD.drawString("Invalid Task", 0, 0);
			LCD.drawString("Left for", 0, 2);
			LCD.drawString("Task 1-2 'curve'", 0, 3);
			LCD.drawString("Right for", 0, 5);
			LCD.drawString("Task 3 'platoon'", 0, 6);
			menuSelection = getTaskNumber();
			taskSetUp(menuSelection);
		}
		LCD.drawString("Press Enter", 0, 1);
		LCD.drawString("to Start", 0, 2);
	}

	public int getTaskNumber()
	{
		int menuSelection = Button.waitForAnyPress();
		if(menuSelection == Button.ID_LEFT) //Task 1 and 2: 
		{
			return 1;
		}
		else if(menuSelection == Button.ID_RIGHT) //Task 3:
		{
			return 2;
		}
		else
		{
			return -1;
		}
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
		LCD.clear();
		if(menuSelection == 1)
		{
			LCD.drawString("Task 1-2 Selected", 0, 0);
		}
		else if(menuSelection == 2)
		{
			LCD.drawString("Task 3 Selected", 0, 0);
		}
		ping.wakeUp();	
		display.wakeUp();
		LCD.drawString("Threads Awake", 0, 1);
		leftTunedSpeed = leftSpeed;
		rightTunedSpeed = rightSpeed;
		LCD.drawString("Running...", 0, 2);
		try {
			Thread.sleep(50);
			while(!Button.ESCAPE.isDown())
			{
				Thread.sleep(timeStep);
				if(Math.abs(curveError) > 400)
				{
					counter++;
					if(counter > 15)
					{
						switchGains();
						counter = 0;
					}
				}
//				else
//				{
//					counter++;
//					if(counter > 50)
//					{
//						state = 0;
//						switchGains();
//					}
//				}
				
//				state = 1;
//				switchGains();
//				else
//				{
//					counter = 0;
//				}
				
				echoValue = ping.getPulseLenght()/1000;
				//					LCD.drawString("" + echoValue, 0, 4);

				echoError = (echoPID.pid(echoTarget, echoValue, (double)timeStep/1000))/40; // blah/40

				position = array.calculatePosition();
				curveError = 20 * curvePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR); //360
				readings = array.poleSensor();

				if(menuSelection == 1) // Task 1-2: Line and Block Stop
				{
					leftTunedSpeed = (int)(set - curveError);
					rightTunedSpeed = (int)(set + curveError);
//					if(echoValue < 600)
//					{
//						leftTunedSpeed /= 2;
//						rightTunedSpeed /= 2;
//					}
//					if(echoValue < 300)
//					{
//						leftTunedSpeed /= 5;
//						rightTunedSpeed /= 5;
//					}
					if(echoValue < 300)
					{
						stop = true;
					}
					else
					{
						stop = false;
					}
				}
				else if(menuSelection == 2) //Task 3: Platooning and Redline Stop
				{
					if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
					{
						stop = true;
					}
					leftTunedSpeed = (int)(set - curveError - echoError);
					rightTunedSpeed = (int)(set + curveError - echoError);
				}
				if(stop)
				{
					motors.stopMotors();
					LCD.drawString("Stopped", 0, 3);
				}
				else
				{
					motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
				}
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
	
	public synchronized int getState()
	{
		return state;
	}
	
	public synchronized int[] getSpeed()
	{
		int[] speed = {leftTunedSpeed, rightTunedSpeed};
		return speed;
	}

	public synchronized double getUltraSonicError()
	{
		return echoError;
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