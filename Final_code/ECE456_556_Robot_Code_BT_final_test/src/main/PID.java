/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Josh
 */
public class PID {

	private double kp;
	private double kd;
	private double ki;
	private double currentError;
	private double previousError;
	private double integralError;
	private double pError;
	private double dError;
	private double iError;
	private double totalError;
	
	public PID() 
	{
		// TODO Auto-generated constructor stub
	kp = 1;
	kd = 0;
	ki = 0;
	currentError = 0;
	previousError = 0;
	integralError  = 0;
	dError = 0;
	pError = 0;
	iError = 0;
	totalError = 0;
	}
	
	public PID(double KP, double KD, double KI) 
	{
		// TODO Auto-generated constructor stub
	kp = KP;
	kd = KD;
	ki = KI;
	currentError = 0;
	previousError = 0;
	integralError  = 0;
	dError = 0;
	pError = 0;
	iError = 0;
	totalError = 0;
	}	
	
	public void updatePID(double KP, double KD, double KI)
	{
		kp = KP;
		kd = KD;
		ki = KI;
	}
	
	// here is the function that you will need to use
	
	public double pid(int target, double processVar, double timestep)
	{
		// target is the point where both sensors are reading black
		// processVar is the value that you are currently reading
		// timestep is the differnts between the last reading and the current reading
		currentError = (target - processVar);
		pError = kp*currentError;
		dError = kd*(currentError-previousError)/timestep;
		integralError += (currentError+previousError)/2*timestep;
		iError = ki*integralError;
		totalError = pError+dError+iError;
		previousError=currentError;
		return totalError;
	}

}

