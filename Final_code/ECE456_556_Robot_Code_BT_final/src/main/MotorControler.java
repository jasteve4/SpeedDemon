/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;


import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
/**
 *
 * @author Josh
 */
public class MotorControler {

	public NXTMotor leftMotor = null;
	public NXTMotor rightMotor = null;
	
	public MotorControler(MotorPort a, MotorPort b) 
	{
		// TODO Auto-generated constructor stub
		leftMotor = new NXTMotor(a);
		rightMotor = new NXTMotor(b);
	}


	
	
	public void updateMotors(int leftSpeed, int rightSpeed)
	{
		if(leftSpeed > 100)
			leftSpeed = 100;
		if(leftSpeed < 0)
			leftSpeed = 0;
		if(rightSpeed > 100)
			rightSpeed = 100;
		if(rightSpeed < 0)
			rightSpeed = 0;		
		
		leftMotor.setPower(leftSpeed);
		rightMotor.setPower(rightSpeed);
	}
	
	
	public void stopMotors()
	{
		leftMotor.stop();
		rightMotor.stop();
	}
}