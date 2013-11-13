package com.mydomain;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class IRSensorArray 
{

	private LightSensor leftIR = null;
	private LightSensor middleIR = null;
	private LightSensor rightIR = null;
	private double kp = 10;
	private double kd = 1;
	private double ki = .1;
	private short MAX = 1000;
	private short MIN = 200;
	
	public IRSensorArray(SensorPort left,SensorPort middle, SensorPort right) 
	{
		// TODO Auto-generated constructor stub
		leftIR = new LightSensor(left,false);
		middleIR = new LightSensor(middle,false);
		rightIR = new LightSensor(right,false);
	}
	
	public void IROn()
	{
		leftIR.setFloodlight(true);
		middleIR.setFloodlight(true);
		rightIR.setFloodlight(true);	
	}
	
	private short poleLeft()
	{
		return (short)leftIR.getNormalizedLightValue();
	}
	
	private short poleRight()
	{
		return (short)rightIR.getNormalizedLightValue();
	}	
	
	private short polemiddle()
	{
		return (short)middleIR.getNormalizedLightValue();
	}
	
	public double iRError()
	{
		double state = calculateState();
		
		
		return 0.0;
	}
	
	private short calculateState()
	{
		short leftReading = poleLeft();
		short rightReading = poleRight();
		short middleReading = 0;
		if((leftReading >= MAX)||(rightReading >= MAX)) // is either sensor seeing all white
			middleReading = polemiddle();
		if(middleReading == 0) 		// if the line is in the middle of the robot
		{
			if(leftReading > MIN)  // left sensor is reading
			{
				if(leftReading >= 3*MAX/4)
				{
					return -6;
				}
				else if((leftReading < 3*MAX/4)&&(leftReading >= 2*MAX/4))
				{
					return -5;
					
				}
				else if((leftReading < 2*MAX/4)&&(leftReading >= MAX/4))
				{
					return -4;
				}			
				else
				{
					return -3;
				}
			}
			if(rightReading >= MIN)
			{
				if(rightReading >= 3*MAX/4)
				{
					return 6;
				}
				else if((rightReading < 3*MAX/4)&&(rightReading >= 2*MAX/4))
				{
					return 5;
					
				}
				else if((rightReading < 2*MAX/4)&&(rightReading >= MAX/4))
				{
					return 4;
				}			
				else
				{
					return 3;
				}
			}
			return 0;
		}
		else						// line is to the right or left of the robot
		{
			if(leftReading >= MAX)  // line is on the right robot
			{
					if(middleReading >= 3*MAX/4)
					{
						
					}
					else if((middleReading < 3*MAX/4)&&(middleReading >= 2*MAX/4))
					{
						
						
					}
					else if((middleReading < 2*MAX/4)&&(middleReading >= MAX/4))
					{
						
						
					}			
					else
					{
						
						
					}
					
					
					if(rightReading >= 3*MAX/4)
					{
						
					}
					else if((rightReading < 3*MAX/4)&&(rightReading >= 2*MAX/4))
					{
						
						
					}
					else if((rightReading < 2*MAX/4)&&(rightReading >= MAX/4))
					{
						
						
					}			
					else
					{
						
						
						
					}
			}
			else					// line is on the left robot
			{
				if(leftReading >= 3*MAX/4)
				{
					
				}
				else if((leftReading < 3*MAX/4)&&(leftReading >= 2*MAX/4))
				{
					
					
				}
				else if((leftReading < 2*MAX/4)&&(leftReading >= MAX/4))
				{
					
					
				}			
				else
				{
					
					
				}
				
				
				if(middleReading >= 3*MAX/4)
				{
					
				}
				else if((middleReading < 3*MAX/4)&&(middleReading >= 2*MAX/4))
				{
					
					
				}
				else if((middleReading < 2*MAX/4)&&(middleReading >= MAX/4))
				{
					
					
				}			
				else
				{
					
					
					
				}
				
				
				
			}
			
			
		}
		return 0;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new IRSensorArray(SensorPort.S1,SensorPort.S2,SensorPort.S3);

	}

}
