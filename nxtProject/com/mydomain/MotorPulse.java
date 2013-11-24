package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

public class MotorPulse implements Runnable 
{
	public NXTMotor left = new NXTMotor(MotorPort.A);
	public NXTMotor right = new NXTMotor(MotorPort.A);
	public long leftTocho = 0;
	public long tachoTimer = 0;
	public double tachoTime = 0;
	public short time = 0;
	public short timer = 0;
	public double tachoAvg = 0;
	public int threadCount = 0;
	public String record = new String();
	public log logger = new log("PulseTest.txt");
	public boolean end = false;
	public long startTime = 0;
	
	public MotorPulse() 
	{
		// TODO Auto-generated constructor stub
		new Thread(this).start();
		new Thread(this).start();
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new MotorPulse();
	}
	
	public synchronized void motorControl()
	{
		try 
		{
			leftTocho = left.getTachoCount();
			tachoTimer = System.nanoTime();
			startTime = System.nanoTime();
			if((left.getTachoCount() - leftTocho) != 0)
			{
				tachoTime = (double) (System.nanoTime()-tachoTimer)/1000;
				leftTocho = left.getTachoCount();
				tachoTimer = System.nanoTime();
				this.notifyAll();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

	public synchronized void logging()
	{
		try 
		{
			this.wait();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(timer < 99)
		{
			timer++;
			record = (double)(System.nanoTime()-startTime)/1000000 + ", " + tachoTime + "\n";
			
		}
		else
		{
			System.out.println("here: "+ record);
			timer = 0;
			logger.writeToLog(record);
			record = "";
			
		}
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		if(threadCount == 0)
		{
			threadCount++;
			left.setPower(60);
			right.setPower(60);
			while(!Button.ESCAPE.isDown())
			{
				motorControl();
			}
			end = true;
			this.notifyAll();
		}
		else
		{
			while(!Button.ESCAPE.isDown()&&!end)
			{
				logging();
			}
			logger.closeLog();
		}
	}

}
