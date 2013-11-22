package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;


public class Control implements Runnable 
{

	public Ping ping;
	public Echo echo;
	public DisplayReadings display;
	public double echoValue = 0;
	public IRSensorArray array = null;
	public int [] readings = {0, 0, 0};
	public log logger;
	public NXTMotor leftMotor;
	public NXTMotor rightMotor;
	public PID echoPid;
	public PID leftPid;
	public PID rightPid;
	public PID centerPid;
	public int leftSpeed = 60;
	public int rightSpeed = 60;
	public int timeStep = 10;
	public int MAX = 846;
	public double leftError = 0;
	public double rightError = 0;
	public double centerError = 0;
	public double echoError = 0;
	
	public Control() throws InterruptedException 
	{
		// TODO Auto-generated constructor stub		
		ping = new Ping(49,SensorPort.S4);
		echo = new Echo(SensorPort.S4);
		display = new DisplayReadings();
		array = new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		leftMotor = new NXTMotor(MotorPort.A);
		rightMotor = new NXTMotor(MotorPort.B);
		echoPid = new PID(1,0,0);
		rightPid = new PID(1,0,0);
		leftPid = new PID(1,0,0);
		centerPid = new PID(1,0,0);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		ping.echo = echo;
		echo.ping = ping;
		display.control = this;
		display.logger = logger;
		LCD.drawString("System Ready", 0, 0);
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try {
			new Control();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub

		while(!Button.ENTER.isDown());
		LCD.drawString("System Active", 0, 0);
		ping.wakeUp();		
		display.wakeUp();
		leftMotor.setPower(leftSpeed);
		rightMotor.setPower(rightSpeed);
		try {
				 
				while(!Button.ESCAPE.isDown())
				{
					Thread.sleep(timeStep);
					echoValue = echo.getPulseLenght()/1000;
					readings = array.calculateState();
					leftError = leftPid.pid(MAX, readings[0], timeStep);
					rightError = rightPid.pid(MAX, readings[2], timeStep);
					centerError = centerPid.pid(MAX, readings[1], timeStep);
					echoError = echoPid.pid(1000, echoValue, timeStep);
					leftMotor.setPower((int) (leftSpeed+20*leftError/446 - 2*rightError/446));
					rightMotor.setPower((int) (rightSpeed+20*rightError/446 - 2*leftError/446));
				}
		}
		catch (InterruptedException e) 
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		logger.writeToLog(display.string);
		logger.closeLog();
	}
	
	public synchronized double getUltraSonicReading()
	{
		return echoValue;
	}
	
	public synchronized int [] getIRReading()
	{
		return readings;
	}

	public synchronized double [] getError()
	{
		double[] error = {echoError, leftError, centerError, rightError};
		return error;
	}
	
	

}
