package com.mydomain;

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
	
	public void updateGains(double d, double e, double f)
	{
		kp = d;
		kd = e;
		ki = f;
	}
	
	public void capI(int cap)
	{
		if(iError > cap)
			iError = cap;
		else if(-iError > cap)
			iError = -cap;
	}

	public double pid(int target, double processVar, double timestep)
	{
		currentError = (target - processVar);
		pError = kp*currentError;
		dError = kd*(currentError-previousError)/timestep;
		integralError += (currentError+previousError)/2*timestep;
		iError = ki*integralError;
		totalError = pError+dError+iError;
		previousError=currentError;
		return totalError;
		//return totalError + target;
	}
}
