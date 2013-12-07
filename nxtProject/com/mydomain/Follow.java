package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


public class Follow implements Runnable 
{

	public UltrasonicSensorEcho ultrasonicSensor;
	public FollowDisplay display;
	public double echoValue = 1000;
	public IRSensorArray IRArray = null;
	public int [] readings = {0, 0, 0};
	MotorControler motors;
	public PID rangeFinderPID;
	public PID linePID;
	public int leftSpeed = 80;
	public int rightSpeed = 80;
	public int timeStep = 10;
	public int set = 0;
	public double leftPosition = 0.0;
	public double rightPosition = 0.0;
	public double centerPosition = 0.0;
	public double rangePower = 0;
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
		ultrasonicSensor = new UltrasonicSensorEcho(49,SensorPort.S4);
		display = new FollowDisplay();
		LCD.drawString("Left for", 0, 0);
		LCD.drawString("Task 1-2 'curve'", 0, 1);
		LCD.drawString("Right for", 0, 3);
		LCD.drawString("Task 3 'platoon'", 0, 4);
		IRArray = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		menuSelection = getTaskNumber();
		taskSetUp(menuSelection);
		motors = new MotorControler(MotorPort.A,MotorPort.B);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		display.follow = this;

		new Thread(this).start();
	}

	public void switchGains()
	{
		//		double a = 2.5; //1.7 S
		//		double b = 1.25; //1.6 S
		if(state == 1) //Curve
		{
			linePID.updateGains(1.9, 1.5, .05); //(1.9, 1.5, .05)
			//			curvePID.updateGains(1.7, 0, 0);

			//			curvePID.updateGains(0.33*a, a*b/3, a/b); //Curve

			//			curvePID.updateGains(0.33*a, 2*a/b, a*b/3); //Straight
			state = 0;
		}
		else if(state == 0) //Straight
		{
			//			curvePID.updateGains(1.7, 0, 0);
			//			curvePID.updateGains(1.3, 1, .01);
			double a = 1.7;
			double b = 1.6;
			linePID.updateGains(1.9, 1.5, .05);
//			curvePID.updateGains(.561, .906, .01);

			//			curvePID.updateGains(0.33*a, a*b/3, a/b); //Curve

//			curvePID.updateGains(0.33*a, 2*a/b, a*b/3); //Straight
			state = 1;
		}
	}

	public void taskSetUp(int task)
	{
		LCD.clear();
		if(menuSelection == 1)
		{
			linePID = new PID(1, 0, 0); //Initialized
			rangeFinderPID = new PID(1, 0, 0);
			LCD.drawString("Task 1-2", 0, 0);
			set = 80;
			echoTarget = 200;
		}
		else if(menuSelection == 2)
		{
			double a = 1.7;
			double b = 1.6;
			linePID = new PID(0.33*a, 2*a/b, a*b/3);
			rangeFinderPID = new PID(4, 6, .1);
			LCD.drawString("Task 3", 0, 0);
			set = 20;
			echoTarget = 890;
			//			motors.enableReverse();
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
		ultrasonicSensor.wakeUp();	
		display.wakeUp();
		LCD.drawString("Threads Awake", 0, 1);
		leftTunedSpeed = leftSpeed;
		rightTunedSpeed = rightSpeed;
		switchGains();
		LCD.drawString("Running...", 0, 2);
		try {
			Thread.sleep(50);
			while(!Button.ESCAPE.isDown())
			{
				Thread.sleep(timeStep);
				if(menuSelection == 1)
				{
					if(Math.abs(curveError) > 200)
					{
						counter++;
						if(counter > 10)
						{
							switchGains();
							counter = 0;
						}
					}
					if(state == 0)
					{
						Sound.beep();
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

				//				state = 0;
				//				switchGains();
				//				state = 0;
				//				else
				//				{
				//					counter = 0;
				//				}

				echoValue = ultrasonicSensor.getPulseLenght()/1000;
				//					LCD.drawString("" + echoValue, 0, 4);

				rangePower = (rangeFinderPID.pid(echoTarget, echoValue, (double)timeStep/1000))/40; // blah/40

				position = IRArray.calculatePosition();

				curveError = 10 * linePID.pid(0,position,(double)timeStep/1000)/(3*IR_MAX_ERROR); //360

				readings = IRArray.poleSensor();

				if(menuSelection == 1) // Task 1-2: Line and Block Stop
				{
					if(echoValue < 600)
					{
						leftTunedSpeed = (int)(set - curveError - rangePower);
						rightTunedSpeed = (int)(set + curveError - rangePower);
					}
					else
					{
						leftTunedSpeed = (int)(set - curveError);
						rightTunedSpeed = (int)(set + curveError);
					}
					//					if(echoValue < 300)
					//					{
					//						leftTunedSpeed /= 5;
					//						rightTunedSpeed /= 5;
					//					}
					if(echoValue < 400)
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
					rangeFinderPID.capI(100);
					//					if(echoValue < echoTarget)
					//					{
					//						leftTunedSpeed = (int)(set - curveError - echoError)/5;
					//						rightTunedSpeed = (int)(set + curveError - echoError)/5;
					//					}
					//					else
					//					{
					leftTunedSpeed = (int)(set - curveError - rangePower);
					rightTunedSpeed = (int)(set + curveError - rangePower);
					//					}
					if(readings[0] < 800 && readings[0] > 400 && readings[2] < 800 && readings[2] > 400 && readings[1] < 800 && readings[1] > 400)
					{
						stop = true;
					}
				}
				if(stop)
				{
					motors.stopMotors();
					LCD.drawString("Stopped", 0, 3);
				}
				else
				{
					if(menuSelection == 1)
						motors.updateMotors(leftTunedSpeed, rightTunedSpeed);
					else
					{
						if(leftTunedSpeed < 0 && rightTunedSpeed < 0)
						{
							motors.leftMotor.motor.backward();
							motors.rightMotor.motor.backward();
							leftTunedSpeed = set-leftTunedSpeed;
							rightTunedSpeed = set-rightTunedSpeed;

						}
						else
						{
							motors.leftMotor.motor.forward();
							motors.rightMotor.motor.forward();
						}
						motors.updateMotors(leftTunedSpeed, rightTunedSpeed);

					}
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
		return rangePower;
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
		double[] error = {rangePower, leftPosition, centerPosition, rightPosition};
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