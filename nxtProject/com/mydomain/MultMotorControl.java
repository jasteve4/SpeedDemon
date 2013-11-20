package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class MultMotorControl implements Runnable
{
	private boolean wakeUp = false;
	private multThreadMotor leftMotor;
	private multThreadMotor rightMotor;
	private Ping trigger;
	private Echo echo;
	private MultIRArray irArray;
	private PID rightMotorPID;
	private PID centerAdjustPID;
	private PID leftMotorPID;
	private PID echoPID;
	private int [] readings = {0, 0, 0};
	private long echoReading = 0;
	private Display display = null; 
	
	public MultMotorControl() 
	{
		// TODO Auto-generated constructor stub
		trigger = new Ping(49,SensorPort.S4);
		echo = new Echo(SensorPort.S4);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI0, SensorPort.SP_MODE_INPUT);
		SensorPort.S4.setSensorPinMode(SensorPort.SP_DIGI1, SensorPort.SP_MODE_OUTPUT);
		//leftMotor = new multThreadMotor(MotorPort.A);
		//rightMotor = new multThreadMotor(MotorPort.B);
		irArray = new MultIRArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);
		leftMotorPID = new PID(1, 0, 0);
		centerAdjustPID = new PID(1,0,0);
		rightMotorPID = new PID(1, 0, 0);
		echoPID = new PID(1, 0, 0);
		display = new Display();
		
		display.motorControl = this;
		trigger.echo = echo;
		echo.ping = trigger;
		
		
		
	}
	
	public static void main()
	{
		new MultMotorControl();
	}
	
	
	public synchronized void wakeUp() 
	{
			wakeUp = true;
	}
	
	public synchronized int [] getIR()
	{
		return readings;
	}
	
	public synchronized long gerEchoReading()
	{
		return echoReading;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		double echoAdjust = 0;
		double leftWheelAdjust = 0;
		double centerAdjust = 0;
		double rightWheelAdjust = 0;
		int echoTarget = 0;
		int leftMotorTarget = 0;
		int rightMotorTarget = 0;
		int centerMotorTarget = 0;
		int echoTimestep = 50;
		int motorTimestep = 10;
		int setPoint = 0;
		
		trigger.wakeUp();
		//leftMotor.wakeUp();
		//rightMotor.wakeUp();
		irArray.wakeUp();
		display.wakeUp();
		
		while((!wakeUp)&&(!Button.ESCAPE.isDown()));
		while(!Button.ESCAPE.isDown())
		{
			readings = irArray.getIRReadings();
			echoReading = echo.getPulseLenght();
			echoAdjust = echoPID.pid(echoTarget, echoReading, echoTimestep);
			leftWheelAdjust = leftMotorPID.pid(leftMotorTarget, readings[0], motorTimestep);
			centerAdjust = leftMotorPID.pid(centerMotorTarget, readings[0], motorTimestep);
			rightWheelAdjust = leftMotorPID.pid(rightMotorTarget, readings[0], motorTimestep);
			//leftMotor.setPower(setPoint);
			//rightMotor.setPower(setPoint);
		}
		
	}

}