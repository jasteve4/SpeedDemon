package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class SpeedDemon implements Runnable, ButtonListener
{

	LightSensor leftSensor = new LightSensor(SensorPort.S1,false);
	LightSensor middleSensor = new LightSensor(SensorPort.S2,false);
	LightSensor rightSensor = new LightSensor(SensorPort.S3,false);
	private boolean done = false;
	private boolean homeButtonPressed = true;
	private boolean ledButtonPressed = false;
	
	private int select = 0;
	private int ledSelect = 0;
	
	public SpeedDemon() 
	{
		// TODO Auto-generated constructor stub
	    Button.ENTER.addButtonListener(this);
	    Button.LEFT.addButtonListener(this);
	    Button.RIGHT.addButtonListener(this);
	    Button.ESCAPE.addButtonListener(this);
	    
	    new Thread(this).start();
	    
	    
		
		
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			mainMenu();
			if(done){
				break;
			}
		}
			
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new SpeedDemon();
	}

	void mainMenu()
	{
		if(homeButtonPressed)
		{
			if(select == 0)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("Home", 0, 0);
			}
			else if(select == 1)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("Select Mode", 0, 0);
			}
			else if(select == 2)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("Select Mode", 0, 0);
			}
			if(select == 3)
			{
				select = 2;
			}
			homeButtonPressed = false;
		}
		if(ledButtonPressed)
		{
			if(ledSelect == 0)
			{
				homeButtonPressed = true;
			}
			if(ledSelect == 1)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("LED ONE", 0, 0);
			}
			else if(ledSelect == 2)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("LED TWO", 0, 0);
			}
			else if(ledSelect == 3)
			{
				LCD.drawString("                      ", 0, 0);
				LCD.drawString("LED Three", 0, 0);
			}
			if(ledSelect == 4)
			{
				ledSelect = 3;
			}
			else if(ledSelect == -1)
			{
				ledSelect = 0;
			}
			ledButtonPressed = false;
		}
		
		
	}
	
	
	@Override
	public void buttonPressed(Button b) 
	{
		// TODO Auto-generated method stub
		if(b.getId() == 1)
		{
			select++;
			homeButtonPressed = true;
		}
		
		if(b.getId() == 2)
		{
			ledSelect++;
			ledButtonPressed = true;
		}
		
		if(b.getId() == 4)
		{
			ledSelect--;
			ledButtonPressed = true;
		}
		
		if(b.getId() == 8)
		{
			select--;
			homeButtonPressed = true;
		}
		
	}

	@Override
	public void buttonReleased(Button b) 
	{
		// TODO Auto-generated method stub
		if(b.getId() == 1)
		{
			//LCD.drawString("                 ", 0, 0);
		}
		
		if(b.getId() == 2)
		{
			//LCD.drawString("                 ", 0, 0);
		}
		
		if(b.getId() == 4)
		{
		//	LCD.drawString("                 ", 0, 0);
		}
		
		if(b.getId() == 8)
		{
		//	LCD.drawString("                 ", 0, 0);
			if(select == -1)
				done = true;
		}
		
		
		
	}

}
